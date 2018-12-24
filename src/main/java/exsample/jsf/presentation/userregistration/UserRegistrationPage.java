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

import exsample.jsf.application.service.RegisterUser;
import exsample.jsf.domain.model.user.GenderType;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import exsample.jsf.presentation.userregistration.form.DateOfBirthForm;
import exsample.jsf.presentation.userregistration.form.EmailForm;
import exsample.jsf.presentation.userregistration.form.GenderForm;
import exsample.jsf.presentation.userregistration.form.NameForm;
import exsample.jsf.presentation.userregistration.form.PhoneNumberForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.Valid;
import base.annotation.FieldOrder;
import base.annotation.presentation.view.InvalidMessageMapping;
import base.annotation.presentation.view.View;
import base.validation.Validator;

/**
 *
 * @author Yamashita,Takahiro
 */
@View
public class UserRegistrationPage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Validator validator;

    private UserId userId;

    @Valid
    @FieldOrder(1)
    @InvalidMessageMapping(RegisterUser.Error.SAME_EMAIL_USER_ALREADY_EXIST)
    private EmailForm email;

    @Valid
    @FieldOrder(2)
    private NameForm name;

    @Valid
    @FieldOrder(3)
    private DateOfBirthForm dateOfBirth;

    @Valid
    @FieldOrder(4)
    private PhoneNumberForm phoneNumber;

    @Valid
    @FieldOrder(5)
    private GenderForm gender;

    public UserRegistrationPage() {
    }

    @Inject
    public UserRegistrationPage(Validator validator) {
        this.validator = validator;
    }

    public void init() {
        this.userId = new UserId();
        this.email = new EmailForm();
        this.name = new NameForm();
        this.dateOfBirth = new DateOfBirthForm();
        this.phoneNumber = new PhoneNumberForm();
        this.gender = new GenderForm();
    }

    public void update(User user) {
        this.userId = user.getUserId();
        this.email = new EmailForm(user.getUserEmail().getValue());
        this.name = new NameForm(user.getName().getValue());
        this.dateOfBirth = new DateOfBirthForm(user.getDateOfBirth().getValue());
        this.phoneNumber = new PhoneNumberForm(user.getPhoneNumber().getValue());
        this.gender = new GenderForm(user.getGender().getValue());
    }

    public User toUser() {
        this.validator.validate(this);
        return new User(this.userId, email.getValue(), name.getValue(), dateOfBirth.getValue(), phoneNumber.getValue(), gender.getValue());
    }

    public Map<String, String> checked(Integer index) {
        GenderType genderType = GenderType.find(index);
        Map<String, String> map = new HashMap<>();
        if (this.gender.isSameType(genderType)) {
            map.put("checked", "checked");
        }
        return map;
    }

    public String targetFor(String clientId, String targetName) {
        return clientId + "-" + targetName;
    }

    public void setGender(Integer index) {
        this.gender = new GenderForm(GenderType.find(index));
    }

    public void setEmail(String email) {
        this.email = new EmailForm(email);
    }

    public void setName(String name) {
        this.name = new NameForm(name);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = new DateOfBirthForm(dateOfBirth);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumberForm(phoneNumber);
    }

    public String getEmail() {
        return email.display();
    }

    public String getName() {
        return name.display();
    }

    public String getDateOfBirth() {
        return dateOfBirth.display();
    }

    public String getPhoneNumber() {
        return phoneNumber.display();
    }

    public String getGender() {
        return gender.display();
    }

}
