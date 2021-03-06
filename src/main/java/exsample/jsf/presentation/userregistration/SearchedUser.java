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
package exsample.jsf.presentation.userregistration;

import exsample.jsf.domain.model.user.Age;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import exsample.jsf.presentation.userregistration.form.EmailForm;
import exsample.jsf.presentation.userregistration.form.NameForm;
import java.io.Serializable;

public class SearchedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UserId userId;

    private final EmailForm email;

    private final NameForm name;

    private final Age age;

    public SearchedUser(User user) {
        this.userId = user.getUserId();
        this.email = new EmailForm(user.getUserEmail().getValue());
        this.name = new NameForm(user.getName().getValue());
        this.age = user.getAge();
    }

    public Integer getUserId() {
        return this.userId.getValue();
    }

    public String getEmail() {
        return email.display();
    }

    public String getName() {
        return name.display();
    }

    public Integer getAge() {
        return age.getValue();
    }

}
