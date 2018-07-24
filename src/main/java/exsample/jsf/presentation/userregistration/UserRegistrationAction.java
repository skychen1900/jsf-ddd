package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.Validator;
import ee.domain.annotation.controller.Controller;
import ee.domain.annotation.controller.EndConversation;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.Optional;
import javax.inject.Inject;

@Controller
public class UserRegistrationAction {

    private UserRegistrationPage registrationPage;

    private UserService userService;

    private Validator validator;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(UserRegistrationPage registrationForm, UserService userService, Validator validator) {
        this.registrationPage = registrationForm;
        this.userService = userService;
        this.validator = validator;
    }

    public String fwPersist() {
        this.registrationPage.init();
        return "persistedit.xhtml";
    }

    public String confirm() {
        validator.validate(registrationPage.getValidationForm());
        return "persistconfirm.xhtml";
    }

    public String modify() {
        return "persistedit.xhtml";
    }

    public String register() {
        User requestUser = this.registrationPage.toUser();
        this.userService.register(requestUser);

        Optional<User> responseUser = this.userService.findByKey(requestUser);

        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (responseUser.isPresent() == false) {
            return null;
        }

        this.registrationPage.update(responseUser.get());
        return "persistcomplete.xhtml";
    }

    @EndConversation
    public String fwTop() {
        return "index.xhtml";
    }

}
