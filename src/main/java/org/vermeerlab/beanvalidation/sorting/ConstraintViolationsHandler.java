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

import static java.util.Comparator.comparing;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

/**
 *
 * @author Yamashita,Takahiro
 */
public class ConstraintViolationsHandler {

    private final List<SortKeyConstraintViolation> sortKeyConstraintViolations;

    private ConstraintViolationsHandler(List<SortKeyConstraintViolation> sortKeyConstraintViolations) {
        this.sortKeyConstraintViolations = sortKeyConstraintViolations;
    }

    public List<ConstraintViolation<?>> sortedConstraintViolations() {
        return sortKeyConstraintViolations.stream()
                .sorted(comparing(SortKeyConstraintViolation::getSortkey)
                        .thenComparing(s -> s.getConstraintViolation().getMessageTemplate()))
                .map(SortKeyConstraintViolation::getConstraintViolation)
                .collect(Collectors.toList());
    }

    public static class Builder {

        private final MessageTmplateSortKeyMap messageTmplateSortKeyMap;
        private Set<ConstraintViolation<?>> constraintViolationSet;

        public Builder() {
            messageTmplateSortKeyMap = new MessageTmplateSortKeyMap();
            constraintViolationSet = new HashSet<>();
        }

        public Builder messageSortkeyMap(MessageTmplateSortKeyMap messageTmplateSortKeyMap) {
            this.messageTmplateSortKeyMap.putAll(messageTmplateSortKeyMap);
            return this;
        }

        public Builder constraintViolationSet(Set<ConstraintViolation<?>> constraintViolationSet) {
            this.constraintViolationSet = constraintViolationSet;
            return this;
        }

        public ConstraintViolationsHandler build() {
            return new ConstraintViolationsHandler(
                    messageTmplateSortKeyMap.replaceSortKey(
                            SortkeyConstraintViolationConverter.of(constraintViolationSet).toList()
                    ));
        }

    }
}
