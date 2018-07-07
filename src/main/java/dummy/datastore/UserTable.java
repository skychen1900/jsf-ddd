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
package dummy.datastore;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import exsample.jsf.domain.model.user.DateOfBirth;
import exsample.jsf.domain.model.user.Gender;
import exsample.jsf.domain.model.user.GenderType;
import exsample.jsf.domain.model.user.PhoneNumber;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserEmail;
import exsample.jsf.domain.model.user.UserId;
import exsample.jsf.domain.model.user.UserName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class UserTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressFBWarnings("SE_BAD_FIELD")
    private List<User> users;

    @PostConstruct
    void init() {
        List<User> _users = new ArrayList<>();
        _users.add(new User(new UserId("1"), new UserEmail("aaaaaa@example.com"), new UserName("ＡＡ　ＡＡ"), new DateOfBirth("1980-04-01"),
                            new PhoneNumber("03-1234-5678"), new Gender(GenderType.MAN)));

        _users.add(new User(new UserId("2"), new UserEmail("bbbbbb@example.com"), new UserName("ＢＢ　ＢＢ"), new DateOfBirth("2000-05-01"),
                            new PhoneNumber("03-2345-6789"), new Gender(GenderType.WOMAN)));

        _users.add(new User(new UserId("3"), new UserEmail("cccccc@example.com"), new UserName("ＣＣ　ＣＣ"), new DateOfBirth("1990-08-31"),
                            new PhoneNumber("03-3456-7890"), new Gender(GenderType.OTHER)));
        this.users = _users;
    }

    public List<User> list() {
        return this.users;
    }

    public Optional<User> findById(User user) {
        Optional<User> filterdUser = this.users.stream()
                .filter(_user -> _user.getUserId().equals(user.getUserId()))
                .findAny();
        return filterdUser;
    }

    public Optional<User> findByKey(User user) {
        Optional<User> filterdUser = this.users.stream()
                .filter(_user -> _user.getUserEmail().equals(user.getUserEmail()))
                .findAny();
        return filterdUser;
    }

    public void put(User user) {
        List<User> filterdUsers = this.users.stream()
                .filter(_user -> Objects.equals(_user.getUserId(), user.getUserId()) == false)
                .collect(Collectors.toList());

        String _userId = user.getUserId().getValue() == null
                         ? String.valueOf(this.users.size() + 1)
                         : user.getUserId().getValue();

        User _user = new User(new UserId(_userId),
                              user.getUserEmail(), user.getName(), user.getDateOfBirth(), user.getPhoneNumber(), user.getGender());

        filterdUsers.add(_user);
        this.users = Collections.unmodifiableList(filterdUsers);
    }

    public void remove(User user) {
        List<User> filterdUsers = this.users.stream()
                .filter(_user -> Objects.equals(_user.getUserId(), user.getUserId()) == false)
                .collect(Collectors.toList());
        this.users = Collections.unmodifiableList(filterdUsers);
    }

}
