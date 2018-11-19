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
package ee.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link MessageMappingInfo} の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
public class MessageMappingInfos {

    // keyは message
    private final Map<String, MessageMappingInfo> messageMappingInfos;

    public MessageMappingInfos() {
        messageMappingInfos = new HashMap<>();
    }

    public void put(String message, String sortKey, String targetClientId) {
        if (messageMappingInfos.containsKey(message) == false) {
            MessageMappingInfo messageMappingInfo = new MessageMappingInfo(message, sortKey, targetClientId);
            messageMappingInfos.put(message, messageMappingInfo);
            return;
        }

        MessageMappingInfo _messageMappingInfo = messageMappingInfos.get(message);
        String _sortKey = _messageMappingInfo.isUpdate(sortKey)
                          ? sortKey
                          : _messageMappingInfo.getSortKey();

        TargetClientIds _ids = _messageMappingInfo.getTargetClientIds();
        _ids.put(targetClientId);

        MessageMappingInfo messageMappingInfo = new MessageMappingInfo(message, _sortKey, _ids);
        this.messageMappingInfos.put(message, messageMappingInfo);
    }

    public void put(MessageMappingInfo messageMappingInfo) {
        String message = messageMappingInfo.getMessage();
        if (messageMappingInfos.containsKey(message) == false) {
            messageMappingInfos.put(message, messageMappingInfo);
        }

        if (messageMappingInfos.get(message).isUpdate(messageMappingInfo.getSortKey())) {
            messageMappingInfos.put(message, messageMappingInfo);
        }
    }

    public void putAll(MessageMappingInfos messageMappingInfos) {
        messageMappingInfos.messageMappingInfos.entrySet().stream()
                .forEach(entry -> {
                    this.put(entry.getValue());
                });
    }

    public void addAll(MessageMappingInfos messageMappingInfos) {

    }

    public ConstraintViolationForMessage updateSortkey(ConstraintViolationForMessage constraintViolationForMessage) {

        MessageMappingInfo messageMappingInfo = messageMappingInfos.getOrDefault(
                constraintViolationForMessage.getConstraintViolation().getMessageTemplate(),
                MessageMappingInfo.createDummyBySortKey(constraintViolationForMessage.getSortKey()));

        return new ConstraintViolationForMessage(messageMappingInfo.getSortKey(),
                                                 constraintViolationForMessage.getId(),
                                                 constraintViolationForMessage.getConstraintViolation());
    }
}
