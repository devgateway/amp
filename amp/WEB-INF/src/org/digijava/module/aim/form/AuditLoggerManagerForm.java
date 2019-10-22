package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.digijava.kernel.user.User;

public class AuditLoggerManagerForm extends FilterAuditLoggerForm implements Serializable {


    private static final long serialVersionUID = -2614366966871314200L;

    private boolean withLogin;
    private List<Long> userId;
    private List<String> teamList;

    public boolean isWithLogin() {
        return withLogin;
    }

    public void setWithLogin(boolean withLogin) {
        this.withLogin = withLogin;
    }

    public List<String> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<String> teamList) {
        this.teamList = teamList;
    }

    public List<Long> getUserId() {
        return userId;
    }

    public void setUserId(List<Long> userId) {
        this.userId = userId;
    }
}