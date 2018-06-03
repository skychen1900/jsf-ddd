package exsample.jsf.presentation.userregistration;

import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import exsample.jsf.domain.model.user.UserId;
import java.io.Serializable;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserRemovalAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRegistrationForm registrationForm;

    private UserService userService;

    public UserRemovalAction() {
    }

    @Inject
    public UserRemovalAction(UserRegistrationForm registrationForm, UserService userService) {
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
        return "removeconfirm.xhtml?faces-redirect=true";
    }

    public String remove() {
        User requestUser = this.registrationForm.toUser();
        this.userService.remove(requestUser);
        this.registrationForm.update(requestUser);
        return "removecomplete.xhtml?faces-redirect=true";
    }

}
