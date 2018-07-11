package exsample.jsf.presentation.userregistration;

import ddd.domain.validation.BeanValidationException;
import ddd.domain.validation.ValidationPriority;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
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
public class UserUpdateAction {

    private UserRegistrationPage registrationForm;

    private UserService userService;

    public UserUpdateAction() {
    }

    @Inject
    public UserUpdateAction(UserRegistrationPage registrationForm, UserService userService) {
        this.registrationForm = registrationForm;
        this.userService = userService;
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
        Validator validator = new GroupSequenceValidator(ValidationPriority.class);
        Set<ConstraintViolation<Object>> results = validator.validate(registrationForm.getValidationForm());
        if (results.isEmpty() == false) {
            throw new BeanValidationException(results);
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
