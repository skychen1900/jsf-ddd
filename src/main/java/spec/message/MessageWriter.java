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
package spec.message;

import java.util.List;
import spec.message.validation.ClientIdMessages;

/**
 * クライアントにメッセージを出力する機能を提供します
 *
 * @author Yamashita,Takahiro
 */
public interface MessageWriter {

    /**
     * クライアントのメッセージ領域に検証結果を出力します.
     *
     * @param message 出力メッセージ
     */
    public void appendErrorMessage(String message);

    /**
     * クライアントのメッセージ領域に検証結果を出力します.
     *
     * @param messages 出力メッセージ
     */
    public void appendErrorMessages(List<String> messages);

    /**
     * クライアントのメッセージ領域に検証結果を出力します.
     * <P>
     * 入力領域のクライアントＩＤと対になる {@code h:message}も出力対象とします.
     * 出力先となるクライアントＩＤが無い場合（クライアントＩＤが {@code null}）は {@code h:messages}を出力先とします.
     *
     * @param clientIdMessages 出力するクライアントＩＤとメッセージのペア
     */
    public void appendErrorMessageToComponent(ClientIdMessages clientIdMessages);

}
