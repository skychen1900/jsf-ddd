/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ddd.infrastructure.exceptionhandler.jsf;

import ddd.domain.validation.MessageHandler;
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

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Inject
    MessageHandler messageHandler;

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler(), messageHandler);
        return handler;
    }
}
