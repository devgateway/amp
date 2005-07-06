/*
*   ShowUserSuccessMarket.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ShowUserSuccessMarket.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserAccountForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;
import org.digijava.kernel.util.RequestUtils;


public class ShowUserSuccessMarket
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowUserSuccessMarket.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserAccountForm accountForm = (UserAccountForm)form;

        // get user object from session
        User user = (User) RequestUtils.getUser(request);


        // check if user loged in
        // if is true fill account form
        if( user != null ) {

            // Set first name
            accountForm.setFirstName(user.getFirstNames());

            // set last name
            accountForm.setLastName(user.getLastName());

            // set email
            accountForm.setEmail(user.getEmail());
        }
        else {
            // if user object can't found in session then
            // forward to home page
            if (logger.isDebugEnabled()) {
                String errKey = "Module.Um.ShowAccountAction.userNotFound";
                logger.l7dlog(Level.ERROR, errKey, null, null );
            }
        }




        return null;
    }

}
