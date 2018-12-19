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

import exsample.jsf.application.service.RegisterUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import javax.inject.Inject;
import spec.annotation.presentation.controller.Controller;
import spec.annotation.presentation.controller.EndConversation;
import spec.annotation.presentation.controller.ViewContext;

@Controller
public class UserRegistrationAction {

    @ViewContext
    private UserRegistrationPage registrationPage;

    private UserService userService;

    private RegisterUser registerUser;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(UserRegistrationPage registrationForm, UserService userService, RegisterUser registerUser) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.registerUser = registerUser;
    }

    public String fwPersist() {
        this.registrationPage.init();
        return "persist-edit.xhtml";
    }

    public String confirm() {
        User requestUser = this.registrationPage.toUser();
        registerUser.validatePreCondition(requestUser);
        return "persist-confirm.xhtml";
    }

    public String modify() {
        return "persist-edit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        registerUser.with(requestUser);
        User responseUser = userService.persistedUser(requestUser);
        this.registrationPage.update(responseUser);
        return "persist-complete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }

}
