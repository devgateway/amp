/*
 *   Login.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Feb 21, 2004
 * 	 CVS-ID: $Id: Login.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
package org.digijava.module.um.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.security.HttpLoginManager;
import org.digijava.module.um.form.LogonForm;

public final class Login
    extends Action {

    private static Logger logger = Logger.getLogger(Login.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        LogonForm loginForm = (LogonForm) form;

        String sessionId;
        if (loginForm.isAutoLogin()) {
            // Log in authomatically using cookies (if any)
            sessionId = HttpLoginManager.autoLogin(request, response);
        }
        else {
            // Log in using username & password
            sessionId = HttpLoginManager.loginByCredentials(request,
                response, loginForm.getUsername().toLowerCase(), loginForm.getPassword(),
                loginForm.isSaveLogin());
        }
        // Redirect back
        HttpLoginManager.passSessionIdToReferrer(request, response, sessionId, null);
        return null;
    }
}
