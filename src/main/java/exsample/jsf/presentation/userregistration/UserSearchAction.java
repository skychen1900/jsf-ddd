package exsample.jsf.presentation.userregistration;

import exsample.jsf.application.service.UserService;
import exsample.jsf.domain.model.user.User;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserSearchAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserSearchForm searchForm;

    private UserService userService;

    public UserSearchAction() {
    }

    @Inject
    public UserSearchAction(UserSearchForm searchForm, UserService userService) {
        this.searchForm = searchForm;
        this.userService = userService;
    }

    public String search() {
        List<User> users = this.userService.findAll();
        this.searchForm.init(users);
        return "index.xhtml?faces-redirect=true";
    }

}
