/*
 *   AdministrateUserForm.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 18, 2003
 * 	 CVS-ID: $Id: AdministrateUserForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.admin.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

//ActionForm for user Administration

public class AdministrateUserForm
    extends ActionForm {

    private Long selectedUserId;

    private String firstNames;
    private String lastName;

    private boolean alertable;

    private boolean ban;
    private String newPassword;
    private String confirmnewPassword;

    private boolean globalAdmin;

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        newPassword = null;
        confirmnewPassword = null;

        alertable = false;
        ban = false;
        globalAdmin = false;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = new ActionErrors();

        if ( ( (newPassword == null) || (newPassword.trim().length() == 0)) &&
            (confirmnewPassword != null) &&
            (confirmnewPassword.trim().length() != 0)) {
            errors.add(null, new ActionError("error.admin.password1Empty"));
        }
        if ( (newPassword != null) && (newPassword.trim().length() != 0) &&
            ( (confirmnewPassword == null) ||
             (confirmnewPassword.trim().length() == 0))) {
            errors.add(null, new ActionError("error.admin.password2Empty"));
        }

        if ( (newPassword != null) && (newPassword.trim().length() != 0) &&
            (confirmnewPassword != null) &&
            (confirmnewPassword.trim().length() != 0) &&
            ( (newPassword.compareTo(confirmnewPassword)) != 0)) {
            errors.add(null, new ActionError("error.admin.passwordsDiffer"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public boolean isAlertable() {
        return alertable;
    }

    public void setAlertable(boolean alertable) {
        this.alertable = alertable;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public String getConfirmnewPassword() {
        return confirmnewPassword;
    }

    public void setConfirmnewPassword(String confirmnewPassword) {
        this.confirmnewPassword = confirmnewPassword;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }

}