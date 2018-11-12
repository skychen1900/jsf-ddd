/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.scope.conversation;

import java.util.Objects;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.ExecuteOnce;
import spec.presentation.CurrentViewContext;
import spec.presentation.MessageHandler;

@ExecuteOnce
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class ExecuteOnceInterceptor {

    private final DoubleSubmitLifecycle doubleSubmitLifecycle;
    private final CurrentViewContext context;
    private final MessageHandler messageHandler;

    @Inject
    public ExecuteOnceInterceptor(DoubleSubmitLifecycle doubleSubmitLifecycle, CurrentViewContext context, MessageHandler messageHandler) {
        this.doubleSubmitLifecycle = doubleSubmitLifecycle;
        this.context = context;
        this.messageHandler = messageHandler;
    }

    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Exception {

        try {
            if (this.doubleSubmitLifecycle.isSubmitted()) {
                ExecuteOnce annotation = ic.getMethod().getAnnotation(ExecuteOnce.class);
                return this.toErrorPage(annotation);
            }
            Object result = ic.proceed();
            return result;
        } finally {
            this.doubleSubmitLifecycle.nextState();
        }

    }

    //
    Object toErrorPage(ExecuteOnce annotation) {
        String message = annotation.message();
        if (Objects.equals(message, "") == false) {
            messageHandler.appendMessage(message);
        }

        // エラーページを指定していない場合は自画面へ遷移
        String result = Objects.equals(annotation.forwardPage(), "")
                        ? context.currentViewId()
                        : annotation.forwardPage();

        return context.responseViewId(result);
    }

}
