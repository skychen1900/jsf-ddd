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

/**
 * CanNotMappingHtmlMessagesException の補足後の処理を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class CanNotMappingHtmlMessagesExceptionHandler implements ThrowableHandler {

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
    @Override
    public void execute() {
        System.err.println("h:messages is not exist page, message '" + throwable.getMessage() + "' can not mapping page.");
        this.conversationLifecycleManager.endConversation();
        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
        String forwardPage = "/error.xhtml?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

}
