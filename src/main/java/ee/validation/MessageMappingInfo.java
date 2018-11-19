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

/**
 * {@link spec.annotation.presentation.view.InvalidMessageMapping} でマークしたフィールドの情報を扱う機能を提供します.
 * <p>
 * {@code sortKey} が１つだけとしている理由は、同一メッセージに対して上位順位のもののみを保持するようにしているためです.<br>
 * {@code targetClientId} を複数保持している理由は、同一メッセージに対して複数のクライアントＩＤが割り当てられることを想定しているためです。
 *
 * @author Yamashita,Takahiro
 */
public class MessageMappingInfo {

    private final String message;
    private final String sortKey;
    private final TargetClientIds targetClientIds;

    public MessageMappingInfo(String message, String sortKey, String targetClientId) {
        this.message = message;
        this.sortKey = sortKey;

        TargetClientIds _targetClientIds = new TargetClientIds();
        _targetClientIds.put(targetClientId);
        this.targetClientIds = _targetClientIds;
    }

    public MessageMappingInfo(String message, String sortKey, TargetClientIds targetClientIds) {
        this.message = message;
        this.sortKey = sortKey;
        this.targetClientIds = targetClientIds;
    }

    public static MessageMappingInfo createDummyBySortKey(String sortKey) {
        return new MessageMappingInfo("", sortKey, "");
    }

    public boolean isUpdate(String sortKey) {
        return this.sortKey.compareTo(sortKey) == 1;
    }

    public String getMessage() {
        return message;
    }

    public String getSortKey() {
        return sortKey;
    }

    public TargetClientIds getTargetClientIds() {
        return targetClientIds;
    }

}
