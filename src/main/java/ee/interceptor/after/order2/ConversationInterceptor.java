/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Copyright © 2018 Yamashita,Takahiro
 */
package ee.interceptor.after.order2;

import ee.jsf.scope.conversation.ConversationLifecycleManager;
import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import base.annotation.presentation.controller.Action;
import spec.interfaces.infrastructure.CurrentViewContext;

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
