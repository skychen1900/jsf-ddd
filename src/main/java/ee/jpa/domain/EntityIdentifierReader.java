/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Copyright © 2019 Yamashita,Takahiro
 */
package ee.jpa.domain;

import base.annotation.domain.EntityIdentifier;
import base.annotation.domain.EntityIdentifierValue;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * {@link base.annotation.domain.EntityIdentifier} でアノテーションされたフィールド情報を取得する機能を提供します.
 * <P>
 * 使用例
 * <ul>
 * <li>
 * Entity（DDD）における、Indentifierを取得することで、Entity（JPA）で複数検索に使用するIDを編集するために使用します.
 * </li>
 * </ul>
 *
 * @author Yamashita,Takahiro
 */
public class EntityIdentifierReader {

    /**
     * Identifierをカンマ区切りで連結した情報を返却します.
     *
     * @param entityObject EntityのIdentifierを取得する対象とするEntityObject
     * @return Identifierをカンマ区切りで連結した文字列
     */
    public Optional<String> inConditionValue(Collection<Object> entityObjects) {

        List<Object> identifierValues = new ArrayList<>();

        for (Object entityObject : entityObjects) {

            boolean hasEntityIdentifier = false;
            for (Field field : entityObject.getClass().getDeclaredFields()) {

                if (field.getAnnotation(EntityIdentifier.class) == null) {
                    continue;
                }

                if (hasEntityIdentifier) {
                    throw new EntityIdentifierException("EntityIdentifier field must annotate once.");
                }

                try {
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        field.setAccessible(true);
                        return null;
                    });

                    Object identifierObj = field.get(entityObject);

                    boolean hasEntityIdentifierValue = false;
                    for (Field identifierFieled : identifierObj.getClass().getDeclaredFields()) {

                        if (identifierFieled.getAnnotation(EntityIdentifierValue.class) == null) {
                            continue;
                        }

                        if (hasEntityIdentifierValue) {
                            throw new EntityIdentifierException("EntityIdentifierValue field must annotate once.");
                        }

                        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                            identifierFieled.setAccessible(true);
                            return null;
                        });
                        Object identifierValue = identifierFieled.get(identifierObj);

                        identifierValues.add(identifierValue);

                        hasEntityIdentifierValue = true;
                    }

                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new EntityIdentifierException("EntityIdentifier field could not get.", ex);
                }

                hasEntityIdentifier = true;
            }
        }

        String idValues = this.toValue(identifierValues);

        return Optional.ofNullable(idValues);

    }

    private String toValue(List<Object> identifierValues) {
        if (identifierValues.isEmpty()) {
            return null;
        }

        List<String> ids = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Object identifierValue : identifierValues) {
            sb.append("'").append(String.valueOf(identifierValue)).append("'");
            ids.add(sb.toString());
            sb.setLength(0);
        }

        String id = String.join(",", ids);
        return id;
    }

}
