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
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import spec.message.validation.ClientIdsWithComponents;

/**
 *
 * @author Yamashita,Takahiro
 */
public class HtmlMessageScanner {

    public ClientIdsWithComponents scan() {
        return recursiveScan(FacesContext.getCurrentInstance().getViewRoot().getChildren(), new ClientIdsWithComponents());
    }

    private ClientIdsWithComponents recursiveScan(List<UIComponent> uiComponents, ClientIdsWithComponents clientIdWithComponent) {
        for (UIComponent uiComponent : uiComponents) {

            /**
             * h:message と対象要素が並列の構造の動作確認が出来ている状態です.
             * 繰り返し領域の対応などをする場合には、改修が必要であると想定されますが 未対応です.
             */
            if (uiComponent instanceof HtmlMessage) {
                Object obj = uiComponent.getAttributes().get("for");
                if (obj != null) {
                    String clientId = uiComponent.getClientId();
                    String id = uiComponent.getId();
                    String targetId = clientId.substring(0, clientId.length() - id.length()) + obj.toString();
                    clientIdWithComponent.put(obj.toString(), targetId);
                }
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                this.recursiveScan(uiComponent.getChildren(), clientIdWithComponent);
            }

        }
        return clientIdWithComponent;
    }

}
