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
package ddd.infrastructure.jsf;

import ddd.presentation.ViewMessage;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolator;
import org.vermeerlab.beanvalidation.messageinterpolator.MessageInterpolatorFactory;

/**
 *
 * @author Yamashita,Takahiro
 */
@Named
@Dependent
public class ViewMessageProducer {

    @Produces
    public ViewMessage getViewMessage() {
        return new JsfViewMessage();
    }

    public static class JsfViewMessage implements ViewMessage {

        @Override
        public void appendMessage(Set<ConstraintViolation<Object>> validatedResults) {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            MessageInterpolatorFactory interpolatorFactory
                                       = MessageInterpolatorFactory.of("Messages", "FormMessages", "FormLabels");

            MessageInterpolator interpolator = interpolatorFactory.create();

            validatedResults.stream()
                    .map(result -> {
                        return interpolator.toMessage(result);
                    })
                    .distinct()
                    .forEach(message -> {

                        FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                        facesContext.addMessage(null, facemsg);
                    });

            // リダイレクトしてもFacesMessageが消えないように設定
            facesContext.getExternalContext().getFlash().setKeepMessages(true);

        }

    }

}
