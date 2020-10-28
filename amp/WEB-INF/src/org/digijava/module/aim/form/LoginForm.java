package org.digijava.module.aim.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class LoginForm extends ValidatorForm {

    private String userId = null;
    private String password = null;
    private boolean login = false;
    private Collection members;
    private Collection<AmpCategoryValue> workspaceGroups;

    public boolean getLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        if (userId != null || password != null) {
            return super.validate(mapping, request);
        } else
            return null;
    }

    /**
     * @return Returns the members.
     */
    public Collection getMembers() {
        return members;
    }
    /**
     * @param members The members to set.
     */
    public void setMembers(Collection members) {
        this.members = members;
    }

    public void setWorkspaceGroups(Collection<AmpCategoryValue> workspaceGroups) {
        this.workspaceGroups = workspaceGroups;
    }

    public Collection<AmpCategoryValue> getWorkspaceGroups() {
        return workspaceGroups;
    }
}
