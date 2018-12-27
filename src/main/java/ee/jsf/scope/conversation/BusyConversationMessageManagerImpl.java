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

import spec.scope.conversation.exception.BusyConversationMessageManager;
import base.annotation.presentation.controller.BusyConversationMessage;
import javax.annotation.PostConstruct;
import javax.enterprise.context.BusyConversationException;
import javax.enterprise.context.ConversationScoped;

/**
 *
 * @author Yamashita,Takahiro
 */
@ConversationScoped
public class BusyConversationMessageManagerImpl implements BusyConversationMessageManager {

    private static final long serialVersionUID = 1L;

    private String message;

    @PostConstruct
    void init() {
        this.message = BusyConversationException.class.getName();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(BusyConversationMessage classMessage, BusyConversationMessage methodMessage) {
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
