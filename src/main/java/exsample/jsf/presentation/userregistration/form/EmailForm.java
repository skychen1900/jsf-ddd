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

import ddd.presentation.DefaultForm;
import exsample.jsf.domain.model.user.UserEmail;

/**
 * Emailを表示するFormです.
 *
 * @author Yamashita,Takahiro
 */
public class EmailForm implements DefaultForm<UserEmail> {

    private String value = "";

    public EmailForm() {
    }

    public EmailForm(String userEmail) {
        this.value = userEmail;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String display() {
        return this.getValue().getValue();
    }

    /**
     * @inheritDoc
     */
    @Override
    public UserEmail getValue() {
        return new UserEmail(this.value);
    }

}
