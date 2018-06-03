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
package exsample.jsf.domain.model.user;

/**
 *
 * @author Yamashita,Takahiro
 */
public class User {

    private final UserId userId;

    private final UserEmail userEmail;

    private final UserName name;

    private final DateOfBirth dateOfBirth;

    private final PhoneNumber phoneNumber;

    private final Gender gender;

    public User(UserId userId, UserEmail email, UserName name, DateOfBirth dateOfBirth, PhoneNumber phoneNumber, Gender gender) {
        this.userId = userId;
        this.userEmail = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public User(UserId userId) {
        this(userId, null, null, null, null, null);
    }

    public static User createDefault() {
        return new User(new UserId(null), new UserEmail(""), new UserName(""), new DateOfBirth("1900-01-01"), new PhoneNumber(""), new Gender(
                        GenderType.MAN));
    }

    public UserId getUserId() {
        return userId;
    }

    public UserEmail getUserEmail() {
        return userEmail;
    }

    public UserName getName() {
        return name;
    }

    public Age getAge() {
        return dateOfBirth.currentAge();
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return this.gender;
    }

}
