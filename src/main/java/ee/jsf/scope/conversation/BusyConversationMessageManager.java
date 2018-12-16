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
package ee.jsf.scope.conversation;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.BusyConversationException;
import javax.enterprise.context.ConversationScoped;
import spec.annotation.presentation.controller.BusyConversationMessage;

/**
 *
 * @author Yamashita,Takahiro
 */
@ConversationScoped
public class BusyConversationMessageManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private String defaultMessage;

    @PostConstruct
    void init() {
        defaultMessage = BusyConversationException.class.getName();
        this.message = defaultMessage;
    }

    public String getMessage() {
        return message;
    }

    public void message(BusyConversationMessage classMessage, BusyConversationMessage methodMessage) {
        String _message = methodMessage != null
                          ? methodMessage.value()
                          : classMessage != null
                            ? classMessage.value()
                            : "";

        if (_message.equals("")) {
            return;
        }
        this.message = _message;
    }
}
