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

import spec.annotation.FieldOrder;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/**
 * ConstraintViolationを{@link spec.annotation.FieldOrder} で指定した順にソートする機能を提供します.
 *
 * @author Yamashita,Takahiro
 */
class SortkeyConstraintViolationConverter {

    private final Set<ConstraintViolation<?>> constraintViolationSet;

    private SortkeyConstraintViolationConverter(Set<ConstraintViolation<?>> constraintViolationSet) {
        this.constraintViolationSet = constraintViolationSet;
    }

    static SortkeyConstraintViolationConverter of(Set<ConstraintViolation<?>> constraintViolationSet) {
        return new SortkeyConstraintViolationConverter(constraintViolationSet);
    }

    List<SortKeyConstraintViolation> toList() {
        return constraintViolationSet.stream()
                .map(this::makeSortKey)
                .collect(Collectors.toList());
    }

    //
    private SortKeyConstraintViolation makeSortKey(ConstraintViolation<?> constraintViolation) {
        Class<?> clazz = constraintViolation.getRootBeanClass();
        List<String> paths = Arrays.asList(constraintViolation.getPropertyPath().toString().split("\\."));
        String key = this.recursiveAppendKey(clazz, paths, 0, clazz.getCanonicalName());
        return new SortKeyConstraintViolation(key, constraintViolation);
    }

    //
    private String recursiveAppendKey(Class<?> clazz, List<String> paths, Integer index, String appendedKey) {
        if (paths.size() - 1 <= index) {
            return appendedKey;
        }

        String field = paths.get(index);
        String fieldOrder = fieldOrder(clazz, field);
        String key = appendedKey + fieldOrder + field;

        try {
            Class<?> nextClass = clazz.getDeclaredField(field).getType();
            return this.recursiveAppendKey(nextClass, paths, index + 1, key);
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new ConstraintViolationSortingException("Target field or filedtype can not get.", ex);
        }
    }

    //
    private String fieldOrder(Class<?> clazz, String property) {
        short index = Short.MAX_VALUE;

        try {
            Field field = clazz.getDeclaredField(property);
            FieldOrder fieldOrder = field.getAnnotation(FieldOrder.class);
            if (fieldOrder != null) {
                index = fieldOrder.value();
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new ConstraintViolationSortingException("Target field can not get.", ex);
        }

        return String.format("%03d", index);
    }

}
