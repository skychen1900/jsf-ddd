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

import ee.jsf.scope.conversation.ConversationLifecycleManager;
import ee.jsf.exceptionhandler.throwablehandler.BeanValidationExceptionHandler;
import ee.jsf.exceptionhandler.throwablehandler.CanNotMappingHtmlMessagesExceptionHandler;
import ee.jsf.exceptionhandler.throwablehandler.DefaultThrowableHandler;
import ee.jsf.exceptionhandler.throwablehandler.IllegalConversationStartpathExceptionHandler;
import ee.jsf.exceptionhandler.throwablehandler.UnexpectedApplicationExceptionHandler;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.inject.Inject;
import spec.exception.ThrowableHandler;
import spec.exception.UnexpectedApplicationException;
import spec.message.CanNotMappingHtmlMessagesException;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.scope.conversation.IllegalConversationStartpathException;
import spec.validation.BeanValidationException;

/**
 * {@link CustomExceptionHandler} における Throwable 毎の処理をするクラスを生成する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class ThrowableHandlerFactory {

    private MessageConverter messageConverter;
    private MessageWriter messageWriter;
    private ConversationLifecycleManager conversationLifecycleManager;

    public ThrowableHandlerFactory() {
    }

    @Inject
    public ThrowableHandlerFactory(MessageConverter messageConverter, MessageWriter messageWriter, ConversationLifecycleManager conversationLifecycleManager) {
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.conversationLifecycleManager = conversationLifecycleManager;
    }

    /**
     * 例外に応じた操作を行うクラスを返却します.
     *
     * @param throwable 例外
     * @return 例外に応じた操作を行うクラス
     */
    public ThrowableHandler createThrowableHandler(Throwable throwable, ExceptionQueuedEventContext eventContext) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (throwable instanceof CanNotMappingHtmlMessagesException) {
            return new CanNotMappingHtmlMessagesExceptionHandler(throwable);
        }

        if (throwable instanceof BeanValidationException) {
            BeanValidationException beanValidationException = (BeanValidationException) throwable;
            return new BeanValidationExceptionHandler(beanValidationException, messageConverter, messageWriter, facesContext);
        }

        if (throwable instanceof UnexpectedApplicationException) {
            return new UnexpectedApplicationExceptionHandler(throwable, messageConverter, messageWriter, facesContext);
        }

        if (throwable instanceof IllegalConversationStartpathException) {
            return new IllegalConversationStartpathExceptionHandler(conversationLifecycleManager, facesContext);
        }

        return new DefaultThrowableHandler(throwable, messageConverter);
    }

}
