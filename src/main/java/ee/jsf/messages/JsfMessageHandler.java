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
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import spec.presentation.MessageHandler;

/**
 * メッセージ出力する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@Named
@ApplicationScoped
public class JsfMessageHandler implements MessageHandler {

    /**
     * {@inheritDoc }
     */
    @Override
    public void appendMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
        facesContext.addMessage(null, facemsg);

        // リダイレクトしてもFacesMessageが消えないように設定
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }

    @Override
    public void appendMessages(List<String> messages) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        messages.stream()
                .forEachOrdered(message -> {
                    FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                    facesContext.addMessage(null, facemsg);
                });

        // リダイレクトしてもFacesMessageが消えないように設定
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }

}
