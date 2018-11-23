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
package ee.jsf.messages;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;

/**
 * 指定先にメッセージを出力する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
class FacesMessageWriter {

    private final boolean hasHtmlMessages;
    private final FacesContext facesContext;

    private FacesMessageWriter(boolean hasHtmlMessages, FacesContext facesContext) {
        this.hasHtmlMessages = hasHtmlMessages;
        this.facesContext = facesContext;
    }

    static FacesMessageWriter of(FacesContext facesContext) {
        boolean _hasHtmlMessages = hasHtmlMessages(facesContext.getViewRoot().getChildren(), false);
        return new FacesMessageWriter(_hasHtmlMessages, facesContext);
    }

    void write(String clientId, FacesMessage facesMessage) {
        if (clientId == null && this.hasHtmlMessages == false) {
            this.forwardErrorPage();
        }
        this.facesContext.addMessage(clientId, facesMessage);
    }

    void writeHtmlMessages(FacesMessage facesMessage) {
        this.write(null, facesMessage);
    }

    private static boolean hasHtmlMessages(List<UIComponent> uiComponents, boolean hasHtmlMessages) {
        if (hasHtmlMessages) {
            return true;
        }

        boolean _hasHtmlMessages = false;
        for (UIComponent uiComponent : uiComponents) {
            if (uiComponent instanceof HtmlMessages) {
                _hasHtmlMessages = true;
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                _hasHtmlMessages = hasHtmlMessages(uiComponent.getChildren(), _hasHtmlMessages);
            }
        }
        return _hasHtmlMessages;
    }

    private void forwardErrorPage() {
        System.err.println("to error page");
    }

}
