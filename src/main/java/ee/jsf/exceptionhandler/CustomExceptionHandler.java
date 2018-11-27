/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import java.util.Iterator;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import spec.exception.ThrowableHandler;

/**
 * ExceptionHandler
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final ThrowableHandlerFactory throwableHandlerFactory;
    private final ErrorPageNavigator errorPageNavigator;

    CustomExceptionHandler(ExceptionHandler exception, ThrowableHandlerFactory throwableHandlerFactory, ErrorPageNavigator errorPageNavigator) {
        this.wrapped = exception;
        this.throwableHandlerFactory = throwableHandlerFactory;
        this.errorPageNavigator = errorPageNavigator;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() {

        final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();

        while (it.hasNext()) {

            ExceptionQueuedEventContext eventContext = (ExceptionQueuedEventContext) it.next().getSource();
            Throwable throwable = getRootCause(eventContext.getException()).getCause();

            ThrowableHandler throwableHandler = this.throwableHandlerFactory.createThrowableHandler(throwable, eventContext);

            try {
                throwableHandler.execute();

            } catch (Exception ex) {
                this.errorPageNavigator.navigate(ex);

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }

            getWrapped().handle();
        }

    }
}
