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
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.EndConversation;

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
