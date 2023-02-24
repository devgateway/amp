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

package org.digijava.module.um.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


public final class LogonForm
    extends ValidatorForm {

    // ------------------------------------------------ Instance Variables

    /**
     * The password.
     */
    private String password = null;

    /**
     * The username.
     */
    private String username = null;

    /**
     * Save Login.
     */
    private boolean saveLogin;

    /**
     * Authomatic login
     */
    private boolean autoLogin;

    /**
     * Return the password.
     */
    public String getPassword() {
        return (this.password);
    }

    /**
     * Set the password.
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the username.
     */
    public String getUsername() {
        return (this.username);
    }

    /**
     * Set the username.
     * @param username The new username
     */
    public void setUsername(String username) {

        this.username = username;

    }

    /**
     * Get save login
     * @return
     */
    public boolean isSaveLogin() {
        return saveLogin;
    }

    /**
     * Set save login
     *
     * @param saveLogin
     */
    public void setSaveLogin(boolean saveLogin) {
        this.saveLogin = saveLogin;
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        password = null;
        username = null;
        saveLogin = false;
        autoLogin = false;
    }

    /**
     * Ensure that both fields have been input.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(
        ActionMapping actionMapping,
        HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        return errors;

    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

} // End LogonForm
