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
package ee.jsf.context;

import ee.validation.TargetClientIds;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class UIComponentHandler {

    FacesContext context;

    @PostConstruct
    private void init() {
        context = FacesContext.getCurrentInstance();
    }

    public TargetClientIds filterMessageTargets() {
        return this.targetClientId(context.getViewRoot().getChildren(), 0, new TargetClientIds());
    }

    private TargetClientIds targetClientId(List<UIComponent> uiComponents, int depth, TargetClientIds targetClientIds) {
        for (UIComponent uiComponent : uiComponents) {

            if (uiComponent instanceof HtmlMessage) {
                Object obj = uiComponent.getAttributes().get("for");
                if (obj != null) {
                    String clientId = uiComponent.getClientId();
                    String id = uiComponent.getId();
                    String targetId = clientId.substring(0, clientId.length() - id.length()) + obj.toString();
                    targetClientIds.put(obj.toString(), targetId);
                }
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                this.targetClientId(uiComponent.getChildren(), depth + 1, targetClientIds);
            }

        }

        return targetClientIds;

    }

}
