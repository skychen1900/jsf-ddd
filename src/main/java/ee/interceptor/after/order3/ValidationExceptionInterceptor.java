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
package ee.interceptor.after.order3;

import base.annotation.presentation.controller.Action;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ValidationException;
import spec.validation.BeanValidationException;

/**
 * BeanValidationを階層的に呼び出した際にValidationExceptionによりBeanValidationExceptionが包含されてしまう場合に、
 * 原因となったBeanValidationExceptionを取得して、再スローします
 *
 * @author Yamashita,Takahiro
 */
@Action
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class ValidationExceptionInterceptor {

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        try {
            return ic.proceed();
        } catch (ValidationException ex) {
            BeanValidationException cause = recursiveCause(ex);
            if (cause == null) {
                throw ex;
            }
            throw cause;
        }
    }

    private BeanValidationException recursiveCause(Throwable th) {
        if (th == null) {
            return null;
        }

        if (th instanceof BeanValidationException) {
            return (BeanValidationException) th;
        }

        return this.recursiveCause(th.getCause());

    }

}
