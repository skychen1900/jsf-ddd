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
 *  Copyright ? 2018 Yamashita,Takahiro
 */
package ee.jsf.exceptionhandler.throwablehandler;

import spec.exception.ThrowableHandler;
import spec.exception.ThrowableHandlerException;

/**
 * CanNotMappingHtmlMessagesException の補足後の処理を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class CanNotMappingHtmlMessagesExceptionHandler implements ThrowableHandler {

    private final Throwable throwable;

    public CanNotMappingHtmlMessagesExceptionHandler(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * {@inheritDoc }
     * <p>
     * メッセージの出力先領域が無いため、エラー画面へ遷移します.
     */
    @Override
    public void execute() {
        throw new ThrowableHandlerException("h:messages is not exist page, message '" + throwable.getMessage() + "' can not mapping page.");
    }

}
