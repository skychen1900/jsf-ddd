/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.validation;

import spec.validation.BeanValidationException;
import spec.presentation.MessageHandler;
import spec.annotation.presentation.controller.Action;
import ee.jsf.messages.MessageConverter;
import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import ee.validation.ConstraintViolationsHandler;
import ee.validation.ViewContextScanner;
import spec.presentation.CurrentViewContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class BeanValidationExceptionInterceptor {

    @Inject
    CurrentViewContext urlContext;

    @Inject
    MessageConverter messageConverter;

    @Inject
    MessageHandler messageHandler;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        String currentViewId = urlContext.currentViewId();

        try {
            return ic.proceed();
        } catch (BeanValidationException ex) {
            ConstraintViolationsHandler handler = new ConstraintViolationsHandler.Builder()
                    .messageSortkeyMap(ViewContextScanner.of(ic.getTarget().getClass().getSuperclass()).scan())
                    .constraintViolationSet(ex.getValidatedResults())
                    .build();
            List<String> messages = messageConverter.toMessages(handler.sortedConstraintViolations());
            messageHandler.appendMessage(messages);
            return currentViewId;
        }

    }
}
