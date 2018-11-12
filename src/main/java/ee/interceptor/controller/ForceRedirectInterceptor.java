/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.controller;

import spec.annotation.presentation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.interfaces.infrastructure.CurrentViewContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 100)
@Dependent
public class ForceRedirectInterceptor {

    @Inject
    CurrentViewContext context;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        String currentViewId = context.currentViewId();

        Object resultViewId = ic.proceed();

        if (resultViewId == null) {
            resultViewId = currentViewId;
        }
        return context.responseViewId(resultViewId);
    }
}
