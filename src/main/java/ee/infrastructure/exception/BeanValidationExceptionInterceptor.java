/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.exception;

import ddd.domain.validation.BeanValidationException;
import ddd.domain.validation.MessageHandler;
import ddd.presentation.url.UrlContext;
import ee.domain.annotation.controller.Action;
import ee.infrastructure.jsf.MessageConverter;
import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.sorting.ConstraintViolationSorting;

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
            List<ConstraintViolation<?>> constraintViolations = ConstraintViolationSorting.of(ex.getValidatedResults()).toList();
            List<String> messages = messageConverter.toMessages(constraintViolations);
            messageHandler.appendMessage(messages);
            return currentViewId;
        }

    }
}
