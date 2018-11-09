/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.exception;

import core.domain.validation.BeanValidationException;
import core.domain.validation.MessageHandler;
import core.presentation.url.UrlContext;
import ee.domain.annotation.controller.Action;
import ee.infrastructure.jsf.MessageConverter;
import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.vermeerlab.beanvalidation.sorting.ConstraintViolationsHandler;
import org.vermeerlab.beanvalidation.sorting.ViewContextScanner;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class BeanValidationExceptionInterceptor {

    @Inject
    UrlContext urlContext;

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
