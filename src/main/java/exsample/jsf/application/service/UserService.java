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

import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserRepository;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Yamashita,Takahiro
 */
@RequestScoped
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRepository userRepository;

    public UserService() {
    }

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<User> findById(User user) {
        return this.userRepository.findById(user);
    }

    public Optional<User> findByKey(User user) {
        return this.userRepository.findByKey(user);
    }

    public void register(User user) {
        this.userRepository.register(user);
    }

    public void remove(User user) {
        this.userRepository.remove(user);
    }

}
