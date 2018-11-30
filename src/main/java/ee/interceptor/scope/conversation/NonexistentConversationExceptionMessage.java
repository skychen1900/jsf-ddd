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
package ee.interceptor.scope.conversation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 * ConversationScopedがタイムアウトした際の画面遷移を行う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
@SessionScoped
public class NonexistentConversationExceptionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private State state = State.NOT_HAVE_EXCEPTION;

    public static enum State {
        HAS_EXCEPTION,
        NOT_HAVE_EXCEPTION
    }

    public NonexistentConversationExceptionMessage() {
    }

    public void setException() {
        this.state = State.HAS_EXCEPTION;
    }

    public State state() {
        return this.state;
    }

    public String message() {
        this.state = State.NOT_HAVE_EXCEPTION;
        return "javax.enterprise.context.NonexistentConversationException";
    }

}
