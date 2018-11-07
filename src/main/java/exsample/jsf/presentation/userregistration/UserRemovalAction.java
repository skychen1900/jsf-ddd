package exsample.jsf.presentation.userregistration;

import ee.domain.annotation.controller.Action;
import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import exsample.jsf.application.service.RemoveUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import javax.inject.Inject;

@Controller
@Action
public class UserRemovalAction {

    private UserRegistrationPage registrationForm;

    private UserService userService;

    private RemoveUser removeUser;

    public UserRemovalAction() {
    }

    @Inject
    public UserRemovalAction(UserRegistrationPage registrationForm, UserService userService, RemoveUser removeUser) {
        this.registrationForm = registrationForm;
        this.userService = userService;
        this.removeUser = removeUser;
    }

    public String fwRemove(String userId) {
        User user = new User(new UserId(userId));
        User requestUser = userService.registeredUser(user);
        this.registrationForm.update(requestUser);
        return "remove-confirm.xhtml";
    }

    public String remove() {
        User requestUser = this.registrationForm.toUser();
        removeUser.with(requestUser);
        return "remove-complete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }

}
