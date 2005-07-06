/*
*   UserChangePasswordForm.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserChangePasswordForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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

import org.apache.struts.action.ActionForm;
import org.digijava.kernel.entity.*;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
//////////////
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.validator.ValidatorForm;

public class UserChangePasswordForm
    extends ValidatorForm {

    private String currentpassword;
    private String newpassword;
    private String confirmpassword;
    private String name;

    public UserChangePasswordForm() {
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    /**
     *
     * @param actionMapping
     * @param httpServletRequest
     */
    public void reset(ActionMapping actionMapping,
                      HttpServletRequest httpServletRequest) {

        setCurrentpassword(null);
        setNewpassword(null);
        setConfirmpassword(null);
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        if ( (this.getNewpassword() == null) ||
            this.getNewpassword().trim().length() == 0) {
            ActionError error = new ActionError(
                "error.update.blankconfirmpassword");
            errors.add(null, error);

        }
        else if (! (this.getConfirmpassword().equals(this.getNewpassword()))) {
            ActionError error = new ActionError("error.update.noPasswordMatch");
            errors.add(null, error);

        }

        if (errors.isEmpty()) {
            return null;
        }
        else {
            return errors;
        }

    }
}