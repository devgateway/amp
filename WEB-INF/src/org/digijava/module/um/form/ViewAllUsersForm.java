package org.digijava.module.um.form;

import java.util.*;

import org.apache.struts.action.*;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.UserBean;

public class ViewAllUsersForm
    extends ActionForm {
    private String keyword;
    private int type=-1;
    private Collection<UserBean> users;

    public ViewAllUsersForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<UserBean> getUsers() {
        return users;
    }

    public int getType() {
        return type;
    }

    public void setUsers(Collection<UserBean> users) {
        this.users = users;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setType(int type) {
        this.type = type;
    }
}
