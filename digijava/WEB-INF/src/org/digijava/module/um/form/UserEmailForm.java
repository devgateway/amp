/*
*   UserEmailForm.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserEmailForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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
import org.apache.struts.action.ActionErrors ;
import org.apache.struts.action.ActionError;
import org.apache.struts.validator.ValidatorForm;

public class UserEmailForm
    extends ValidatorForm
{

    private String email;

    public UserEmailForm() {
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}