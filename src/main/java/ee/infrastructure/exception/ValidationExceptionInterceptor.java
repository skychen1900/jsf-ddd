/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.exception;

import ddd.domain.validation.BeanValidationException;
import ee.domain.annotation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ValidationException;

/**
 * BeanValidationを階層的に呼び出した際にValidationExceptionによりBeanValidationExceptionが包含されてしまう場合に、
 * 原因となったBeanValidationExceptionを取得して、再スローします
 *
 * @author Yamashita,Takahiro
 */
@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class ValidationExceptionInterceptor {

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        try {
            return ic.proceed();
        } catch (ValidationException ex) {
            BeanValidationException cause = recursiveCause(ex);
            if (cause == null) {
                throw ex;
            }
            throw cause;
        }
    }

    private BeanValidationException recursiveCause(Throwable th) {
        if (th == null) {
            return null;
        }

        if (th instanceof BeanValidationException) {
            return (BeanValidationException) th;
        }

        return this.recursiveCause(th.getCause());

    }

}
