package org.digijava.module.um.form;

import java.util.*;

import org.apache.struts.action.*;
import org.digijava.kernel.user.User;

public class ViewAllUsersForm
    extends ActionForm {
    private String keyword;
    private int type=-1;
    private Collection<User> users;

    public ViewAllUsersForm() {
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public int getType() {
        return type;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setType(int type) {
        this.type = type;
    }
}
