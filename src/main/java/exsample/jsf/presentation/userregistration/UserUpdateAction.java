package exsample.jsf.presentation.userregistration;

import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import ee.domain.annotation.view.ViewContext;
import exsample.jsf.application.service.UpdateUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import javax.inject.Inject;

@Controller
public class UserUpdateAction {

    @ViewContext
    private UserRegistrationPage registrationPage;

    private UserService userService;

    private UpdateUser updateUser;

    public UserUpdateAction() {
    }

    @Inject
    public UserUpdateAction(UserRegistrationPage registrationForm, UserService userService, UpdateUser updateUser) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.updateUser = updateUser;
    }

    public String fwUpdate(String userId) {
        User user = new User(new UserId(userId));

        User requestUser = this.userService.registeredUser(user);
        this.registrationPage.update(requestUser);
        return "updateedit.xhtml";
    }

    public String confirm() {
        User requestUser = this.registrationPage.toUser();
        updateUser.validatePreCondition(requestUser);
        return "updateconfirm.xhtml";
    }

    public String modify() {
        return "updateedit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        updateUser.with(requestUser);

        User responseUser = userService.registeredUser(requestUser);
        this.registrationPage.update(responseUser);
        return "updatecomplete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }
}
