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

import ee.interceptor.scope.conversation.ConversationLifecycleManager;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import spec.exception.ThrowableHandler;
import spec.message.MessageConverter;
import spec.message.MessageWriter;

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
        if (throwable == null) {
            System.err.println("Throwable is null.");
        } else {
            String message = messageConverter.toMessage(throwable.getMessage());
            System.err.println(throwable.getClass().getCanonicalName() + "::" + message);
            messageWriter.appendErrorMessage(message);
        }
        this.conversationLifecycleManager.endConversation();
        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
        String forwardPage = "/error.xhtml?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

}
