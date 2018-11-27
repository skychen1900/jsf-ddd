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
import spec.message.MessageConverter;

/**
 * 例外発生後の処理を行うデフォルトの処理を行う機能を提供します..
 * <p>
 * 該当するものが無い場合に摘要します.
 *
 * @author Yamashita,Takahiro
 */
public class DefaultThrowableHandler implements ThrowableHandler {

    private final Throwable throwable;
    private final MessageConverter messageConverter;

    public DefaultThrowableHandler(Throwable throwable, MessageConverter messageConverter) {
        this.throwable = throwable;
        this.messageConverter = messageConverter;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void execute() {
        String message = throwable == null
                         ? "Throwable is null."
                         : messageConverter.toMessage(throwable.getMessage());
        throw new ThrowableHandlerException(message);
    }

}
