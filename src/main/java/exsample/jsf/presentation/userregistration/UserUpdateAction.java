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

import exsample.jsf.application.service.UpdateUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import javax.inject.Inject;
import spec.annotation.presentation.controller.Controller;
import spec.annotation.presentation.controller.EndConversation;
import spec.annotation.presentation.controller.ViewContext;

@Controller
public class UserUpdateAction {

    @ViewContext
    private UserRegistrationPage registrationPage;

    private UserService userService;

    private UpdateUser updateUser;

    public UserUpdateAction() {
    }

    @Inject
    public UserUpdateAction(UserRegistrationPage registrationForm, UserService userService, UpdateUser updateUser) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.updateUser = updateUser;
    }

    public String fwUpdate(String userId) {
        User user = new User(new UserId(userId));

        User requestUser = this.userService.registeredUser(user);
        this.registrationPage.update(requestUser);
        return "update-edit.xhtml";
    }

    public String confirm() {
        User requestUser = this.registrationPage.toUser();
        updateUser.validatePreCondition(requestUser);
        return "update-confirm.xhtml";
    }

    public String modify() {
        return "update-edit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        updateUser.with(requestUser);

        User responseUser = userService.registeredUser(requestUser);
        this.registrationPage.update(responseUser);
        return "update-complete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }
}
