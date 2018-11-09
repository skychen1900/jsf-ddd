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

import core.validation.ValidationGroups;
import ddd.presentation.DefaultForm;
import exsample.jsf.domain.model.user.UserName;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * ユーザー名を表示するFormです
 *
 * @author Yamashita,Takahiro
 */
public class NameForm implements DefaultForm<UserName>, Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(groups = ValidationGroups.Form.class)
    private String value = "";

    public NameForm() {
    }

    public NameForm(String userName) {
        this.value = userName;
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
    @Valid
    @Override
    public UserName getValue() {
        return new UserName(this.value);
    }
}
