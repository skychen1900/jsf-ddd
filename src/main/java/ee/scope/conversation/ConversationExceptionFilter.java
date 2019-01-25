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
package ee.scope.conversation;

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
import spec.scope.conversation.exception.ConversationExceptionKey;
import spec.scope.conversation.exception.ConversationExceptionValue;

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
            request.setAttribute(ConversationExceptionKey.FORWARD_PAGE, indexPage);

            request.setAttribute(ConversationExceptionKey.EXCEPTION, ConversationExceptionValue.NON_EXISTENT);

            request.setAttribute(ConversationExceptionKey.FROM_PATH, indexRootPath);

            httpServletRequest.getRequestDispatcher("/parts/conversation/nonexist-conversation.jsp").forward(request, response);

        } catch (BusyConversationException ex) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            String servletPath = httpServletRequest.getServletPath();
            request.setAttribute(ConversationExceptionKey.FORWARD_PAGE, servletPath);

            String cid = httpServletRequest.getParameter("cid");
            request.setAttribute(ConversationExceptionKey.CONVERSATION_ID, cid);

            request.setAttribute(ConversationExceptionKey.EXCEPTION, ConversationExceptionValue.BUSY);

            httpServletRequest.getRequestDispatcher("/parts/conversation/busy-conversation.jsp").forward(request, response);

        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void destroy() {
    }

}
