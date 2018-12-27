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
 *  Copyright © 2018 Yamashita,Takahiro
 */
package ee.jsf.message.converter;

import base.annotation.FieldOrder;
import base.annotation.presentation.view.View;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import spec.exception.UnexpectedApplicationException;
import spec.context.ClientIds;

/**
 * クライアントメッセージの出力に必要な情報をPresentation層から取得して
 * ConstraintViolationと関連付ける機能を提供します.
 * <P>
 * <ul>
 * <li>{@link base.annotation.FieldOrder} で指定したソート情報を付与します</li>
 * <li>UIComponentで指定した{@code for} で指定した情報がある場合は その項目を対象に、出力対象にない場合は 全体メッセージの対象にします</li>
 * </ul>
 *
 * @author Yamashita,Takahiro
 */
class PresentationConstraintViolationForMessages {

    private final Set<ConstraintViolation<?>> constraintViolationSet;
    private final ClientIds clientIds;

    private PresentationConstraintViolationForMessages(Set<ConstraintViolation<?>> constraintViolationSet, ClientIds clientIds) {
        this.constraintViolationSet = constraintViolationSet;
        this.clientIds = clientIds;
    }

    static PresentationConstraintViolationForMessages of(Set<ConstraintViolation<?>> constraintViolationSet, ClientIds clientIds) {
        return new PresentationConstraintViolationForMessages(constraintViolationSet, clientIds);
    }

    ConstraintViolationForMessages toConstraintViolationForMessages() {
        return new ConstraintViolationForMessages(
                constraintViolationSet
                        .stream()
                        .map(this::toConstraintViolationForMessage)
                        .collect(Collectors.toList())
        );
    }

    //
    private ConstraintViolationForMessage toConstraintViolationForMessage(ConstraintViolation<?> constraintViolation) {
        Class<?> clazz = constraintViolation.getRootBeanClass();
        List<String> paths = Arrays.asList(constraintViolation.getPropertyPath().toString().split("\\."));
        String key = this.recursiveAppendKey(clazz, paths, 0, clazz.getCanonicalName());
        String clientId = this.toClientId(clazz, paths.get(0));
        return new ConstraintViolationForMessage(key, clientId, constraintViolation);
    }

    //
    private String recursiveAppendKey(Class<?> clazz, List<String> paths, Integer index, String appendedKey) {
        if (paths.size() - 1 <= index) {
            return appendedKey;
        }

        String field = paths.get(index);
        String fieldOrder = fieldOrder(clazz, field);
        String key = appendedKey + fieldOrder + field;

        try {
            Class<?> nextClass = clazz.getDeclaredField(field).getType();
            return this.recursiveAppendKey(nextClass, paths, index + 1, key);
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new UnexpectedApplicationException("Target field or filedtype can not get.", ex);
        }
    }

    //
    private String fieldOrder(Class<?> clazz, String property) {
        short index = Short.MAX_VALUE;

        try {
            Field field = clazz.getDeclaredField(property);
            FieldOrder fieldOrder = field.getAnnotation(FieldOrder.class);
            if (fieldOrder != null) {
                index = fieldOrder.value();
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new UnexpectedApplicationException("Target field can not get.", ex);
        }

        return String.format("%03d", index);
    }

    /**
     * View情報から取得される情報から判断できる情報で message出力先を編集します.
     * <p>
     * ここではPresentation層から判断できる判断できる情報だけで編集して、他レイヤーによる更新は別に行います.
     *
     * @param clazz 検証不正のルートとなるクラス
     * @param path 検証不正のルートとなるフィールド名
     * @return 指定したIdが存在しない場合は {@code null}、存在したら フィールド名を返却
     */
    private String toClientId(Class<?> clazz, String path) {
        return clazz.getAnnotation(View.class) != null
               ? this.clientIds.findFirstById(path).orElse(null)
               : null;
    }

}
