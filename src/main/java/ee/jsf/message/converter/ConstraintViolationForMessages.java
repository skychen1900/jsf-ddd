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

import ee.jsf.message.model.ClientIdMessageImpl;
import ee.jsf.message.model.ClientIdMessagesImpl;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import spec.message.ClientIdMessage;
import spec.message.ClientIdMessages;

/**
 * {@link ConstraintViolationForMessage} の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
class ConstraintViolationForMessages {

    private final List<ConstraintViolationForMessage> items;

    ConstraintViolationForMessages(List<ConstraintViolationForMessage> constraintViolationForMessages) {
        this.items = constraintViolationForMessages;
    }

    /**
     * 関数で情報を更新した新たなインスタンスを返却します.
     * <P>
     * 循環参照をさせないために関数型で呼出し元で処理します.
     *
     * @param unaryOperator 更新する関数
     * @return 更新した新たなインスタンス
     */
    ConstraintViolationForMessages update(UnaryOperator<ConstraintViolationForMessage> unaryOperator) {
        return new ConstraintViolationForMessages(
                items.stream()
                        .map(c -> unaryOperator.apply(c))
                        .collect(Collectors.toList())
        );
    }

    /**
     * クライアントＩＤとメッセージの組み合わせた情報に変換した情報を返却します.
     * <p>
     * 出力順序は本メソッドで行い、メッセージの出力用変換は呼び出し側のクラスから関数によって編集を行います.
     *
     * @param function メッセージの出力変換を行う関数
     * @return 変換したクライアントＩＤとメッセージの組み合わせた情報
     */
    ClientIdMessages toClientidMessages3(Function<ConstraintViolationForMessage, ClientIdMessage> function) {
        List<ClientIdMessage> clientidMessages = this.items.stream()
                .sorted(comparing(ConstraintViolationForMessage::getSortKey)
                        .thenComparing(s -> s.getConstraintViolation().getMessageTemplate()))
                .map(c -> function.apply(c))
                .collect(Collectors.toList());
        return new ClientIdMessagesImpl(clientidMessages);
    }

    /**
     * クライアントＩＤとメッセージの組み合わせた情報に変換した情報を返却します.
     * <p>
     * 出力順序は本メソッドで行い、メッセージの出力用変換は呼び出し側のクラスから関数によって編集を行います.
     *
     * @param messageConverterFunction メッセージの変換を行う関数
     * @return 変換したクライアントＩＤとメッセージの組み合わせた情報
     */
    ClientIdMessages toClientidMessages(Function<ConstraintViolation<?>, String> messageConverterFunction) {
        List<ClientIdMessage> clientidMessages = this.items.stream()
                .sorted(comparing(ConstraintViolationForMessage::getSortKey)
                        .thenComparing(s -> s.getConstraintViolation().getMessageTemplate()))
                .map(c -> {
                    String message = messageConverterFunction.apply(c.getConstraintViolation());
                    return new ClientIdMessageImpl(c.geClientId(), message);
                })
                .collect(Collectors.toList());
        return new ClientIdMessagesImpl(clientidMessages);
    }

}
