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

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.scope.conversation.ConversationExceptionValue;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class BusyConversationMessageHandler {

    private BusyConversationMessageManager busyConversationMessageManager;

    private MessageConverter messageConverter;
    private MessageWriter messageWriter;
    private Conversation conversation;

    public BusyConversationMessageHandler() {
    }

    @Inject
    public BusyConversationMessageHandler(BusyConversationMessageManager busyConversationMessageManager,
                                          MessageConverter messageConverter, MessageWriter messageWriter, Conversation conversation) {
        this.busyConversationMessageManager = busyConversationMessageManager;
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
        this.conversation = conversation;
    }

    public boolean isBusyConversationException(String flashException, String requestParameterException) {
        return (flashException == null || flashException.equals(""))
               && (requestParameterException != null && requestParameterException.equals(ConversationExceptionValue.BUSY))
               && this.conversation.isTransient() == false;
    }

    public void write() {
        String message = messageConverter.toMessage(this.busyConversationMessageManager.getMessage());
        messageWriter.appendErrorMessage(message);
    }

}
