/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ddd.infrastructure.exceptionhandler.jsf;

import ddd.domain.validation.BeanValidationException;
import java.util.Iterator;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * ExceptionHandler
 * <P>
 * 参考：{@link http://n-agetsuma.hatenablog.com/entry/2013/02/11/134531}
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() {

        final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();

        while (it.hasNext()) {

            ExceptionQueuedEventContext eventContext = it.next().getContext();

            try {
                // ハンドリング対象のアプリケーション例外を取得
                Throwable th = getRootCause(eventContext.getException()).getCause();

                // 任意の例外毎に処理を行う
                this.handleBeanValidationException(th);

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }
        }
        getWrapped().handle();
    }

    void handleBeanValidationException(Throwable th) {
        if (th instanceof BeanValidationException == false) {
            return;
        }

        BeanValidationException ex = (BeanValidationException) th;

    }

}
