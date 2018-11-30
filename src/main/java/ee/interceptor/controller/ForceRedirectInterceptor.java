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
package ee.interceptor.controller;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.Action;
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
