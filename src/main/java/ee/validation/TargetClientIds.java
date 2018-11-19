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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yamashita,Takahiro
 */
public class TargetClientIds {

    /**
     * key:Id, value clientIdのlist
     */
    private final Map<String, Set<String>> map;

    public TargetClientIds() {
        this.map = new HashMap<>();
    }

    public void put(String id) {
        this.put(id, id);
    }

    public void put(String id, String clientId) {
        Set<String> clientIds = map.getOrDefault(id, new HashSet<>());
        clientIds.add(clientId);
        this.map.put(id, clientIds);
    }

    public void putAll(TargetClientIds targetClientIds) {
        this.map.putAll(targetClientIds.map);
    }

    /**
     * xhtmlに指定されているＩＤに合致する、フルパスのクライアントＩＤを返却します.
     * <p>
     * TODO:まだ、繰り返し領域を考慮していません。
     *
     * @param id クライアントＩＤ（フルパスではない）
     * @return 指定のIDがxhtmlに指定されている場合はフルパスのクライアントＩＤ、存在しない場合は null を返却します.
     */
    public String orNull(String id) {
        Set<String> clientIds = this.map.getOrDefault(id, new HashSet<>());
        return clientIds.isEmpty()
               ? null
               : clientIds.iterator().next();
    }

    public ConstraintViolationForMessage updateTargetClientId(ConstraintViolationForMessage constraintViolationForMessage) {
        String clientId = clientId(constraintViolationForMessage);

        return new ConstraintViolationForMessage(constraintViolationForMessage.getSortKey(),
                                                 clientId,
                                                 constraintViolationForMessage.getConstraintViolation());

    }

    private String clientId(ConstraintViolationForMessage constraintViolationForMessage) {
        return constraintViolationForMessage.getId() != null
               ? constraintViolationForMessage.getId()
               : null;
    }
}
