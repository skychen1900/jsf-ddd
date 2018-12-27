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

import spec.message.ComponentErrorMessageWriter;
import ee.jsf.context.HtmlMessageScanner;
import ee.jsf.context.InputComponentScanner;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import spec.message.ClientIdMessages;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.context.ClientIds;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class ComponentErrorMessageWriterImpl implements ComponentErrorMessageWriter {

    private ClientIdMessages clientIdMessages;

    private MessageConverter messageConverter;
    private MessageWriter messageWriter;
    private InputComponentScanner inputComponentScanner;
    private HtmlMessageScanner htmlMessageScanner;

    public ComponentErrorMessageWriterImpl() {
    }

    @Inject
    public ComponentErrorMessageWriterImpl(MessageConverter messageConverter, MessageWriter messageWriter, InputComponentScanner inputComponentScanner, HtmlMessageScanner htmlMessageScanner) {
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.inputComponentScanner = inputComponentScanner;
        this.htmlMessageScanner = htmlMessageScanner;
    }

    @Override
    public void write(Set<ConstraintViolation<?>> constraintViolationSet, Class<?> actionClass) {
        ClientIds _inputClientIds = this.inputComponentScanner.getClientIds();
        ClientIdMessages _clientIdMessages = this.toClientIdMessages(constraintViolationSet, actionClass, _inputClientIds);

        ClientIds _htmlMessageClientIds = this.htmlMessageScanner.getClientIds();
        ClientIdMessages clientIdMessagesForWriting = _clientIdMessages.filter(_htmlMessageClientIds::contains);
        messageWriter.appendErrorMessageToComponent(clientIdMessagesForWriting);

        this.clientIdMessages = _clientIdMessages;
    }

    private ClientIdMessages toClientIdMessages(Set<ConstraintViolation<?>> constraintViolationSet, Class<?> actionClass, ClientIds clientIds) {

        MessageMappingInfos messageMappingInfosNotYetReplaceClientId
                            = ViewContextScanner.of(actionClass).messageMappingInfosNotYetReplaceClientId();

        MessageMappingInfos messageMappingInfos
                            = messageMappingInfosNotYetReplaceClientId.replacedClientIds(clientIds);

        ConstraintViolationForMessages constraintViolationForMessages = PresentationConstraintViolationForMessages
                .of(constraintViolationSet, clientIds)
                .toConstraintViolationForMessages();

        return constraintViolationForMessages
                .update(c -> messageMappingInfos.updateConstraintViolationForMessage(c))
                .toClientidMessages(c -> this.messageConverter.toMessageFromConstraintViolation(c));
    }

    @Override
    public ClientIdMessages getClientIdMessages() {
        return clientIdMessages;
    }

}
