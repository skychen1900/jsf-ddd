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

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import base.annotation.presentation.controller.Action;

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
