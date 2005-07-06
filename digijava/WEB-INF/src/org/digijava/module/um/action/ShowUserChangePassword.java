/*
*   ShowUserChangePassword.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ShowUserChangePassword.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserChangePasswordForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

public class ShowUserChangePassword
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowUserChangePassword.class);


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

		UserChangePasswordForm pwdForm = (UserChangePasswordForm) form;
		User user = (User)RequestUtils.getUser(request);
		pwdForm.setUserName(user.getFirstNames() + " " + user.getLastName());

        return mapping.findForward("forward");
    }

}
