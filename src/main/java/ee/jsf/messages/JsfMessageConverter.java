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

import ee.validation.ConstraintViolationForMessage;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolator;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolatorFactory;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.interfaces.infrastructure.MessageConverter;
import spec.message.ClientidMessage;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named
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
    public void init() {
        this.interpolatorFactory = MessageInterpolatorFactory.of("Messages", "FormMessages", "FormLabels");
    }

    @Override
    public List<String> toMessages(Collection<ConstraintViolation<?>> constraintViolations) {
        MessageInterpolator interpolator = interpolatorFactory.create(context.clientLocate());
        return constraintViolations.stream()
                .map(interpolator::toMessage)
                .collect(Collectors.toList());
    }

    @Override
    public ClientidMessage toClientidMessage(ConstraintViolationForMessage constraintViolationForMessage) {
        MessageInterpolator interpolator = interpolatorFactory.create(context.clientLocate());
        String message = interpolator.toMessage(constraintViolationForMessage.getConstraintViolation());
        String targetField = constraintViolationForMessage.getId();
        return new ClientidMessage(targetField, message);
    }

}
