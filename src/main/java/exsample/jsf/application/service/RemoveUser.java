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

import spec.interfaces.application.commnand.CommandPreCondition;
import spec.validation.PreConditionValidationGroups;
import spec.validation.ValidateCondition;
import spec.validation.Validator;
import spec.annotation.application.Service;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserRepository;
import javax.inject.Inject;

/**
 *
 * @author Yamashita,Takahiro
 */
@Service
public class RemoveUser implements CommandPreCondition<User> {

    private User user;

    private UserRepository userRepository;
    private Validator validator;
    private RegisterUser registerUser;

    public RemoveUser() {
    }

    @Inject
    public RemoveUser(UserRepository userRepository, Validator validator, RegisterUser registerUser) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.registerUser = registerUser;
    }

    @Override
    public void validatePreCondition(User user) {
        this.user = user;
        validator.validate(this);
    }

    public void with(User user) {
        validatePreCondition(user);
        userRepository.remove(user);
    }

    @ValidateCondition(groups = PreConditionValidationGroups.PreCondition.class)
    private ValidateCondition.Void getRegisteredUser() {
        return registerUser.invalidPostCondition(user);
    }

}
