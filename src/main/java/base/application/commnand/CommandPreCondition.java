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
package base.application.commnand;

import spec.validation.BeanValidationException;
import base.validation.ValidateCondition;

/**
 *
 * @author Yamashita,Takahiro
 */
public interface CommandPreCondition<T> {

    public void validatePreCondition(T entity);

    public default ValidateCondition.Void invalidPreCondition(T entity) {
        try {
            this.validatePreCondition(entity);
            return new ValidateCondition.Void();
        } catch (BeanValidationException ex) {
            return new ValidateCondition.Void(ex);
        }
    }

}
