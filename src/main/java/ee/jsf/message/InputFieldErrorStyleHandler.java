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
package ee.jsf.message;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
public class InputFieldErrorStyleHandler {

    private String errorClass;

    @PostConstruct
    public void init() {
        this.errorClass = "error-field";
    }

    public void updateErrorStyle(FacesContext context, ClientComplementManager clientComplementManager) {
        this.clearErrorStyle(context);

        if (context.isValidationFailed() == false) {
            return;
        }

        this.updateColorHtmlMessage(context);
        this.updateColorInputComponent(context, clientComplementManager);
    }

    private void clearErrorStyle(FacesContext context) {

        recursiveScan(context.getViewRoot().getChildren())
                .forEach(c -> {
                    String styleClass = String.valueOf(c.getAttributes().get("styleClass"));
                    if (styleClass != null && styleClass.contains(errorClass)) {
                        c.getAttributes().put("styleClass", styleClass.replace(errorClass, "").trim());
                    }
                });

    }

    private Set<UIComponent> recursiveScan(List<UIComponent> components) {
        Set<UIComponent> set = new HashSet<>();
        if (components == null) {
            return set;
        }

        components.forEach(component -> {
            set.add(component);
            set.addAll(recursiveScan(component.getChildren()));
        });
        return set;
    }

    private void updateColorHtmlMessage(FacesContext context) {
        context.getClientIdsWithMessages()
                .forEachRemaining(clientId -> this.updateBackgroundColor(context, clientId));
    }

    private void updateColorInputComponent(FacesContext context, ClientComplementManager clientComplementManager) {
        clientComplementManager.clientIds().stream()
                .forEach(clientId -> this.updateBackgroundColor(context, clientId));
    }

    private void updateBackgroundColor(FacesContext context, String clientId) {
        if (clientId == null) {
            return;
        }
        UIInput uiInput = (UIInput) context.getViewRoot().findComponent(clientId);
        uiInput.setValid(false);
        String styleClass = String.valueOf(uiInput.getAttributes().get("styleClass"));
        uiInput.getAttributes().put("styleClass", styleClass.trim() + " " + errorClass);
    }

}
