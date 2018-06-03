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

import ddd.presantation.Form;
import exsample.jsf.domain.model.user.UserEmail;

/**
 *
 * @author Yamashita,Takahiro
 */
public class EmailForm implements Form {

    private final UserEmail value;

    public EmailForm() {
        this.value = new UserEmail("");
    }

    public EmailForm(String userEmail) {
        this.value = new UserEmail(userEmail);
    }

    @Override
    public String display() {
        return value.getValue();
    }

    public UserEmail getValue() {
        return this.value;
    }

}
