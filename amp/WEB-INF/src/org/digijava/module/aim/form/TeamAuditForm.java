package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.user.User;

public class TeamAuditForm extends FilterAuditLoggerForm implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -2614366966871314200L;

    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamname) {
        this.teamName = teamname;
    }
}