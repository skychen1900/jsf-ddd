/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.inject.Inject;
import spec.message.MessageConverter;
import spec.message.MessageWriter;

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
    MessageConverter messageConverter;

    @Inject
    MessageWriter messageHandler;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler(), messageConverter, messageHandler);
        return handler;
    }
}