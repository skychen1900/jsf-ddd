/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.scope.conversation;

import spec.presentation.url.UrlContext;
import spec.annotation.presentation.controller.Action;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
@Dependent
public class ConversationInterceptor {

    private final ConversationLifecycleManager conversationLifecycleManager;

    private final UrlContext urlContext;

    @Inject
    public ConversationInterceptor(ConversationLifecycleManager conversationLifecycleManager, UrlContext urlContext) {
        this.conversationLifecycleManager = conversationLifecycleManager;
        this.urlContext = urlContext;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        String currentViewId = urlContext.currentViewId();
        Object resultViewId = ic.proceed();
        this.conversationLifecycleManager.endConversation(currentViewId, (String) resultViewId);
        return resultViewId;
    }
}
