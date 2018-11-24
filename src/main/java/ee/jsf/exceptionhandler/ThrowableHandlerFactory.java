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

import ee.interceptor.scope.conversation.ConversationLifecycleManager;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import spec.exception.ThrowableHandler;
import spec.exception.UnexpectedApplicationException;
import spec.message.CanNotMappingHtmlMessagesException;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.validation.BeanValidationException;

/**
 * {@link CustomExceptionHandler} における Throwable 毎の処理をするクラスを生成する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
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
    public ThrowableHandler createThrowableHandler(Throwable throwable) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (throwable instanceof CanNotMappingHtmlMessagesException) {
            return new CanNotMappingHtmlMessagesExceptionHandler(throwable, conversationLifecycleManager, facesContext);
        }

        if (throwable instanceof BeanValidationException) {
            BeanValidationException beanValidationException = (BeanValidationException) throwable;
            return new BeanValidationExceptionHandler(beanValidationException, messageConverter, messageWriter, facesContext);
        }

        if (throwable instanceof UnexpectedApplicationException) {
            return new UnexpectedApplicationExceptionHandler(throwable, messageConverter, messageWriter, facesContext);
        }

        return new DefaultThrowableHandler(throwable, messageConverter, messageWriter, conversationLifecycleManager, facesContext);
    }

    /**
     * CanNotMappingHtmlMessagesException の補足後の処理を行うクラス
     */
    public static class CanNotMappingHtmlMessagesExceptionHandler implements ThrowableHandler {

        private final Throwable throwable;
        private final ConversationLifecycleManager conversationLifecycleManager;
        private final FacesContext facesContext;

        public CanNotMappingHtmlMessagesExceptionHandler(Throwable throwable, ConversationLifecycleManager conversationLifecycleManager, FacesContext facesContext) {
            this.throwable = throwable;
            this.conversationLifecycleManager = conversationLifecycleManager;
            this.facesContext = facesContext;
        }

        /**
         * {@inheritDoc }
         */
        /**
         * {@inheritDoc }
         */
        @Override
        public void execute() {
            System.err.println(
                    "h:messages is not exist page, message '" + throwable.getMessage() + "' can not mapping page."
            );
            this.conversationLifecycleManager.endConversation();
            NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
            String forwardPage = "/error.xhtml?faces-redirect=true";
            navigationHandler.handleNavigation(facesContext, null, forwardPage);
            this.facesContext.renderResponse();
        }
    }

    /**
     * BeanValidationException の補足後の処理を行うクラス
     */
    public static class BeanValidationExceptionHandler implements ThrowableHandler {

        private final BeanValidationException beanValidationException;
        private final MessageConverter messageConverter;
        private final MessageWriter messageWriter;
        private final FacesContext facesContext;

        public BeanValidationExceptionHandler(BeanValidationException beanValidationException, MessageConverter messageConverter, MessageWriter messageWriter, FacesContext facesContext) {
            this.beanValidationException = beanValidationException;
            this.messageConverter = messageConverter;
            this.messageWriter = messageWriter;
            this.facesContext = facesContext;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public void execute() {

            System.err.println(
                    "Target class must dependency by @Controller or @Action for BeanValidationException."
            );

            List<String> messages = messageConverter.toMessages(beanValidationException.getValidatedResults());
            messageWriter.appendErrorMessages(messages);

            NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();

            String contextPath = facesContext.getExternalContext().getRequestContextPath();
            String currentPage = facesContext.getViewRoot().getViewId();

            String forwardPage = contextPath + currentPage + "?faces-redirect=true";
            navigationHandler.handleNavigation(facesContext, null, forwardPage);
            this.facesContext.renderResponse();
        }
    }

    /**
     * UnexpectedApplicationException の補足後の処理を行うクラス
     */
    public static class UnexpectedApplicationExceptionHandler implements ThrowableHandler {

        private final Throwable throwable;
        private final MessageConverter messageConverter;
        private final MessageWriter messageWriter;
        private final FacesContext facesContext;

        public UnexpectedApplicationExceptionHandler(Throwable throwable, MessageConverter messageConverter, MessageWriter messageWriter, FacesContext facesContext) {
            this.throwable = throwable;
            this.messageConverter = messageConverter;
            this.messageWriter = messageWriter;
            this.facesContext = facesContext;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public void execute() {
            String message = messageConverter.toMessage(throwable.getMessage());
            System.err.println(
                    message
            );

            messageWriter.appendErrorMessage(message);
            NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();

            String contextPath = facesContext.getExternalContext().getRequestContextPath();
            String currentPage = facesContext.getViewRoot().getViewId();

            String forwardPage = contextPath + currentPage + "?faces-redirect=true";
            navigationHandler.handleNavigation(facesContext, null, forwardPage);
            this.facesContext.renderResponse();
        }
    }

    /**
     * 例外発生後の処理を行うデフォルトクラス.
     * <p>
     * 該当するものが無い場合に摘要します.
     */
    public static class DefaultThrowableHandler implements ThrowableHandler {

        private final Throwable throwable;
        private final MessageConverter messageConverter;
        private final MessageWriter messageWriter;
        private final ConversationLifecycleManager conversationLifecycleManager;
        private final FacesContext facesContext;

        public DefaultThrowableHandler(Throwable throwable, MessageConverter messageConverter, MessageWriter messageWriter, ConversationLifecycleManager conversationLifecycleManager, FacesContext facesContext) {
            this.throwable = throwable;
            this.messageConverter = messageConverter;
            this.messageWriter = messageWriter;
            this.conversationLifecycleManager = conversationLifecycleManager;
            this.facesContext = facesContext;
        }

        /**
         * {@inheritDoc }
         */
        @Override
        public void execute() {
            String message = messageConverter.toMessage(throwable.getMessage());
            System.err.println(
                    throwable.getClass().getCanonicalName() + "::" + message
            );

            messageWriter.appendErrorMessage(message);

            this.conversationLifecycleManager.endConversation();
            NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
            String forwardPage = "/error.xhtml?faces-redirect=true";
            navigationHandler.handleNavigation(facesContext, null, forwardPage);
            this.facesContext.renderResponse();
        }

    }

}
