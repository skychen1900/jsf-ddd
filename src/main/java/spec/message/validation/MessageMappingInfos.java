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
package spec.message.validation;

import spec.message.validation.ConstraintViolationForMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void putAll(MessageMappingInfos messageMappingInfos) {
        messageMappingInfos.messageMappingInfos.entrySet().stream()
                .forEach(entry -> {
                    this.put(entry.getValue());
                });
    }

    private void put(MessageMappingInfo messageMappingInfo) {
        String message = messageMappingInfo.getMessage();
        if (messageMappingInfos.containsKey(message) == false) {
            messageMappingInfos.put(message, messageMappingInfo);
            return;
        }

        MessageMappingInfo _messageMappingInfo = messageMappingInfos.get(message);
        String _paramSortKey = messageMappingInfo.getSortKey();
        String _sortKey = _messageMappingInfo.isUpdate(_paramSortKey)
                          ? _paramSortKey
                          : _messageMappingInfo.getSortKey();

        TargetClientIds _ids = _messageMappingInfo.getTargetClientIds();
        _ids.putAll(messageMappingInfo.getTargetClientIds());

        this.messageMappingInfos.put(message, new MessageMappingInfo(message, _sortKey, _ids));
    }

    /**
     * 項目名であるＩＤからクライアントＩＤ（フルパス）に置き換えた、新たなインスタンスを返却します.
     * <p>
     * TODO：まだクライアントＩＤを複数保持した機能は実装していません。（繰り返し処理を扱っていないため）
     * {@link TargetClientIds} はクライアントＩＤを複数保持していますが、デフォルトとして先頭のクライアントＩＤで置き換えます.<br>
     *
     * @param targetClientIds 項目名とクライアントＩＤを置き換えるための情報
     * @return 項目名であるＩＤからクライアントＩＤ（フルパス）に置き換えた 新たなインスタンス
     */
    public MessageMappingInfos replacedClientIds(TargetClientIds targetClientIds) {

        List<MessageMappingInfo> replaceItems = messageMappingInfos.entrySet().stream()
                .map(entry -> {
                    String message = entry.getKey();

                    MessageMappingInfo messageMappingInfo = entry.getValue();
                    TargetClientIds clientIds = messageMappingInfo.getTargetClientIds();
                    String replaceClientId = targetClientIds.getClientIdOrNull(clientIds);
                    MessageMappingInfo replacedMessageMappingInfo = new MessageMappingInfo(message,
                                                                                           messageMappingInfo.getSortKey(),
                                                                                           replaceClientId);
                    return replacedMessageMappingInfo;
                })
                .collect(Collectors.toList());

        MessageMappingInfos replacedMessageMappingInfos = new MessageMappingInfos();
        for (MessageMappingInfo replaceItem : replaceItems) {
            replacedMessageMappingInfos.put(replaceItem);
        }

        return replacedMessageMappingInfos;
    }

    public ConstraintViolationForMessage updateConstraintViolationForMessage(ConstraintViolationForMessage constraintViolationForMessage) {

        MessageMappingInfo messageMappingInfo = messageMappingInfos.get(
                constraintViolationForMessage.getConstraintViolation().getMessageTemplate());

        String _sortKey = (messageMappingInfo != null)
                          ? messageMappingInfo.getSortKey() : constraintViolationForMessage.getSortKey();

        String _id = (messageMappingInfo != null)
                     ? messageMappingInfo.firstClientId()
                     : constraintViolationForMessage.getId();

        return new ConstraintViolationForMessage(_sortKey,
                                                 _id,
                                                 constraintViolationForMessage.getConstraintViolation());
    }

}
