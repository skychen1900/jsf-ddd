package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.Validator;
import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import exsample.jsf.application.service.UpdateUser;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import java.util.Optional;
import javax.inject.Inject;

@Controller
public class UserUpdateAction {

    private UserRegistrationPage registrationPage;

    private UserService userService;

    private UpdateUser updateUser;

    private Validator validator;

    public UserUpdateAction() {
    }

    @Inject
    public UserUpdateAction(UserRegistrationPage registrationForm, UserService userService, UpdateUser updateUser, Validator validator) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.updateUser = updateUser;
        this.validator = validator;
    }

    public String fwUpdate(String userId) {
        User user = new User(new UserId(userId));
        Optional<User> requestUser = this.userService.findById(user);
        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (requestUser.isPresent() == false) {
            return null;
        }
        this.registrationPage.update(requestUser.get());
        return "updateedit.xhtml";
    }

    public String confirm() {
        validator.validate(registrationPage.getValidationForm());
        User requestUser = this.registrationPage.toUser();
        updateUser.validatePreCondition(requestUser);
        return "updateconfirm.xhtml";
    }

    public String modify() {
        return "updateedit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        updateUser.execute(requestUser);

        Optional<User> responseUser = this.userService.findByKey(requestUser);

        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (responseUser.isPresent() == false) {
            return null;
        }

        this.registrationPage.update(responseUser.get());
        return "updatecomplete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }
}
