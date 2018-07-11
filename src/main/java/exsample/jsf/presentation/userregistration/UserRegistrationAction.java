package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.ValidationPriority;
import ddd.presentation.ViewMessage;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

@Named
@RequestScoped
public class UserRegistrationAction {

    private Provider<UserRegistrationPage> registrationForm;

    private UserService userService;

    private ViewMessage viewMessage;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(Provider<UserRegistrationPage> registrationForm, UserService userService, ViewMessage viewMessage) {
        this.registrationForm = registrationForm;
        this.userService = userService;
        this.viewMessage = viewMessage;
    }

    public String fwPersist() {
        this.registrationForm.get().init();
        return "persistedit.xhtml?faces-redirect=true";
    }

    public String confirm() {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //validateしても、対象のクラスフィールドに @Validと記述しても無視される
        //理由は、コンテナで生成したインスタンスのフィールドが nullだから。
        //ただし、アクセッサを経由したら値は取得できることは デバッグモードで確認済み
        Set<ConstraintViolation<Object>> results = validator.validate(registrationForm.get(), ValidationPriority.class);
        this.viewMessage.appendMessage(results);
        if (results.isEmpty() == false) {
            return "persistedit.xhtml?faces-redirect=true";
        }

        return "persistconfirm.xhtml?faces-redirect=true";
    }

    public String register() {
        User requestUser = this.registrationForm.get().toUser();
        this.userService.register(requestUser);

        Optional<User> responseUser = this.userService.findByKey(requestUser);

        //とりあえず、登録要素が無いという有り得ない状況だったら 自画面にそのまま遷移させる（ありえないケース）
        if (responseUser.isPresent() == false) {
            return null;
        }

        this.registrationForm.get().update(responseUser.get());
        return "persistcomplete.xhtml?faces-redirect=true";
    }
}
