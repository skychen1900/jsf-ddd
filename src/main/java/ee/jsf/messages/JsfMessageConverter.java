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

import ee.validation.PresentationConstraintViolationForMessages;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolator;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolatorFactory;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.message.MessageConverter;
import spec.message.validation.ClientidMessage;
import spec.message.validation.ClientidMessages;
import spec.message.validation.ConstraintViolationForMessage;
import spec.message.validation.ConstraintViolationForMessages;
import spec.message.validation.MessageMappingInfos;
import spec.message.validation.TargetClientIds;

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
    public ClientidMessages toClientidMessages(Set<ConstraintViolation<?>> constraintViolationSet, MessageMappingInfos messageMappingInfosNotYetReplaceClientId) {

        TargetClientIds targetClientIds = this.scanTargetClientIds(
                FacesContext.getCurrentInstance().getViewRoot().getChildren(), new TargetClientIds());

        MessageMappingInfos messageMappingInfos
                            = messageMappingInfosNotYetReplaceClientId.replacedClientIds(targetClientIds);

        ConstraintViolationForMessages constraintViolationForMessages = PresentationConstraintViolationForMessages
                .of(constraintViolationSet, targetClientIds)
                .toConstraintViolationForMessages();

        return constraintViolationForMessages
                .update(c -> messageMappingInfos.updateConstraintViolationForMessage(c))
                .toClientidMessages(c -> this.toClientidMessage(c));
    }

    private TargetClientIds scanTargetClientIds(List<UIComponent> uiComponents, TargetClientIds targetClientIds) {
        for (UIComponent uiComponent : uiComponents) {

            /**
             * h:message と対象要素が並列の構造の動作確認が出来ている状態です.
             * 繰り返し領域の対応などをする場合には、改修が必要であると想定されますが 未対応です.
             */
            if (uiComponent instanceof HtmlMessage) {
                Object obj = uiComponent.getAttributes().get("for");
                if (obj != null) {
                    String clientId = uiComponent.getClientId();
                    String id = uiComponent.getId();
                    String targetId = clientId.substring(0, clientId.length() - id.length()) + obj.toString();
                    targetClientIds.put(obj.toString(), targetId);
                }
            }

            if (uiComponent.getChildren().isEmpty() == false) {
                this.scanTargetClientIds(uiComponent.getChildren(), targetClientIds);
            }

        }
        return targetClientIds;
    }

    private ClientidMessage toClientidMessage(ConstraintViolationForMessage constraintViolationForMessage) {
        MessageInterpolator interpolator = interpolatorFactory.create(context.clientLocate());
        String message = interpolator.toMessage(constraintViolationForMessage.getConstraintViolation());
        String targetClientId = constraintViolationForMessage.getId();
        return new ClientidMessage(targetClientId, message);
    }

}
