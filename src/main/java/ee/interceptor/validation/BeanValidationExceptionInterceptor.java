/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.validation;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
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
            return currentViewId;
        }

    }
}
