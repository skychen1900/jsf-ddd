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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
public class InputFieldColorHandler {

    private String errorClass;

    @PostConstruct
    public void init() {
        this.errorClass = "error-field";
    }

    public void updateErrorFieldColor(FacesContext context) {
        this.clearErrorColor(context);

        if (context.isValidationFailed() == false) {
            return;
        }

        context.getClientIdsWithMessages().forEachRemaining(clientId -> {
            if (clientId == null) {
                return;
            }
            UIComponent component = context.getViewRoot().findComponent(clientId);
            String styleClass = String.valueOf(component.getAttributes().get("styleClass"));
            if (styleClass != null) {
                component.getAttributes().put("styleClass", styleClass.trim() + " " + errorClass);
            }
        });

    }

    void clearErrorColor(FacesContext context) {

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

}
