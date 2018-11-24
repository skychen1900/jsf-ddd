/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import java.util.Arrays;
import java.util.Iterator;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * ExceptionHandler
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final ThrowableHandlerFactory throwableHandlerFactory;

    CustomExceptionHandler(ExceptionHandler exception, ThrowableHandlerFactory throwableHandlerFactory) {
        this.wrapped = exception;
        this.throwableHandlerFactory = throwableHandlerFactory;
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

            try {
                this.throwableHandlerFactory.createThrowableHandler(throwable).execute();

            } catch (Exception ex) {
                System.err.println(Arrays.toString(ex.getStackTrace()));

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }

            getWrapped().handle();
        }

    }
}
