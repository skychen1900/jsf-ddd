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
package spec.message.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link ClientIdMessage}の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class ClientIdMessages {

    private final List<ClientIdMessage> clientIdMessages;

    /**
     * key:clientId
     */
    private final Map<String, List<ClientIdMessage>> clientIdMessageMap;

    public ClientIdMessages() {
        this.clientIdMessages = new ArrayList<>();
        this.clientIdMessageMap = new HashMap<>();
    }

    public ClientIdMessages(List<ClientIdMessage> clientidMessages) {
        this.clientIdMessages = clientidMessages;

        Map<String, List<ClientIdMessage>> _clientIdMessageMap = new HashMap<>();
        for (ClientIdMessage _clientidMessage : clientidMessages) {
            List<ClientIdMessage> _clientIdMessageList = _clientIdMessageMap.getOrDefault(_clientidMessage.getClientId(), new ArrayList<>());
            _clientIdMessageList.add(_clientidMessage);
            _clientIdMessageMap.put(_clientidMessage.getClientId(), _clientIdMessageList);
        }
        this.clientIdMessageMap = _clientIdMessageMap;
    }

    public List<ClientIdMessage> getList() {
        return Collections.unmodifiableList(clientIdMessages);
    }

    public ClientIdMessages toClientIdMessagesForWriting(ClientIdsWithComponents clientIdsWithMessages) {
        List<ClientIdMessage> _clientidMessage = this.clientIdMessages.stream()
                .map(c -> {
                    String _clientId = clientIdsWithMessages.contains(c.getClientId()) ? c.getClientId() : null;
                    return new ClientIdMessage(_clientId, c.getMessage());
                }).collect(Collectors.toList());
        return new ClientIdMessages(_clientidMessage);
    }

    public String getMessage(String clientId) {
        List<ClientIdMessage> _clientIdMessages = this.clientIdMessageMap.getOrDefault(clientId, new ArrayList<>());

        List<String> messages = new ArrayList<>();
        for (ClientIdMessage _clientIdMessage : _clientIdMessages) {
            messages.add(_clientIdMessage.getMessage());
        }

        return messages.isEmpty()
               ? ""
               : String.join("\n", messages);
    }

}
