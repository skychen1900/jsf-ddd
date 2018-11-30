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

import exsample.jsf.application.service.RemoveUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import javax.inject.Inject;
import spec.annotation.presentation.controller.Controller;
import spec.annotation.presentation.controller.EndConversation;

@Controller
public class UserRemovalAction {

    private UserRegistrationPage registrationForm;

    private UserService userService;

    private RemoveUser removeUser;

    public UserRemovalAction() {
    }

    @Inject
    public UserRemovalAction(UserRegistrationPage registrationForm, UserService userService, RemoveUser removeUser) {
        this.registrationForm = registrationForm;
        this.userService = userService;
        this.removeUser = removeUser;
    }

    public String fwRemove(String userId) {
        User user = new User(new UserId(userId));
        User requestUser = userService.registeredUser(user);
        this.registrationForm.update(requestUser);
        return "remove-confirm.xhtml";
    }

    public String remove() {
        User requestUser = this.registrationForm.toUser();
        removeUser.with(requestUser);
        return "remove-complete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }

}
