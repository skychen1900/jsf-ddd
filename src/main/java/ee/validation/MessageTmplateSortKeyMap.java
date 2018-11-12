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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Yamashita,Takahiro
 */
public class MessageTmplateSortKeyMap {

    private final Map<String, String> map;

    public MessageTmplateSortKeyMap() {
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

    public Map<String, String> getEntry() {
        return Collections.unmodifiableMap(map);
    }

    public void putAll(MessageTmplateSortKeyMap keyMap) {
        keyMap.getEntry().entrySet().stream()
                .forEach(entry -> {
                    this.put(entry.getKey(), entry.getValue());
                });
    }

    public List<SortKeyConstraintViolation> replaceSortKey(List<SortKeyConstraintViolation> sortkeyConstraintViolations) {
        return sortkeyConstraintViolations.stream()
                .map(e -> {
                    String sortKey = map.getOrDefault(e.getConstraintViolation().getMessageTemplate(), e.getSortkey());
                    return new SortKeyConstraintViolation(sortKey, e.getConstraintViolation());
                })
                .collect(Collectors.toList());
    }

}
