/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.infrastructure.jsf;

import ddd.domain.exception.UnexpectedApplicationException;
import ddd.domain.validation.BeanValidationException;
import ddd.domain.validation.MessageHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.vermeerlab.resourcebundle.CustomControl;

/**
 * ExceptionHandler
 * <P>
 * 参考：{@link http://n-agetsuma.hatenablog.com/entry/2013/02/11/134531}
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final MessageConverter messageConverter;
    private final MessageHandler messageHandler;

    CustomExceptionHandler(ExceptionHandler exception, MessageConverter messageConverter, MessageHandler messageHandler) {
        this.wrapped = exception;
        this.messageConverter = messageConverter;
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
                this.handleEntityNotExistException(th);

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

        List<String> messages = messageConverter.toMessages(ex.getValidatedResults());
        messageHandler.appendMessage(messages);

        FacesContext context = FacesContext.getCurrentInstance();

        String contextPath = context.getExternalContext().getRequestContextPath();
        String currentPage = context.getViewRoot().getViewId();
        context.getExternalContext().redirect(contextPath + currentPage);
    }

    //
    void handleEntityNotExistException(Throwable th) throws IOException {
        if (th instanceof UnexpectedApplicationException == false) {
            return;
        }

        UnexpectedApplicationException ex = (UnexpectedApplicationException) th;

        String message = ex.getMessage();

        FacesContext context = FacesContext.getCurrentInstance();

        Locale locale = context.getViewRoot().getLocale();

        ResourceBundle.Control control = CustomControl.builder().build();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", locale, control);

        String convertedMessage = message == null
                                  ? "System.Error"
                                  : resourceBundle.containsKey(message) == false
                                    ? message
                                    : resourceBundle.getString(message);

        FacesMessage facemsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, convertedMessage, null);
        context.addMessage(null, facemsg);
        // リダイレクトしてもFacesMessageが消えないように設定
        context.getExternalContext().getFlash().setKeepMessages(true);

        String contextPath = context.getExternalContext().getRequestContextPath();
        String currentPage = context.getViewRoot().getViewId();
        context.getExternalContext().redirect(contextPath + currentPage);
    }

}
