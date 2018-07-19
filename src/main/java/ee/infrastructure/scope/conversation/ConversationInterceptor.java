/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.scope.conversation;

import ee.domain.annotation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Dependent
public class ConversationInterceptor {

    @Inject
    ConversationLifecycleManager conversationLifecycleManager;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        String currentViewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        Object resultViewId = ic.proceed();
        this.conversationLifecycleManager.endConversation(currentViewId, (String) resultViewId);
        return resultViewId;
    }
}
