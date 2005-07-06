/*
*   UserResetPassword.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserResetPassword.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import org.apache.struts.action.*;
import org.digijava.module.um.form.*;
import org.digijava.module.um.util.*;

/**
 *
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserResetAction
 */
public class UserResetPassword
    extends Action {

    public UserResetPassword() {
    }

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserResetForm userResetForm = (UserResetForm) form;

        String email = userResetForm.getEmail();
        String code = userResetForm.getCode();

        if (!DbUtil.ResetPassword(email, code, userResetForm.getNewpassword())) {
            return mapping.findForward("error");
        }

        return mapping.findForward("success");

    }
}