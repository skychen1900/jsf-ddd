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
import java.util.Arrays;
import javax.enterprise.context.BusyConversationException;
import javax.enterprise.context.NonexistentConversationException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
        } catch (BusyConversationException | NonexistentConversationException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void destroy() {
    }

}
