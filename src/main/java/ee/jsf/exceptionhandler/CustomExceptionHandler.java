/*
 * Copyright(C) 2016 Sanyu Academy All rights reserved.
 *
 */
package ee.jsf.exceptionhandler;

import ee.interceptor.scope.conversation.ConversationLifecycleManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.vermeerlab.resourcebundle.CustomControl;
import spec.exception.UnexpectedApplicationException;
import spec.message.CanNotMappingHtmlMessagesException;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.validation.BeanValidationException;

/**
 * ExceptionHandler
 *
 * @author takahiro.yamashita
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;
    private final MessageConverter messageConverter;
    private final MessageWriter messageWriter;
    private final FacesContext facesContext;
    private final ConversationLifecycleManager conversationLifecycleManager;

    CustomExceptionHandler(ExceptionHandler exception, MessageConverter messageConverter, MessageWriter messageWriter,
                           FacesContext facesContext, ConversationLifecycleManager conversationLifecycleManager) {
        this.wrapped = exception;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.facesContext = facesContext;
        this.conversationLifecycleManager = conversationLifecycleManager;
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
                // 任意の例外毎に処理を行う
                this.handleNoExistsHtmlMessagesException(throwable);
                this.handleBeanValidationException(throwable);
                this.handleEntityNotExistException(throwable);

            } catch (IOException ex) {
                System.err.println(Arrays.toString(ex.getStackTrace()));

            } finally {
                // 未ハンドリングキューから削除する
                it.remove();
            }

            getWrapped().handle();
        }

    }

    //
    void handleNoExistsHtmlMessagesException(Throwable throwable) throws IOException {
        if (throwable instanceof CanNotMappingHtmlMessagesException == false) {
            return;
        }

        System.err.println(
                "h:messages is not exist page, message '" + throwable.getMessage() + "' can not mapping page."
        );

        this.conversationLifecycleManager.endConversation();
        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
        String forwardPage = "/error.xhtml?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

    //
    void handleBeanValidationException(Throwable th) throws IOException {
        if (th instanceof BeanValidationException == false) {
            return;
        }

        BeanValidationException ex = (BeanValidationException) th;

        System.err.println(
                "Target class must dependency by @Controller or @Action for BeanValidationException."
        );

        List<String> messages = messageConverter.toMessages(ex.getValidatedResults());
        messageWriter.appendErrorMessages(messages);

        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();

        String contextPath = facesContext.getExternalContext().getRequestContextPath();
        String currentPage = facesContext.getViewRoot().getViewId();

        String forwardPage = contextPath + currentPage + "?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

    //
    void handleEntityNotExistException(Throwable throwable) throws IOException {
        if (throwable instanceof UnexpectedApplicationException == false) {
            return;
        }

        UnexpectedApplicationException ex = (UnexpectedApplicationException) throwable;

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
