package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.BeanValidationException;
import ddd.domain.validation.ValidationPriority;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.vermeerlab.beanvalidation.validator.GroupSequenceValidator;

@Named
@RequestScoped
public class UserRegistrationAction {

    private UserRegistrationPage registrationPage;

    private UserService userService;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(UserRegistrationPage registrationForm, UserService userService) {
        this.registrationPage = registrationForm;
        this.userService = userService;
    }

    public String fwPersist() {
        this.registrationPage.init();
        return "persistedit.xhtml?faces-redirect=true";
    }

    public String confirm() {
        Validator validator = new GroupSequenceValidator(ValidationPriority.class);
        Set<ConstraintViolation<Object>> results = validator.validate(registrationPage.getValidationForm());
        if (results.isEmpty() == false) {
            throw new BeanValidationException(results);
        }

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
