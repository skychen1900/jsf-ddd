package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.ValidationPriority;
import ddd.presentation.ViewMessage;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

@Named
@RequestScoped
public class UserUpdateAction {

    private UserRegistrationPage registrationForm;

    private UserService userService;

    private ViewMessage viewMessage;

    public UserUpdateAction() {
    }

    @Inject
    public UserUpdateAction(UserRegistrationPage registrationForm, UserService userService, ViewMessage viewMessage) {
        this.registrationForm = registrationForm;
        this.userService = userService;
        this.viewMessage = viewMessage;
    }

    public String fwUpdate(String userId) {
        User user = new User(new UserId(userId));
        Optional<User> requestUser = this.userService.findById(user);
        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (requestUser.isPresent() == false) {
            return null;
        }
        this.registrationForm.update(requestUser.get());
        return "updateedit.xhtml?faces-redirect=true";
    }

    public String confirm() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //CDI管理外のインスタンスを引数として検証を行えば @Valid が有効（普通のBeanValidationの挙動）
        Set<ConstraintViolation<Object>> results = validator.validate(registrationForm.getValidationPersistUser(), ValidationPriority.class);
        this.viewMessage.appendMessage(results);
        if (results.isEmpty() == false) {
            return "updateedit.xhtml?faces-redirect=true";
        }

        return "updateconfirm.xhtml?faces-redirect=true";
    }

    public String register() {
        User requestUser = this.registrationForm.toUser();
        this.userService.register(requestUser);

        Optional<User> responseUser = this.userService.findByKey(requestUser);

        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (responseUser.isPresent() == false) {
            return null;
        }

        this.registrationForm.update(responseUser.get());
        return "updatecomplete.xhtml?faces-redirect=true";
    }
}
