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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Yamashita,Takahiro
 */
public class TargetClientIds {

    private final Set<String> targetClientIds;

    public TargetClientIds() {
        this.targetClientIds = new HashSet<>();
    }

    public void addAll(Set<String> targetClientIds) {
        this.targetClientIds.addAll(targetClientIds);
    }

    public String orNull(String targetClientId) {
        return this.targetClientIds.contains(targetClientId)
               ? targetClientId
               : null;
    }

    public ConstraintViolationForMessage updateTargetClientId(ConstraintViolationForMessage constraintViolationForMessage) {
        String clientId = clientId(constraintViolationForMessage);

        return new ConstraintViolationForMessage(constraintViolationForMessage.getSortKey(),
                                                 clientId,
                                                 constraintViolationForMessage.getConstraintViolation());

    }

    private String clientId(ConstraintViolationForMessage constraintViolationForMessage) {
        return constraintViolationForMessage.getClientId() != null
               ? constraintViolationForMessage.getClientId()
               : null;
    }
}
