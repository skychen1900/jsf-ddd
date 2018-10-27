package exsample.jsf.presentation.userregistration;

import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import ee.domain.annotation.view.ViewContext;
import exsample.jsf.application.service.RegisterUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import javax.inject.Inject;

@Controller
public class UserRegistrationAction {

    @ViewContext
    private UserRegistrationPage registrationPage;

    private UserService userService;

    private RegisterUser registerUser;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(UserRegistrationPage registrationForm, UserService userService, RegisterUser registerUser) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.registerUser = registerUser;
    }

    public String fwPersist() {
        this.registrationPage.init();
        return "persistedit.xhtml";
    }

    public String confirm() {
        User requestUser = this.registrationPage.toUser();
        registerUser.validatePreCondition(requestUser);
        return "persistconfirm.xhtml";
    }

    public String modify() {
        return "persistedit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        registerUser.with(requestUser);
        User responseUser = userService.persistedUser(requestUser);
        this.registrationPage.update(responseUser);
        return "persistcomplete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }

}
