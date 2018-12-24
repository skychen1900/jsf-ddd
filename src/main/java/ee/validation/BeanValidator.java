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

import base.validation.priority.PostConditionValidationPriority;
import base.validation.priority.PreConditionValidationPriority;
import base.validation.priority.ValidationPriority;
import java.io.Serializable;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.validation.ConstraintViolation;
import org.vermeerlab.beanvalidation.validator.GroupSequenceValidator;
import spec.validation.BeanValidationException;
import spec.validation.Validator;

/**
 * BeanValidationを実行する機能を提供します
 *
 * @author Yamashita,Takahiro
 */
@Dependent
public class BeanValidator implements Validator, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc }
     */
    @Override
    public void validate(Object validateTarget) {
        javax.validation.Validator validator = new GroupSequenceValidator(ValidationPriority.class);
        Set<ConstraintViolation<Object>> results = validator.validate(validateTarget);
        if (results.isEmpty() == false) {
            throw new BeanValidationException(results);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void validatePreCondition(Object validateTarget) {
        javax.validation.Validator validator = new GroupSequenceValidator(PreConditionValidationPriority.class);
        Set<ConstraintViolation<Object>> results = validator.validate(validateTarget);
        if (results.isEmpty() == false) {
            throw new BeanValidationException(results);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void validatePostCondition(Object validateTarget) {
        javax.validation.Validator validator = new GroupSequenceValidator(PostConditionValidationPriority.class);
        Set<ConstraintViolation<Object>> results = validator.validate(validateTarget);
        if (results.isEmpty() == false) {
            throw new BeanValidationException(results);
        }
    }

}
