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
import java.util.function.Consumer;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import spec.message.MessageWriter;
import spec.message.validation.ClientidMessages;

/**
 * メッセージ出力する機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
public class JsfMessageWriter implements MessageWriter {

    /**
     * {@inheritDoc }
     */
    @Override
    public void appendErrorMessage(String message) {
        this.templateMethod(facesContext -> {
            FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            facesContext.addMessage(null, facemsg);
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void appendErrorMessages(List<String> messages) {
        this.templateMethod(facesContext -> {
            messages.stream()
                    .forEachOrdered(message -> {
                        FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                        facesContext.addMessage(null, facemsg);
                    });
        });
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void appendErrorMessages(ClientidMessages clientidMessages) {
        this.templateMethod(facesContext -> {
            clientidMessages.getList().stream()
                    .forEachOrdered(clientidMessage -> {
                        FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, clientidMessage.getMessage(), null);
                        facesContext.addMessage(clientidMessage.getClientId(), facemsg);
                    });
        });
    }

    private void templateMethod(Consumer<FacesContext> consumer) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        consumer.accept(facesContext);

        // リダイレクトしてもFacesMessageが消えないように設定
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
    }

}
