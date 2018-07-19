package exsample.jsf.presentation.userregistration;

import ee.domain.annotation.controller.Action;
import ee.domain.annotation.controller.Controller;
import java.io.Serializable;
import javax.inject.Inject;

@Controller
@Action
public class UserRegistrationActionListener implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRegistrationPage registrationForm;

    public UserRegistrationActionListener() {
    }

    @Inject
    public UserRegistrationActionListener(UserRegistrationPage registrationForm) {
        this.registrationForm = registrationForm;
    }

    public void changeGender(Integer genderId) {
        this.registrationForm.setGender(genderId);
    }

}
