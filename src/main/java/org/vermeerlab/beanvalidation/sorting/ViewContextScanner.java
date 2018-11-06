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
package org.vermeerlab.beanvalidation.sorting;

import ddd.domain.javabean.annotation.FieldOrder;
import ddd.presentation.annotation.InvalidMessageMapping;
import ee.domain.annotation.view.ViewContext;
import java.lang.reflect.Field;

/**
 * {@link ddd.presentation.annotation.InvalidMessageMapping}が付与されたフィールド情報を取得して{@link ddd.domain.javabean.annotation.FieldOrder} 順で
 * 並び替えた検証不正メッセージを返却する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class ViewContextScanner {

    Class<?> actionClass;
    MessageTmplateSortKeyMap map;

    private ViewContextScanner(Class<?> actionClass) {
        this.actionClass = actionClass;
        this.map = new MessageTmplateSortKeyMap();
    }

    public static ViewContextScanner of(Class<?> actionClass) {
        return new ViewContextScanner(actionClass);
    }

    public MessageTmplateSortKeyMap scan() {
        Field[] fields = actionClass.getDeclaredFields();
        for (Field field : fields) {
            ViewContext viewContext = field.getAnnotation(ViewContext.class);
            if (viewContext == null) {
                continue;
            }
            resursiveAppendField(field.getType(), field.getType().getCanonicalName());
        }
        return map;
    }

    //
    void resursiveAppendField(Class<?> clazz, String appendKey) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            InvalidMessageMapping invalidMessageMapping = field.getAnnotation(InvalidMessageMapping.class);

            if (invalidMessageMapping == null) {
                continue;
            }

            String fieldOrder = fieldOrder(field);
            String key = appendKey + fieldOrder + field.getName();

            String[] messages = invalidMessageMapping.value();

            for (String message : messages) {
                map.put(message, key);
            }

            this.resursiveAppendField(field.getType(), key);
        }
    }

    //
    String fieldOrder(Field field) {
        short index = Short.MAX_VALUE;

        FieldOrder fieldOrder = field.getAnnotation(FieldOrder.class);
        if (fieldOrder != null) {
            index = fieldOrder.value();
        }

        return String.format("%03d", index);
    }

}
