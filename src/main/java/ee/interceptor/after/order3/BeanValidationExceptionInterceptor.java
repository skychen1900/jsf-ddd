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
package ee.interceptor.after.order3;

import base.annotation.presentation.controller.Action;
import base.xhtml.error.ErrorStyle;
import base.xhtml.error.ErrorTooltip;
import javax.annotation.Priority;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.context.CurrentViewContext;
import spec.message.ClientComplementManager;
import spec.message.ClientIdMessages;
import spec.message.ComponentErrorMessageWriter;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.validation.BeanValidationException;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class BeanValidationExceptionInterceptor {

    private final CurrentViewContext context;

    private final ComponentErrorMessageWriter componentErrorMessageWriter;
    private final ClientComplementManager clientComplementManager;
    private final ErrorStyle errorStyle;
    private final ErrorTooltip errorTooltip;

    @Inject
    public BeanValidationExceptionInterceptor(CurrentViewContext context,
                                              MessageConverter messageConverter, MessageWriter messageWriter,
                                              ClientComplementManager clientComplementManager,
                                              ComponentErrorMessageWriter componentErrorMessageWriter,
                                              ErrorStyle errorStyle, ErrorTooltip errorTooltip) {
        this.context = context;
        this.componentErrorMessageWriter = componentErrorMessageWriter;
        this.clientComplementManager = clientComplementManager;
        this.errorStyle = errorStyle;
        this.errorTooltip = errorTooltip;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        String currentViewId = context.currentViewId();

        try {
            return ic.proceed();
        } catch (BeanValidationException ex) {

            FacesContext.getCurrentInstance().validationFailed();

            this.componentErrorMessageWriter.write(ex.getValidatedResults(), ic.getTarget().getClass().getSuperclass());

            ClientIdMessages clientIdMessages = this.componentErrorMessageWriter.getClientIdMessages();

            this.clientComplementManager.setClientidMessages(clientIdMessages);
            this.errorStyle.set(clientIdMessages);
            this.errorTooltip.set(clientIdMessages);
            return currentViewId;
        }

    }
}
