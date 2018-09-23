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
package exsample.jsf.infrastructure.datasource.user;

import dummy.datastore.UserTable;
import ddd.domain.exception.EntityNotExistException;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class UserRepositoryImpl implements UserRepository {

    //DB風ののもの仮に準備したもの
    @Inject
    UserTable userTable;

    @Override
    public List<User> findAll() {
        return userTable.list().stream().sorted(Comparator.comparing(user -> user.getUserId().getValue())).collect(Collectors.toList());
    }

    @Override
    public User findById(User user) {
        return this.userTable.findById(user).orElseThrow(() -> new EntityNotExistException("User Entity does not exist"));
    }

    @Override
    public User findByKey(User user) {
        return this.userTable.findByKey(user).orElseThrow(() -> new EntityNotExistException("User Entity does not exist"));
    }

    @Override
    public boolean isExistById(User user) {
        return this.userTable.findById(user).isPresent();
    }

    @Override
    public boolean isExistByEmail(User user) {
        return this.userTable.findByKey(user).isPresent();
    }

    @Override
    public void register(User user) {
        this.userTable.put(user);
    }

    @Override
    public void remove(User user) {
        this.userTable.remove(user);
    }

}
