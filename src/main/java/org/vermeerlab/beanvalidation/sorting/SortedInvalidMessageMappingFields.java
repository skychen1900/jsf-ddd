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
package org.vermeerlab.beanvalidation.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/**
 *
 *
 * @author Yamashita,Takahiro
 */
public class SortedInvalidMessageMappingFields {

    private final List<SortedInvalidMessageMappingField> sortedInvalidMessageMappingFields;

    public SortedInvalidMessageMappingFields(List<SortedInvalidMessageMappingField> sortedInvalidMessageMappingFields) {
        this.sortedInvalidMessageMappingFields = sortedInvalidMessageMappingFields;
    }

    public List<ConstraintViolationWithSortKey> toList(Set<ConstraintViolation<?>> constraintViolations) {

        List<ConstraintViolationWithSortKey> constraintViolationWithSortKeys = new ArrayList<>();

        for (ConstraintViolation<?> constraintViolation : constraintViolations) {

            constraintViolationWithSortKeys.addAll(
                    sortedInvalidMessageMappingFields.stream()
                            .filter(field -> field.getMessage().equals(constraintViolation.getMessageTemplate()))
                            .map(sortedInvalidMessageMappingField -> {
                                return new ConstraintViolationWithSortKey(sortedInvalidMessageMappingField.getKey(), constraintViolation);
                            })
                            .collect(Collectors.toList())
            );

        }
        return Collections.unmodifiableList(constraintViolationWithSortKeys);
    }

}
