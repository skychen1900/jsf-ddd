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
package exsample.jsf.domain.model.user;

import exsample.jsf.domain.validation.DateTimeFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author Yamashita,Takahiro
 */
public class DateOfBirth {

    @DateTimeFormat
    private final String value;

    public DateOfBirth(String value) {
        this.value = value;
    }

    public Age currentAge() {
        return new Age(this.date());
    }

    public String getValue() {
        return this.value;
    }

    public LocalDate date() {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DateOfBirth other = (DateOfBirth) obj;
        return Objects.equals(this.value, other.value);
    }

}
