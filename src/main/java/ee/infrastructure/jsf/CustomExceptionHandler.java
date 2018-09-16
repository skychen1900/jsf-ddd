/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.jsf;

import ddd.domain.validation.BeanValidationException;
import ddd.domain.validation.MessageHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.validation.ConstraintViolation;

/**
 * ExceptionHandler
 * <P>
 * 参考：{@link http://n-agetsuma.hatenablog.com/entry/2013/02/11/134531}
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    private final MessageHandler messageHandler;

    CustomExceptionHandler(ExceptionHandler exception, MessageHandler messageHandler) {
        this.wrapped = exception;
        this.messageHandler = messageHandler;
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

            } catch (IOException ex) {
                System.err.println(Arrays.toString(ex.getStackTrace()));

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }
        }
        getWrapped().handle();
    }

    //
    void handleBeanValidationException(Throwable th) throws IOException {
        if (th instanceof BeanValidationException == false) {
            return;
        }

        BeanValidationException ex = (BeanValidationException) th;

        Set<ConstraintViolation<?>> results = ex.getValidatedResults();

        messageHandler.appendMessage(results);

        FacesContext context = FacesContext.getCurrentInstance();

        String contextPath = context.getExternalContext().getRequestContextPath();
        String currentPage = context.getViewRoot().getViewId();
        context.getExternalContext().redirect(contextPath + currentPage);
    }

}
