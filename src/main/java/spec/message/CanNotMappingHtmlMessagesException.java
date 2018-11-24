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

/**
 * クライアントIDで{@code null}を指定してメッセージを出力した際、メッセージリストの出力領域（{@code h:messages}）が無い場合に発行する実行時例外です.
 * <P>
 * 出力先となる領域が無いことにより、目的のメッセージが出力できないことを示す 実行時例外を発行する際に使用します.
 *
 * @author Yamashita,Takahiro
 */
public class CanNotMappingHtmlMessagesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CanNotMappingHtmlMessagesException() {
    }

    public CanNotMappingHtmlMessagesException(String message) {
        super(message);
    }

    public CanNotMappingHtmlMessagesException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotMappingHtmlMessagesException(Throwable cause) {
        super(cause);
    }

    public CanNotMappingHtmlMessagesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
