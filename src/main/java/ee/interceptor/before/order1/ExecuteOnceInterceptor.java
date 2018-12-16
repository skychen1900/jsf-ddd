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
package ee.interceptor.before.order1;

import ee.jsf.scope.conversation.DoubleSubmitLifecycle;
import java.util.Objects;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.ExecuteOnce;
import spec.interfaces.infrastructure.CurrentViewContext;
import spec.message.MessageWriter;

@ExecuteOnce
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class ExecuteOnceInterceptor {

    private final DoubleSubmitLifecycle doubleSubmitLifecycle;
    private final CurrentViewContext context;
    private final MessageWriter messageHandler;

    @Inject
    public ExecuteOnceInterceptor(DoubleSubmitLifecycle doubleSubmitLifecycle, CurrentViewContext context, MessageWriter messageHandler) {
        this.doubleSubmitLifecycle = doubleSubmitLifecycle;
        this.context = context;
        this.messageHandler = messageHandler;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        if (this.doubleSubmitLifecycle.isSubmitted()) {
            ExecuteOnce annotation = ic.getMethod().getAnnotation(ExecuteOnce.class);
            return this.toErrorPage(annotation);
        }
        Object result = ic.proceed();
        this.doubleSubmitLifecycle.nextState();
        return result;

    }

    //
    Object toErrorPage(ExecuteOnce annotation) {
        String message = annotation.message();
        if (Objects.equals(message, "") == false) {
            messageHandler.appendErrorMessage(message);
        }

        // エラーページを指定していない場合は自画面へ遷移
        String result = Objects.equals(annotation.forwardPage(), "")
                        ? context.currentViewId()
                        : annotation.forwardPage();

        return context.responseViewId(result);
    }

}
