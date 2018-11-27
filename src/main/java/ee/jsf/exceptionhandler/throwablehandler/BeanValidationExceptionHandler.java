/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Copyright ? 2018 Yamashita,Takahiro
 */
package ee.jsf.exceptionhandler.throwablehandler;

import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import spec.exception.ThrowableHandler;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.validation.BeanValidationException;

/**
 * BeanValidationException の捕捉後の処理を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class BeanValidationExceptionHandler implements ThrowableHandler {

    private final BeanValidationException beanValidationException;
    private final MessageConverter messageConverter;
    private final MessageWriter messageWriter;
    private final FacesContext facesContext;

    public BeanValidationExceptionHandler(BeanValidationException beanValidationException, MessageConverter messageConverter, MessageWriter messageWriter, FacesContext facesContext) {
        this.beanValidationException = beanValidationException;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.facesContext = facesContext;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void execute() {
        System.err.println("Target class must dependency by @Controller or @Action for BeanValidationException.");
        List<String> messages = messageConverter.toMessages(beanValidationException.getValidatedResults());
        messageWriter.appendErrorMessages(messages);
        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
        String contextPath = facesContext.getExternalContext().getRequestContextPath();
        String currentPage = facesContext.getViewRoot().getViewId();
        String forwardPage = contextPath + currentPage + "?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

}
