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
package ee.jsf.message;

import java.lang.reflect.Field;
import base.annotation.FieldOrder;
import base.annotation.presentation.controller.ViewContext;
import base.annotation.presentation.view.InvalidMessageMapping;
import spec.message.validation.MessageMappingInfos;

/**
 * Controllerと関連付くViewクラス（{@link base.annotation.presentation.controller.ViewContext}で特定したクラス）から
 * {@link base.annotation.presentation.view.InvalidMessageMapping}が付与されたフィールド情報を取得する機能を提供します.
 * <p>
 * {@link base.annotation.FieldOrder} により 出力するメッセージの順序を指定します。
 *
 * @author Yamashita,Takahiro
 */
class ViewContextScanner {

    Class<?> actionClass;
    MessageMappingInfos messageMappingInfos;

    private ViewContextScanner(Class<?> actionClass) {
        this.actionClass = actionClass;
        this.messageMappingInfos = new MessageMappingInfos();
    }

    static ViewContextScanner of(Class<?> actionClass) {
        return new ViewContextScanner(actionClass);
    }

    /**
     * メッセージとプロパティを関連付けた情報を返却します.
     * <p>
     * 保持しているクライアントＩＤは、取得したプロパティ名のまま（クライアントＩＤへ変換する前の状態）です.
     *
     * @return メッセージとプロパティを関連付けた情報
     */
    MessageMappingInfos messageMappingInfosNotYetReplaceClientId() {
        Field[] fields = actionClass.getDeclaredFields();
        for (Field field : fields) {
            ViewContext viewContext = field.getAnnotation(ViewContext.class);
            if (viewContext == null) {
                continue;
            }
            resursiveAppendField(field.getType(), field.getType().getCanonicalName());
        }
        return messageMappingInfos;
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
                messageMappingInfos.put(message, key, field.getName());
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
