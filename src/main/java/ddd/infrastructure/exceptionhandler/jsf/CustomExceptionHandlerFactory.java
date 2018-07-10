/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ddd.infrastructure.exceptionhandler.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * カスタム例外HandlerFactory<br>
 * ※faces-config.xmlにも登録
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final ExceptionHandlerFactory parent;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
	this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
	ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());
	return handler;
    }
}
