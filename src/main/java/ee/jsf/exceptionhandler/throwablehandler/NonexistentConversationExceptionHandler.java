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
import ee.interceptor.scope.conversation.NonexistentConversationExceptionMessage;
import java.io.IOException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import spec.exception.ThrowableHandler;
import spec.exception.UnexpectedApplicationException;
import spec.message.CanNotMappingHtmlMessagesException;

/**
 * NonexistentConversationException の捕捉後の処理を行う機能を提供します.
 * <p>
 * 会話スコープが既に終わっている場合の実行時例外なので、会話のスタートに位置する{@code index.xhtml}へ遷移させる。
 * 同時に会話スコープも終了（終了しているが念のため終了）させてから、再開させる。
 *
 * @author Yamashita,Takahiro
 */
public class NonexistentConversationExceptionHandler implements ThrowableHandler {

    private final ConversationLifecycleManager conversationLifecycleManager;
    private final FacesContext facesContext;
    private final NonexistentConversationExceptionMessage nonexistentConversationExceptionMessage;

    public NonexistentConversationExceptionHandler(ConversationLifecycleManager conversationLifecycleManager, NonexistentConversationExceptionMessage nonexistentConversationExceptionMessage, FacesContext facesContext) {
        this.conversationLifecycleManager = conversationLifecycleManager;
        this.facesContext = facesContext;
        this.nonexistentConversationExceptionMessage = nonexistentConversationExceptionMessage;
    }

    /**
     * {@inheritDoc }
     * <p>
     * {@code NavigationHandler} では正しく画面遷移が実現しなかったので、{@code ExternalContext} で遷移させます.
     */
    @Override
    public void execute() {
        conversationLifecycleManager.endConversation();
        String contextPath = facesContext.getExternalContext().getRequestContextPath();
        String currentPage = facesContext.getViewRoot().getViewId();
        String indexRootPath = currentPage.substring(0, currentPage.lastIndexOf("/") + 1);
        String indexPage = indexRootPath + "index.xhtml";
        String forwardPage = contextPath + indexPage;
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            if (servletContext.getRealPath(indexPage) == null) {
                throw new UnexpectedApplicationException("Target context file could not find.");
            }
            nonexistentConversationExceptionMessage.setException();
            externalContext.redirect(forwardPage);
        } catch (IOException | UnexpectedApplicationException | CanNotMappingHtmlMessagesException ex) {
            NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
            String errorPage = "/error.xhtml?faces-redirect=true";
            navigationHandler.handleNavigation(facesContext, null, errorPage);
            this.facesContext.renderResponse();
        }
    }

}
