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
package ee.jsf.scope.conversation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import spec.scope.conversation.ConversationExceptionKey;

/**
 * ConversationExceptionが発生した際に画面遷移とメッセージ出力を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@Named
@RequestScoped
public class ConversationExceptionHandler {

    private String forwardPage;
    private String exception;
    private String fromPath;

    private ExternalContext externalContext;

    private BusyConversationMessageHandler busyConversationMessageHandler;
    private NonExistentConversationMessageHandler nonExistentConversationMessageHandler;

    public ConversationExceptionHandler() {
    }

    @Inject
    public ConversationExceptionHandler(BusyConversationMessageHandler busyConversationMessageHandler,
                                        NonExistentConversationMessageHandler nonExistentConversationMessageHandler) {
        this.busyConversationMessageHandler = busyConversationMessageHandler;
        this.nonExistentConversationMessageHandler = nonExistentConversationMessageHandler;
    }

    @PostConstruct
    public void init() {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }

    public String forwardCauseNonexistentConversationException() {
        externalContext.getFlash().put(ConversationExceptionKey.EXCEPTION, this.exception);
        externalContext.getFlash().put(ConversationExceptionKey.FROM_PATH, this.fromPath);
        return this.getForwardPage();
    }

    public void writeMessage() {
        String flashException = (String) externalContext.getFlash().get(ConversationExceptionKey.EXCEPTION);
        String requestParameterException = externalContext.getRequestParameterMap().get(ConversationExceptionKey.EXCEPTION);

        if (busyConversationMessageHandler.isBusyConversationException(flashException, requestParameterException) == false
            && nonExistentConversationMessageHandler.isNonExistentConversation(flashException) == false) {
            return;
        }

        if (busyConversationMessageHandler.isBusyConversationException(flashException, requestParameterException)) {
            this.busyConversationMessageHandler.write();
        }

        if (nonExistentConversationMessageHandler.isNonExistentConversation(flashException)) {
            this.nonExistentConversationMessageHandler
                    .write((String) externalContext.getFlash().get(ConversationExceptionKey.FROM_PATH));
        }

        // 一度だけメッセージ出力をするために 共通メソッドで trueにしている設定を falseで上書きします
        externalContext.getFlash().setKeepMessages(false);
    }

    private boolean isNotTarget(String flashException, String requestParameterException) {
        return nonExistentConversationMessageHandler.isNonExistentConversation(flashException) == false
               && busyConversationMessageHandler.isBusyConversationException(flashException, requestParameterException) == false;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getForwardPage() {
        return forwardPage;
    }

    public void setForwardPage(String forwardPage) {
        this.forwardPage = forwardPage;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }

}
