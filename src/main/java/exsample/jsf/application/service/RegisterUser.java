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

import ddd.application.commnand.Command;
import ddd.domain.validation.PostConditionValidationGroups.PostCondition;
import ddd.domain.validation.PreConditionValidationGroups.PreCondition;
import ddd.domain.validation.Validator;
import ee.domain.annotation.application.Service;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserRepository;
import javax.inject.Inject;
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author Yamashita,Takahiro
 */
@Service
public class RegisterUser implements Command<User> {

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

    @Override
    public void validatePreCondition(User entity) {
        this.user = entity;
        validator.validatePreCondition(this);
    }

    public void with(User user) {
        validatePreCondition(user);
        userRepository.register(user);
        validatePostCondition(userRepository.persistedUser(user));
    }

    @AssertTrue(message = "{same.email.user.already.exist}", groups = PreCondition.class)
    private boolean isNotExistSameEmail() {
        return userRepository.isNotExistSameEmail(user);
    }

    @AssertTrue(message = "{user.cannot.register}", groups = PostCondition.class)
    private boolean isExistEntity() {
        return userRepository.isExistEntity(user);
    }

    @AssertTrue(message = "{same.email.user.already.exist}", groups = PostCondition.class)
    private boolean isNotExistSameEmailAtOtherEntity() {
        return userRepository.isNotExistSameEmailAtOtherEntity(user);
    }

    @Override
    public void validatePostCondition(User entity) {
        this.user = entity;
        validator.validatePostCondition(this);
    }

}
