package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.Validator;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
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
        return "persistedit.xhtml?faces-redirect=true";
    }

    public String confirm() {
        validator.validate(registrationPage.getValidationForm());
        return "persistconfirm.xhtml?faces-redirect=true";
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
        return "persistcomplete.xhtml?faces-redirect=true";
    }
}
