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
 *  Copyright © 2018 Yamashita,Takahiro
 */
package ee.filter;

import java.io.IOException;
import javax.enterprise.context.BusyConversationException;
import javax.enterprise.context.NonexistentConversationException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import spec.scope.conversation.ConversationExceptionKey;

/**
 * Conversationに関する例外を扱うフィルターです.
 *
 * @author Yamashita,Takahiro
 */
public class ConversationExceptionFilter implements Filter {

    /**
     * {@inheritDoc }
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (NonexistentConversationException ex) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String servletPath = httpServletRequest.getServletPath();
            String indexRootPath = servletPath.substring(0, servletPath.lastIndexOf("/") + 1);
            String indexPage = indexRootPath + "index.xhtml";

            String exception = NonexistentConversationException.class.getCanonicalName();

            request.setAttribute(ConversationExceptionKey.FORWARD_PAGE, indexPage);
            request.setAttribute(ConversationExceptionKey.EXCEPTION, exception);
            httpServletRequest.getRequestDispatcher("/parts/conversation/non-conversation.jsp").forward(request, response);

        } catch (BusyConversationException ex) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String servletPath = httpServletRequest.getServletPath();

            String exception = BusyConversationException.class.getCanonicalName();
            String cid = httpServletRequest.getParameter("cid");

            request.setAttribute(ConversationExceptionKey.FORWARD_PAGE, servletPath);
            request.setAttribute(ConversationExceptionKey.EXCEPTION, exception);
            request.setAttribute(ConversationExceptionKey.CONVERSATION_ID, cid == null ? "0" : cid);
            httpServletRequest.getRequestDispatcher("/parts/conversation/conversation.jsp").forward(request, response);

        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void destroy() {
    }

}
