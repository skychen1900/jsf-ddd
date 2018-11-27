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

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import spec.exception.ThrowableHandler;
import spec.message.MessageConverter;
import spec.message.MessageWriter;

/**
 * UnexpectedApplicationException の捕捉後の処理を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class UnexpectedApplicationExceptionHandler implements ThrowableHandler {

    private final Throwable throwable;
    private final MessageConverter messageConverter;
    private final MessageWriter messageWriter;
    private final FacesContext facesContext;

    public UnexpectedApplicationExceptionHandler(Throwable throwable, MessageConverter messageConverter, MessageWriter messageWriter, FacesContext facesContext) {
        this.throwable = throwable;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.facesContext = facesContext;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void execute() {
        String message = messageConverter.toMessage(throwable.getMessage());
        System.err.println(message);
        messageWriter.appendErrorMessage(message);
        NavigationHandler navigationHandler = this.facesContext.getApplication().getNavigationHandler();
        String contextPath = facesContext.getExternalContext().getRequestContextPath();
        String currentPage = facesContext.getViewRoot().getViewId();
        String forwardPage = contextPath + currentPage + "?faces-redirect=true";
        navigationHandler.handleNavigation(facesContext, null, forwardPage);
        this.facesContext.renderResponse();
    }

}
