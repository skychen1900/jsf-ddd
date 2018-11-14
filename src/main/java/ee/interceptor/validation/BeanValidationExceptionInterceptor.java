/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.validation;

import ee.validation.ConstraintViolationsHandler;
import ee.validation.ViewContextScanner;
import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.Action;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.interfaces.infrastructure.MessageConverter;
import spec.interfaces.infrastructure.MessageHandler;
import spec.validation.BeanValidationException;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class BeanValidationExceptionInterceptor {

    private final CurrentViewContext context;

    private final MessageConverter messageConverter;

    private final MessageHandler messageHandler;

    @Inject
    public BeanValidationExceptionInterceptor(CurrentViewContext context, MessageConverter messageConverter, MessageHandler messageHandler) {
        this.context = context;
        this.messageConverter = messageConverter;
        this.messageHandler = messageHandler;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        String currentViewId = context.currentViewId();

        try {
            return ic.proceed();
        } catch (BeanValidationException ex) {
            ConstraintViolationsHandler handler = new ConstraintViolationsHandler.Builder()
                    .messageSortkeyMap(ViewContextScanner.of(ic.getTarget().getClass().getSuperclass()).scan())
                    .constraintViolationSet(ex.getValidatedResults())
                    .build();
            List<String> messages = messageConverter.toMessages(handler.sortedConstraintViolations());
            messageHandler.appendErrorMessages(messages);
            return currentViewId;
        }

    }
}
