package exsample.jsf.presentation.userregistration;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
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
