package exsample.jsf.presentation.userregistration;

import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserSearchAction {

    private UserSearchPage searchForm;

    private UserService userService;

    public UserSearchAction() {
    }

    @Inject
    public UserSearchAction(UserSearchPage searchForm, UserService userService) {
        this.searchForm = searchForm;
        this.userService = userService;
    }

    public String search() {
        List<User> users = this.userService.findAll();
        this.searchForm.init(users);
        return "index.xhtml?faces-redirect=true";
    }

}
