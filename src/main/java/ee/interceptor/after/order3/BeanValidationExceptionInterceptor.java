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
import ee.jsf.message.ClientComplementManager;
import ee.jsf.message.HtmlMessageScanner;
import ee.jsf.message.InputComponentScanner;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.message.validation.ClientIdMessages;
import spec.message.validation.ClientIdsWithComponents;
import spec.validation.BeanValidationException;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class BeanValidationExceptionInterceptor {

    private final CurrentViewContext context;
    private final MessageConverter messageConverter;
    private final MessageWriter messageWriter;
    private final ClientComplementManager clientComplementManager;
    private final ErrorStyle errorStyle;
    private final ErrorTooltip errorTooltip;

    @Inject
    public BeanValidationExceptionInterceptor(CurrentViewContext context,
                                              MessageConverter messageConverter, MessageWriter messageWriter,
                                              ClientComplementManager clientComplementManager,
                                              ErrorStyle errorStyle, ErrorTooltip errorTooltip) {
        this.context = context;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
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

            ClientIdsWithComponents clientIdsWithInputComponents = new InputComponentScanner().scan();

            ClientIdMessages clientidMessages
                             = messageConverter.toClientIdMessages(ex.getValidatedResults(),
                                                                   ic.getTarget().getClass().getSuperclass(),
                                                                   clientIdsWithInputComponents);

            ClientIdsWithComponents clientIdsWithHtmlMessages = new HtmlMessageScanner().scan();
            messageWriter.appendErrorMessageToComponent(clientidMessages.toClientIdMessagesForWriting(clientIdsWithHtmlMessages));

            FacesContext.getCurrentInstance().validationFailed();
            clientComplementManager.setClientidMessages(clientidMessages);

            this.errorStyle.set(clientIdsWithInputComponents, clientidMessages);
            this.errorTooltip.set(clientIdsWithInputComponents, clientidMessages);
            return currentViewId;
        }

    }
}
