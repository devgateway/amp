/*
 *   LogonForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: LogonForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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
package org.digijava.module.um.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
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