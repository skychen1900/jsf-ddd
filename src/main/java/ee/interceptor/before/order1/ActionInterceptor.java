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

import base.annotation.presentation.controller.Action;
import ee.logger.InvocationContextLogger;
import ee.logger.LoggerStore;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class ActionInterceptor {

    @Inject
    private LoggerStore loggerStore;

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {
        Action action = ic.getMethod().getAnnotation(Action.class);

        loggerStore.setUp(ic);

        if (action != null && action.value().equals(Action.Ignore.ON)) {
            return null;
        }

        InvocationContextLogger logger = InvocationContextLogger.getLogger(ic);
        try {
            logger.fine("start");
            return ic.proceed();
        } finally {
            logger.fine("end");
        }

    }
}
