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
package ee.interceptor.scope.conversation;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.scope.conversation.ConversationExceptionKey;

/**
 * ConversationExceptionが発生した際に画面遷移とメッセージ出力を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@Named
@RequestScoped
public class ConversationExceptionHandler {

    private String exception;
    private String startPage;
    private MessageConverter messageConverter;
    private MessageWriter messageWriter;

    public ConversationExceptionHandler() {
    }

    @Inject
    public ConversationExceptionHandler(MessageConverter messageConverter, MessageWriter messageWriter) {
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
    }

    public String forward() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash()
                .put(ConversationExceptionKey.EXCEPTION, this.exception);
        return this.startPage + "?faces-redirect=true";
    }

    public void writeMessage() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        String value = (String) flash.get(ConversationExceptionKey.EXCEPTION);
        if (value == null || value.equals("")) {
            return;
        }
        System.err.println("ｔｔｔｔｔ");
        String message = messageConverter.toMessage(value);
        messageWriter.appendErrorMessage(message);

        // 一度だけメッセージ出力をするために 共通メソッドで trueにしている設定を falseで上書きします
        flash.setKeepMessages(false);

        flash.remove(ConversationExceptionKey.START_PAGE);
        flash.remove(ConversationExceptionKey.EXCEPTION);
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

}
