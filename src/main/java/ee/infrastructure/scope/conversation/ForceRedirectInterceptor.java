/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.scope.conversation;

import core.presentation.url.UrlContext;
import ee.domain.annotation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 100)
@Dependent
public class ForceRedirectInterceptor {

    @Inject
    UrlContext urlContext;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        String currentViewId = urlContext.currentViewId();

        Object resultViewId = ic.proceed();

        if (resultViewId == null) {
            resultViewId = currentViewId;
        }
        return urlContext.responseViewId(resultViewId);
    }
}
