/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.scope.conversation;

import core.annotation.presentation.controller.EndConversation;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@EndConversation
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class EndConversationInterceptor {

    private final ConversationLifecycleManager conversationLifecycleManager;

    @Inject
    public EndConversationInterceptor(ConversationLifecycleManager conversationLifecycleManager) {
        this.conversationLifecycleManager = conversationLifecycleManager;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        Object result = ic.proceed();
        this.conversationLifecycleManager.endConversation();
        return result;
    }
}
