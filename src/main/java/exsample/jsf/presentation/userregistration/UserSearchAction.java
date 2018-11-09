package exsample.jsf.presentation.userregistration;

import spec.annotation.presentation.controller.Controller;
import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.util.List;
import javax.inject.Inject;

@Controller
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
        return "index.xhtml";
    }

}
