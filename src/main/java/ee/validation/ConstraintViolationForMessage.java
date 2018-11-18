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

import javax.validation.ConstraintViolation;

/**
 * SortKeyとConstraintViolationのペアを扱うクラスです.
 *
 * @author Yamashita,Takahiro
 */
public class ConstraintViolationForMessage {

    private final String sortKey;
    private final String clientId;
    private final ConstraintViolation<?> constraintViolation;

    public ConstraintViolationForMessage(String sortkey, String clientId, ConstraintViolation<?> constraintViolation) {
        this.sortKey = sortkey;
        this.clientId = clientId;
        this.constraintViolation = constraintViolation;
    }

    public String getSortKey() {
        return sortKey;
    }

    public String getClientId() {
        return clientId;
    }

    public ConstraintViolation<?> getConstraintViolation() {
        return constraintViolation;
    }
}
