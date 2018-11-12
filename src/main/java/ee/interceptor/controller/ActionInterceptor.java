/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.controller;

import spec.annotation.presentation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
@Dependent
public class ActionInterceptor {

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        Action action = ic.getMethod().getAnnotation(Action.class);
        if (action == null) {
            return ic.proceed();
        }

        if (action.value().equals(Action.Ignore.OFF)) {
            return ic.proceed();

        }

        return null;
    }
}
