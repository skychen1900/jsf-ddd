/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.scope.conversation;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.Action;
import spec.presentation.CurrentViewContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
@Dependent
public class ConversationInterceptor {

    private final ConversationLifecycleManager conversationLifecycleManager;

    private final CurrentViewContext context;

    @Inject
    public ConversationInterceptor(ConversationLifecycleManager conversationLifecycleManager, CurrentViewContext context) {
        this.conversationLifecycleManager = conversationLifecycleManager;
        this.context = context;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        String currentViewId = context.currentViewId();
        Object resultViewId = ic.proceed();
        this.conversationLifecycleManager.endConversation(currentViewId, (String) resultViewId);
        return resultViewId;
    }
}
