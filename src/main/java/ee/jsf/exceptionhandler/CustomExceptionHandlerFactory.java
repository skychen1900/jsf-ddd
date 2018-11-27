/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.inject.Inject;

/**
 * ExceptionHandlerFactory
 * <P>
 * ※faces-config.xmlにも登録
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final ExceptionHandlerFactory parent;

    @Inject
    private ThrowableHandlerFactory throwableHandlerFactory;

    @Inject
    private ErrorPageNavigator errorPageNavigator;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler(), throwableHandlerFactory, errorPageNavigator);
        return handler;
    }
}
