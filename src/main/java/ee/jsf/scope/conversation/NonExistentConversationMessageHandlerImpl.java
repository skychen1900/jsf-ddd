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

import spec.scope.conversation.exception.NonExistentConversationMessageHandler;
import javax.enterprise.context.NonexistentConversationException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import spec.message.MessageConverter;
import spec.message.MessageWriter;
import spec.scope.conversation.exception.ConversationExceptionValue;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class NonExistentConversationMessageHandlerImpl implements NonExistentConversationMessageHandler {

    private MessageConverter messageConverter;
    private MessageWriter messageWriter;

    public NonExistentConversationMessageHandlerImpl() {
    }

    @Inject
    public NonExistentConversationMessageHandlerImpl(MessageConverter messageConverter, MessageWriter messageWriter) {
        this.messageConverter = messageConverter;
        this.messageWriter = messageWriter;
    }

    @Override
    public boolean isNonExistentConversation(String flashException) {
        return (flashException != null && flashException.equals(ConversationExceptionValue.NON_EXISTENT));
    }

    @Override
    public void write(String fromPath) {
        String message = messageConverter.toMessage(fromPath);
        if (message.equals(fromPath)) {
            message = messageConverter.toMessage(NonexistentConversationException.class.getName());
        }

        messageWriter.appendErrorMessage(message);
    }

}
