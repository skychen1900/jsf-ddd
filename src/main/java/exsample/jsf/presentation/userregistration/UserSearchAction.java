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

import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.List;
import javax.inject.Inject;
import base.annotation.presentation.controller.Controller;

@Controller
public class UserSearchAction {

    private UserSearchPage searchForm;

    private UserService userService;

    public UserSearchAction() {
    }

    @Inject
    public UserSearchAction(UserSearchPage searchForm, UserService userService) {
        this.searchForm = searchForm;
        this.userService = userService;
    }

    public void search() {
        List<User> users = this.userService.findAll();
        this.searchForm.init(users);
    }

}
