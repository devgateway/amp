/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.admin.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

//ActionForm for user Administration

public class AdministrateUserForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private Long selectedUserId;

    private String firstNames;
    private String lastName;

    private String newPassword;
    private String confirmnewPassword;

    private boolean ban;
    private boolean globalAdmin;
    private boolean emailVerified;

    public void reset(ActionMapping mapping, HttpServletRequest request) {

        newPassword = null;
        confirmnewPassword = null;

        ban = false;
        globalAdmin = false;
        emailVerified = false;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = new ActionErrors();

        if ( ( (newPassword == null) || (newPassword.trim().length() == 0)) &&
            (confirmnewPassword != null) &&
            (confirmnewPassword.trim().length() != 0)) {
            errors.add(null, new ActionMessage("error.admin.password1Empty"));
        }
        if ( (newPassword != null) && (newPassword.trim().length() != 0) &&
            ( (confirmnewPassword == null) ||
             (confirmnewPassword.trim().length() == 0))) {
            errors.add(null, new ActionMessage("error.admin.password2Empty"));
        }

        if ( (newPassword != null) && (newPassword.trim().length() != 0) &&
            (confirmnewPassword != null) &&
            (confirmnewPassword.trim().length() != 0) &&
            ( (newPassword.compareTo(confirmnewPassword)) != 0)) {
            errors.add(null, new ActionMessage("error.admin.passwordsDiffer"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
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

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

}
