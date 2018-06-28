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
package exsample.jsf.presentation.userregistration.form;

import ddd.presentation.Form;
import exsample.jsf.domain.model.user.Gender;
import exsample.jsf.domain.model.user.GenderType;

/**
 * 性別を表示するFormです
 *
 * @author Yamashita,Takahiro
 */
public class GenderForm implements Form<Gender> {

    private final Gender value;

    public GenderForm() {
        this.value = new Gender(GenderType.MAN);
    }

    public GenderForm(GenderType value) {
        this.value = new Gender(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String display() {
        return value.getValue().getValue();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Gender getValue() {
        return this.value;
    }

    /**
     *
     * @param genderType
     * @return
     */
    public Boolean isSameType(GenderType genderType) {
        return this.value.getValue().equals(genderType);
    }

}
