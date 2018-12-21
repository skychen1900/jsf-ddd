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

import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolator;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolatorFactory;
import org.vermeerlab.resourcebundle.CustomControl;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.message.MessageConverter;
import spec.message.validation.ClientidMessage_;
import spec.message.validation.ClientidMessages_;
import spec.message.validation.ClientIdsWithComponents;
import spec.message.validation.ConstraintViolationForMessage;
import spec.message.validation.ConstraintViolationForMessages;
import spec.message.validation.MessageMappingInfos;

/**
 *
 * @author Yamashita,Takahiro
 */
@ApplicationScoped
public class JsfMessageConverter implements MessageConverter {

    private MessageInterpolatorFactory interpolatorFactory;

    private CurrentViewContext context;

    public JsfMessageConverter() {
    }

    @Inject
    public JsfMessageConverter(CurrentViewContext context) {
        this.context = context;
    }

    @PostConstruct
    protected void init() {
        this.interpolatorFactory = MessageInterpolatorFactory.of("Messages", "FormMessages", "FormLabels");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toMessage(String message) {
        ResourceBundle.Control control = CustomControl.builder().build();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", context.clientLocate(), control);

        return message == null
               ? "System.Error::message does not set"
               : resourceBundle.containsKey(message)
                 ? resourceBundle.getString(message)
                 : message;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> toMessages(Collection<ConstraintViolation<?>> constraintViolations) {
        MessageInterpolator interpolator = interpolatorFactory.create(context.clientLocate());
        return constraintViolations.stream()
                .map(interpolator::toMessage)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ClientidMessages_ toClientIdMessages(Set<ConstraintViolation<?>> constraintViolationSet, Class<?> actionClass, ClientIdsWithComponents clientIdsWithComponents) {

        MessageMappingInfos messageMappingInfosNotYetReplaceClientId
                            = ViewContextScanner.of(actionClass).messageMappingInfosNotYetReplaceClientId();

        MessageMappingInfos messageMappingInfos
                            = messageMappingInfosNotYetReplaceClientId.replacedClientIds(clientIdsWithComponents);

        ConstraintViolationForMessages constraintViolationForMessages = PresentationConstraintViolationForMessages
                .of(constraintViolationSet, clientIdsWithComponents)
                .toConstraintViolationForMessages();

        return constraintViolationForMessages
                .update(c -> messageMappingInfos.updateConstraintViolationForMessage(c))
                .toClientidMessages(c -> this.toClientidMessage(c));
    }

    private ClientidMessage_ toClientidMessage(ConstraintViolationForMessage constraintViolationForMessage) {
        MessageInterpolator interpolator = interpolatorFactory.create(context.clientLocate());
        String message = interpolator.toMessage(constraintViolationForMessage.getConstraintViolation());
        String targetClientId = constraintViolationForMessage.getId();
        return new ClientidMessage_(targetClientId, message);
    }

}
