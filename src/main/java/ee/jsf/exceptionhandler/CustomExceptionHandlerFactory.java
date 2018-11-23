/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import ee.interceptor.scope.conversation.ConversationLifecycleManager;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.FacesContext;
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
    MessageWriter messageWriter;

    @Inject
    ConversationLifecycleManager conversationLifecycleManager;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExceptionHandler handler = new CustomExceptionHandler(
                parent.getExceptionHandler(), messageConverter, messageWriter, facesContext, conversationLifecycleManager);
        return handler;
    }
}
