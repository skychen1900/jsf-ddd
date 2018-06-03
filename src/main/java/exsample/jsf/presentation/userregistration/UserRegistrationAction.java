package exsample.jsf.presentation.userregistration;

import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.io.Serializable;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserRegistrationAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRegistrationForm registrationForm;

    private UserService userService;

    public UserRegistrationAction() {
    }

    @Inject
    public UserRegistrationAction(UserRegistrationForm registrationForm, UserService userService) {
        this.registrationForm = registrationForm;
        this.userService = userService;
    }

    public String fwPersist() {
        this.registrationForm.init();
        return "persistedit.xhtml?faces-redirect=true";
    }

    public String confirm() {
        //TODO validation
        return "persistconfirm.xhtml?faces-redirect=true";
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
        return "persistcomplete.xhtml?faces-redirect=true";
    }
}
