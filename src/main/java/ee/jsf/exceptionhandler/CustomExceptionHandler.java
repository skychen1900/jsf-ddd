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
package ee.jsf.exceptionhandler;

import java.util.Iterator;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import spec.exception.ThrowableHandler;

/**
 * ExceptionHandler
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final ThrowableHandlerFactory throwableHandlerFactory;
    private final ErrorPageNavigator errorPageNavigator;

    CustomExceptionHandler(ExceptionHandler exception, ThrowableHandlerFactory throwableHandlerFactory, ErrorPageNavigator errorPageNavigator) {
        this.wrapped = exception;
        this.throwableHandlerFactory = throwableHandlerFactory;
        this.errorPageNavigator = errorPageNavigator;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() {

        final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();

        while (it.hasNext()) {

            ExceptionQueuedEventContext eventContext = (ExceptionQueuedEventContext) it.next().getSource();

            Throwable causeThrowable = getRootCause(eventContext.getException()).getCause();
            Throwable throwable = causeThrowable != null
                                  ? causeThrowable
                                  : eventContext.getException().getCause();

            ThrowableHandler throwableHandler = this.throwableHandlerFactory.createThrowableHandler(throwable, eventContext);

            try {
                throwableHandler.execute();

            } catch (Exception ex) {
                this.errorPageNavigator.navigate(ex);

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }

            getWrapped().handle();
        }

    }
}
