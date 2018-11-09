package exsample.jsf.presentation.userregistration;

import core.annotation.presentation.controller.Controller;
import java.io.Serializable;
import javax.inject.Inject;

@Controller
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
