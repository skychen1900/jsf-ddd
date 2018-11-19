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
package ee.validation;

import java.util.Collection;
import static java.util.Comparator.comparing;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import spec.interfaces.infrastructure.MessageConverter;
import spec.message.ClientidMessage;
import spec.message.ClientidMessages;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ConstraintViolationsHandler {

    private final MessageConverter messageConverter;
    private final List<ConstraintViolationForMessage> constraintViolationForMessages;

    private ConstraintViolationsHandler(MessageConverter messageConverter, List<ConstraintViolationForMessage> constraintViolationForMessages) {
        this.messageConverter = messageConverter;
        this.constraintViolationForMessages = constraintViolationForMessages;
    }

    public ClientidMessages toClientMessages() {
        List<ClientidMessage> clientidMessages = constraintViolationForMessages.stream()
                .sorted(comparing(ConstraintViolationForMessage::getSortKey)
                        .thenComparing(s -> s.getConstraintViolation().getMessageTemplate()))
                .map(c -> this.messageConverter.toClientidMessage(c))
                .collect(Collectors.toList());
        return new ClientidMessages(clientidMessages);
    }

    public static class Builder {

        private final MessageMappingInfos messageMappingInfos;
        private MessageConverter messageConverter;
        private final TargetClientIds targetClientIds;
        private Set<ConstraintViolation<?>> constraintViolationSet;

        public Builder() {
            messageMappingInfos = new MessageMappingInfos();
            targetClientIds = new TargetClientIds();
            constraintViolationSet = new HashSet<>();
            messageConverter = new DefaultMessageConverter();
        }

        private static class DefaultMessageConverter implements MessageConverter {

            @Override
            public List<String> toMessages(Collection<ConstraintViolation<?>> constraintViolations) {
                return constraintViolations.stream()
                        .map(c -> c.getMessage())
                        .collect(Collectors.toList());
            }

            @Override
            public ClientidMessage toClientidMessage(ConstraintViolationForMessage constraintViolationForMessage) {
                return new ClientidMessage(null, constraintViolationForMessage.getConstraintViolation().getMessage());
            }
        }

        public Builder messageConverter(MessageConverter messageConverter) {
            this.messageConverter = messageConverter;
            return this;
        }

        public Builder messageTemplateAndSortKey(MessageMappingInfos messageTmplateAndSortKey) {
            this.messageMappingInfos.putAll(messageTmplateAndSortKey);
            return this;
        }

        public Builder targetClientIds(TargetClientIds targetClientIds) {
            this.targetClientIds.putAll(targetClientIds);
            return this;
        }

        public Builder constraintViolationSet(Set<ConstraintViolation<?>> constraintViolationSet) {
            this.constraintViolationSet = constraintViolationSet;
            return this;
        }

        public ConstraintViolationsHandler build() {
            return new ConstraintViolationsHandler(
                    messageConverter,
                    PresentationConstraintViolationForMessages
                            .of(constraintViolationSet, targetClientIds)
                            .toConstraintViolationForMessages()
                            .update(c -> messageMappingInfos.updateSortkey(c))
                            .list()
            );

        }

    }
}
