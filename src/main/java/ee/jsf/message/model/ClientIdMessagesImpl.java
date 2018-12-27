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
package ee.jsf.message.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import spec.message.validation.ClientIdMessage;
import spec.message.validation.ClientIdMessages;
import spec.message.validation.ClientIds;

/**
 * {@link ClientIdMessage}の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class ClientIdMessagesImpl implements ClientIdMessages {

    private final List<ClientIdMessage> clientIdMessages;

    /**
     * key:clientId
     */
    private final Map<String, List<ClientIdMessage>> clientIdMessageMap;

    public ClientIdMessagesImpl() {
        this.clientIdMessages = new ArrayList<>();
        this.clientIdMessageMap = new HashMap<>();
    }

    public ClientIdMessagesImpl(List<ClientIdMessage> clientidMessages) {
        this.clientIdMessages = clientidMessages;

        Map<String, List<ClientIdMessage>> _clientIdMessageMap = new HashMap<>();
        for (ClientIdMessage _clientidMessage : clientidMessages) {
            List<ClientIdMessage> _clientIdMessageList = _clientIdMessageMap.getOrDefault(_clientidMessage.getClientId(), new ArrayList<>());
            _clientIdMessageList.add(_clientidMessage);
            _clientIdMessageMap.put(_clientidMessage.getClientId(), _clientIdMessageList);
        }
        this.clientIdMessageMap = _clientIdMessageMap;
    }

    @Override
    public Set<String> getClientIds() {
        return this.clientIdMessages.stream()
                .map(ClientIdMessage::getClientId)
                .collect(Collectors.toSet());
    }

    @Override
    public void forEachOrdered(Consumer<? super ClientIdMessage> action) {
        this.clientIdMessages.stream().forEachOrdered(clientIdMessage -> action.accept(clientIdMessage));

    }

    @Override
    public ClientIdMessages toClientIdMessagesForWriting(ClientIds clientIds) {
        List<ClientIdMessage> _clientidMessage = this.clientIdMessages.stream()
                .map(c -> {
                    String _clientId = clientIds.contains(c.getClientId()) ? c.getClientId() : null;
                    return new ClientIdMessageImpl(_clientId, c.getMessage());
                }).collect(Collectors.toList());
        return new ClientIdMessagesImpl(_clientidMessage);
    }

    @Override
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

    @Override
    public ClientIdMessages filter(Predicate<String> predicate) {
        List<ClientIdMessage> _clientidMessage = this.clientIdMessages.stream()
                .map(c -> {
                    String _clientId = predicate.test(c.getClientId()) ? c.getClientId() : null;
                    return new ClientIdMessageImpl(_clientId, c.getMessage());
                }).collect(Collectors.toList());
        return new ClientIdMessagesImpl(_clientidMessage);
    }

}
