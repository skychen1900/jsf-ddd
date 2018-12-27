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
package ee.jsf.message.converter;

import spec.context.ClientIds;
import ee.jsf.context.ClientIdsImpl;

/**
 * {@link spec.annotation.presentation.view.InvalidMessageMapping} でマークしたフィールドの情報を扱う機能を提供します.
 * <p>
 * {@code sortKey} が１つだけとしている理由は、同一メッセージに対して上位順位のもののみを保持するようにしているためです.<br>
 * ClientIdを複数保持している理由は、同一メッセージに対して複数のクライアントＩＤが割り当てられることを想定しているためです。
 *
 * @author Yamashita,Takahiro
 */
class MessageMappingInfo {

    private final String message;
    private final String sortKey;
    private final ClientIds clientIds;

    MessageMappingInfo(String message, String sortKey, String clientId) {
        this.message = message;
        this.sortKey = sortKey;

        ClientIds _clientIds = new ClientIdsImpl();
        _clientIds.put(clientId);
        this.clientIds = _clientIds;
    }

    MessageMappingInfo(String message, String sortKey, ClientIds clientIds) {
        this.message = message;
        this.sortKey = sortKey;
        this.clientIds = clientIds;
    }

    boolean isUpdate(String sortKey) {
        return this.sortKey.compareTo(sortKey) == 1;
    }

    String getMessage() {
        return message;
    }

    String getSortKey() {
        return sortKey;
    }

    ClientIds getClientIds() {
        return clientIds;
    }

    String firstClientId() {
        return clientIds.findFirstByClientId(clientIds).orElse(null);
    }

}
