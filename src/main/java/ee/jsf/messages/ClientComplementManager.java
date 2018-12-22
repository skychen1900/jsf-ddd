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
import javax.enterprise.context.RequestScoped;
import spec.message.validation.ClientIdMessage;
import spec.message.validation.ClientIdMessages;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class ClientComplementManager {

    private ClientIdMessages clientidMessages;

    public void setClientidMessages(ClientIdMessages clientidMessages) {
        this.clientidMessages = clientidMessages;
    }

    public Set<String> clientIds() {
        return this.clientidMessages.getList().stream()
                .map(ClientIdMessage::getClientId)
                .collect(Collectors.toSet());
    }

}
