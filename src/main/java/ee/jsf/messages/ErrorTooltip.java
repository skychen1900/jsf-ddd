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

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import spec.message.validation.ClientIdMessages;
import spec.message.validation.ClientIdsWithComponents;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named
@RequestScoped
public class ErrorTooltip {

    private ClientIdsWithComponents clientIdsWithComponents;
    private ClientIdMessages clientIdMessages;

    @PostConstruct
    private void init() {
        this.clientIdsWithComponents = new ClientIdsWithComponents();
        this.clientIdMessages = new ClientIdMessages();
    }

    public void set(ClientIdsWithComponents clientIdsWithInputComponents, ClientIdMessages clientIdMessages) {

        Set<String> clientIds = clientIdMessages.getClientIds();
        this.clientIdsWithComponents = clientIdsWithInputComponents.filter(clientIds);
        this.clientIdMessages = clientIdMessages;
    }

    /**
     * 指定したIDの項目が検証不正だった場合に適用する メッセージ を返却します.
     * <P>
     * xhtmlでのパラメータ指定時には、シングルクウォートで値を指定してください.
     *
     * @param id 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    public String byId(String id) {
        String clientId = this.clientIdsWithComponents.getOrNull(id);
        if (clientId == null) {
            return "";
        }
        return clientIdMessages.getMessage(clientId);
    }

    /**
     * 指定したClientId（フルパス）の項目が検証不正だった場合に適用する メッセージ を返却します.
     *
     * @param clientId 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    public String byClientId(String clientId) {
        return clientIdMessages.getMessage(clientId);
    }

}
