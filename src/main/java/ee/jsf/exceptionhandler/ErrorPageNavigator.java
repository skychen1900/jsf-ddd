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
import java.util.Arrays;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * エラー画面への遷移を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class ErrorPageNavigator {

    private ConversationLifecycleManager conversationLifecycleManager;

    public ErrorPageNavigator() {
    }

    @Inject
    public ErrorPageNavigator(ConversationLifecycleManager conversationLifecycleManager) {
        this.conversationLifecycleManager = conversationLifecycleManager;
    }

    /**
     * エラー画面へ遷移させます.
     */
    public void navigate(Exception ex) {
        System.err.println(ex.getMessage());
        System.err.println(Arrays.toString(ex.getStackTrace()));
        conversationLifecycleManager.endConversation();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
        String forwardPage = "/error.xhtml?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        facesContext.renderResponse();
    }
}
