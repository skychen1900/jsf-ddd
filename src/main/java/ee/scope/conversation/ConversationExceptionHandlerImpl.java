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
package ee.scope.conversation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import spec.scope.conversation.exception.BusyConversationMessageHandler;
import spec.scope.conversation.exception.ConversationExceptionHandler;
import spec.scope.conversation.exception.ConversationExceptionKey;
import spec.scope.conversation.exception.NonExistentConversationMessageHandler;

/**
 * ConversationExceptionが発生した際に画面遷移とメッセージ出力を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@Named("conversationExceptionHandler")
@RequestScoped
public class ConversationExceptionHandlerImpl implements ConversationExceptionHandler {

    private String forwardPage;
    private String exception;
    private String fromPath;

    private ExternalContext externalContext;

    private BusyConversationMessageHandler busyConversationMessageHandler;
    private NonExistentConversationMessageHandler nonExistentConversationMessageHandler;

    public ConversationExceptionHandlerImpl() {
    }

    @Inject
    public ConversationExceptionHandlerImpl(BusyConversationMessageHandler busyConversationMessageHandler,
                                            NonExistentConversationMessageHandler nonExistentConversationMessageHandler) {
        this.busyConversationMessageHandler = busyConversationMessageHandler;
        this.nonExistentConversationMessageHandler = nonExistentConversationMessageHandler;
    }

    @PostConstruct
    @Override
    public void init() {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }

    @Override
    public String forwardCauseNonexistentConversationException() {
        externalContext.getFlash().putNow(ConversationExceptionKey.EXCEPTION, this.exception);
        externalContext.getFlash().putNow(ConversationExceptionKey.FROM_PATH, this.fromPath);
        return this.getForwardPage();
    }

    @Override
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

    @Override
    public String getException() {
        return exception;
    }

    @Override
    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String getForwardPage() {
        return forwardPage;
    }

    @Override
    public void setForwardPage(String forwardPage) {
        this.forwardPage = forwardPage;
    }

    @Override
    public String getFromPath() {
        return fromPath;
    }

    @Override
    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }

}
