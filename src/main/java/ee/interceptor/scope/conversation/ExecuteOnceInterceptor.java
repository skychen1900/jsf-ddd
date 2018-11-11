/*
 * Copyright(C) 2013 Sanyu Academy All rights reserved.
 *
 */
package ee.interceptor.scope.conversation;

import java.util.Objects;
import javax.annotation.Priority;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import spec.annotation.presentation.controller.ExecuteOnce;
import spec.presentation.CurrentViewContext;

@ExecuteOnce
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class ExecuteOnceInterceptor {

    private DoubleSubmitLifecycle doubleSubmitLifecycle;

    private CurrentViewContext context;

    public ExecuteOnceInterceptor() {
    }

    @Inject
    public ExecuteOnceInterceptor(DoubleSubmitLifecycle doubleSubmitLifecycle, CurrentViewContext context) {
        this.doubleSubmitLifecycle = doubleSubmitLifecycle;
        this.context = context;
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
            //<h:messages /> にメッセージを出力
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));

            //リダイレクトしたときにメッセージがクリアされないようにする
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        }

        // エラーページを指定していない場合は自画面へ遷移
        String result = Objects.equals(annotation.forwardPage(), "")
                        ? context.currentViewId()
                        : annotation.forwardPage();

        return context.responseViewId(result);
    }

}
