package exsample.jsf.presentation.userregistration;

import core.annotation.presentation.controller.Controller;
import core.annotation.presentation.controller.EndConversation;
import core.annotation.presentation.controller.ViewContext;
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
        return "update-edit.xhtml";
    }

    public String confirm() {
        User requestUser = this.registrationPage.toUser();
        updateUser.validatePreCondition(requestUser);
        return "update-confirm.xhtml";
    }

    public String modify() {
        return "update-edit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        updateUser.with(requestUser);

        User responseUser = userService.registeredUser(requestUser);
        this.registrationPage.update(responseUser);
        return "update-complete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }
}
