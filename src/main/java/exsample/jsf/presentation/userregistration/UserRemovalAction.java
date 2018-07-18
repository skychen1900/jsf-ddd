package exsample.jsf.presentation.userregistration;

import ee.domain.annotation.controller.Action;
import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import java.util.Optional;
import javax.inject.Inject;

@Controller
@Action
public class UserRemovalAction {

    private UserRegistrationPage registrationForm;

    private UserService userService;

    public UserRemovalAction() {
    }

    @Inject
    public UserRemovalAction(UserRegistrationPage registrationForm, UserService userService) {
        this.registrationForm = registrationForm;
        this.userService = userService;
    }

    public String fwRemove(String userId) {
        User user = new User(new UserId(userId));
        Optional<User> requestUser = this.userService.findById(user);
        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (requestUser.isPresent() == false) {
            return null;
        }
        this.registrationForm.update(requestUser.get());
        return "removeconfirm.xhtml";
    }

    @EndConversation
    public String remove() {
        User requestUser = this.registrationForm.toUser();
        this.userService.remove(requestUser);
        this.registrationForm.update(requestUser);
        return "removecomplete.xhtml";
    }

}
