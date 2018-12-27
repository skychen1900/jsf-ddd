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

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import spec.message.validation.ClientIds;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class InputComponentScanner {

    private ClientIds clientIds;

    @PostConstruct
    private void scan() {
        this.clientIds = recursiveScan(FacesContext.getCurrentInstance().getViewRoot().getChildren(), new ClientIds());
    }

    private ClientIds recursiveScan(List<UIComponent> uiComponents, ClientIds clientIds) {
        for (UIComponent uiComponent : uiComponents) {

            if (uiComponent instanceof UIInput) {
                clientIds.put(uiComponent.getId(), uiComponent.getClientId());
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                this.recursiveScan(uiComponent.getChildren(), clientIds);
            }

        }
        return clientIds;
    }

    public ClientIds getClientIds() {
        return clientIds;
    }

}
