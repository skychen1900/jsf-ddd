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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import spec.message.validation.ClientIdMessage;
import spec.message.validation.ClientIdMessages;
import spec.message.validation.ClientIdsWithComponents;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named
@RequestScoped
public class ErrorStyle {

    private ClientIdsWithComponents clientIdsWithComponents;

    private String errorStyle;

    @PostConstruct
    private void init() {
        this.errorStyle = "error";
        this.clientIdsWithComponents = new ClientIdsWithComponents();
    }

    public void set(ClientIdsWithComponents clientIdsWithInputComponents, ClientIdMessages clientidMessages) {

        Set<String> clientIds = clientidMessages.getList().stream()
                .map(ClientIdMessage::getClientId)
                .collect(Collectors.toSet());

        this.clientIdsWithComponents = clientIdsWithInputComponents.filter(clientIds);
    }

    /**
     * 指定したIDの項目が検証不正だった場合に適用する styleClass を返却します.
     * <P>
     * xhtmlでのパラメータ指定時には、シングルクウォートで値を指定してください.
     *
     * @param id 対象となるコンポーネントのID（JSFのクライアントＩＤではありません）
     * @return 当該項目IDにエラーがない場合は 空文字を返却します.
     */
    public String byId(String id) {
        return this.clientIdsWithComponents.getOrNull(id) == null ? "" : errorStyle;

    }

}
