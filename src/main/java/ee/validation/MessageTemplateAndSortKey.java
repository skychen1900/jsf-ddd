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
 * MessageTemplateをKey、SortKeyをValueとしたMapを扱うクラスです
 *
 * @author Yamashita,Takahiro
 */
public class MessageTemplateAndSortKey {

    private final Map<String, String> map;

    public MessageTemplateAndSortKey() {
        this.map = new HashMap<>();
    }

    public void put(String message, String sortKey) {
        if (map.containsKey(message) == false) {
            map.put(message, sortKey);
        }

        if (map.get(message).compareTo(sortKey) == 1) {
            map.put(message, sortKey);
        }
    }

    public void putAll(MessageTemplateAndSortKey keyMap) {
        keyMap.map.entrySet().stream()
                .forEach(entry -> {
                    this.put(entry.getKey(), entry.getValue());
                });
    }

    public ConstraintViolationForMessage updateSortkey(ConstraintViolationForMessage constraintViolationForMessage) {
        String sortkey = map.getOrDefault(constraintViolationForMessage.getConstraintViolation().getMessageTemplate(),
                                          constraintViolationForMessage.getSortkey());
        return new ConstraintViolationForMessage(sortkey,
                                                 constraintViolationForMessage.getClientId(),
                                                 constraintViolationForMessage.getConstraintViolation());

    }
}
