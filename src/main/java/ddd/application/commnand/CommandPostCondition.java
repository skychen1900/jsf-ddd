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
package ddd.application.commnand;

import ddd.domain.validation.BeanValidationException;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface CommandPostCondition<T> {

    public void validatePostCondition(T entity);

    public default boolean isValidPostCondition(T entity) {
        try {
            this.validatePostCondition(entity);
            return true;
        } catch (BeanValidationException ex) {
            return false;
        }
    }
}
