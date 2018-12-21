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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link ClientidMessage_}の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class ClientidMessages_ {

    private final List<ClientidMessage_> clientIdMessages;

    public ClientidMessages_(List<ClientidMessage_> clientidMessage) {
        this.clientIdMessages = clientidMessage;
    }

    public List<ClientidMessage_> getList() {
        return Collections.unmodifiableList(clientIdMessages);
    }

    public ClientidMessages_ toClientIdMessagesForWriting(ClientIdsWithComponents clientIdsWithMessages) {
        List<ClientidMessage_> _clientidMessage = this.clientIdMessages.stream()
                .map(c -> {
                    String _clientId = clientIdsWithMessages.contains(c.getClientId()) ? c.getClientId() : null;
                    return new ClientidMessage_(_clientId, c.getMessage());
                }).collect(Collectors.toList());
        return new ClientidMessages_(_clientidMessage);
    }

}
