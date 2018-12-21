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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import spec.message.validation.ClientIdsWithComponents;

/**
 *
 * @author Yamashita,Takahiro
 */
public class InputComponentScanner {

    public ClientIdsWithComponents scan() {
        return recursiveScan(FacesContext.getCurrentInstance().getViewRoot().getChildren(), new ClientIdsWithComponents());
    }

    private ClientIdsWithComponents recursiveScan(List<UIComponent> uiComponents, ClientIdsWithComponents clientIdWithComponent) {
        for (UIComponent uiComponent : uiComponents) {

            if (uiComponent instanceof UIInput) {
                clientIdWithComponent.put(uiComponent.getId(), uiComponent.getClientId());
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                this.recursiveScan(uiComponent.getChildren(), clientIdWithComponent);
            }

        }
        return clientIdWithComponent;
    }

}
