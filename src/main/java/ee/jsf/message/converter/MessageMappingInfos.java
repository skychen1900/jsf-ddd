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
package ee.jsf.message.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import spec.message.validation.ClientIds;

/**
 * {@link MessageMappingInfo} の集約を扱う機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
class MessageMappingInfos {

    // keyは message
    private final Map<String, MessageMappingInfo> messageMappingInfos;

    MessageMappingInfos() {
        messageMappingInfos = new HashMap<>();
    }

    void put(String message, String sortKey, String targetClientId) {
        if (messageMappingInfos.containsKey(message) == false) {
            MessageMappingInfo messageMappingInfo = new MessageMappingInfo(message, sortKey, targetClientId);
            messageMappingInfos.put(message, messageMappingInfo);
            return;
        }

        MessageMappingInfo _messageMappingInfo = messageMappingInfos.get(message);
        String _sortKey = _messageMappingInfo.isUpdate(sortKey)
                          ? sortKey
                          : _messageMappingInfo.getSortKey();

        ClientIds _clientIds = _messageMappingInfo.getClientIds();
        _clientIds.put(targetClientId);

        MessageMappingInfo messageMappingInfo = new MessageMappingInfo(message, _sortKey, _clientIds);
        this.messageMappingInfos.put(message, messageMappingInfo);
    }

    void putAll(MessageMappingInfos messageMappingInfos) {
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

        ClientIds _clientIds = _messageMappingInfo.getClientIds();
        _clientIds.putAll(messageMappingInfo.getClientIds());

        this.messageMappingInfos.put(message, new MessageMappingInfo(message, _sortKey, _clientIds));
    }

    /**
     * 項目名であるＩＤからクライアントＩＤ（フルパス）に置き換えた、新たなインスタンスを返却します.
     * <p>
     * TODO：まだクライアントＩＤを複数保持した機能は実装していません。（繰り返し処理を扱っていないため）
     * {@link ClientIds} はクライアントＩＤを複数保持していますが、デフォルトとして先頭のクライアントＩＤで置き換えます.<br>
     *
     * @param clientIds 項目名とクライアントＩＤを置き換えるための情報
     * @return 項目名であるＩＤからクライアントＩＤ（フルパス）に置き換えた 新たなインスタンス
     */
    MessageMappingInfos replacedClientIds(ClientIds clientIds) {

        List<MessageMappingInfo> replaceItems = messageMappingInfos.entrySet().stream()
                .map(entry -> {
                    String message = entry.getKey();

                    MessageMappingInfo messageMappingInfo = entry.getValue();
                    ClientIds _clientIdsWithComponents = messageMappingInfo.getClientIds();
                    String replaceClientId = clientIds.findFirstByClientId(_clientIdsWithComponents).orElse(null);
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

    ConstraintViolationForMessage updateConstraintViolationForMessage(ConstraintViolationForMessage constraintViolationForMessage) {

        MessageMappingInfo messageMappingInfo = messageMappingInfos.get(
                constraintViolationForMessage.getConstraintViolation().getMessageTemplate());

        String _sortKey = (messageMappingInfo != null)
                          ? messageMappingInfo.getSortKey() : constraintViolationForMessage.getSortKey();

        String _id = (messageMappingInfo != null)
                     ? messageMappingInfo.firstClientId()
                     : constraintViolationForMessage.geClientId();

        return new ConstraintViolationForMessage(_sortKey,
                                                 _id,
                                                 constraintViolationForMessage.getConstraintViolation());
    }

}
