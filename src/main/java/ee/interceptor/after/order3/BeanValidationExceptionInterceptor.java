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

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.Action;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.message.validation.ClientidMessages;
import spec.validation.BeanValidationException;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class BeanValidationExceptionInterceptor {

    private final CurrentViewContext context;

    private final MessageConverter messageConverter;
    private final MessageWriter messageWriter;

    @Inject
    public BeanValidationExceptionInterceptor(CurrentViewContext context, MessageConverter messageConverter, MessageWriter messageWriter) {
        this.context = context;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        String currentViewId = context.currentViewId();

        try {
            return ic.proceed();
        } catch (BeanValidationException ex) {
            ClientidMessages clientidMessages
                             = messageConverter.toClientidMessages(ex.getValidatedResults(),
                                                                   ic.getTarget().getClass().getSuperclass());

            messageWriter.appendErrorMessages(clientidMessages);
            FacesContext.getCurrentInstance().validationFailed();
            return currentViewId;
        }

    }
}
