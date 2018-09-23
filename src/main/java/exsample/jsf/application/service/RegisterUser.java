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
package exsample.jsf.application.service;

import ddd.domain.validation.Validator;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserRepository;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class RegisterUser {

    private User user;

    private UserRepository userRepository;
    private Validator validator;

    public RegisterUser() {
    }

    @Inject
    public RegisterUser(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public void validatePreCondition(User user) {
        this.user = user;
        validator.validate(this);
    }

    public void execute(User user) {
        validatePreCondition(user);
        userRepository.register(user);
    }

    @AssertTrue(message = "{same.email.user.already.exist}")
    boolean isNotExistSameEmail() {
        return userRepository.isExistByEmail(user) == false;
    }

}
