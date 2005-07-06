/*
 *   ShowCreateSite.java
 * 	 @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: BecomeUser.java,v 1.1 2005-07-06 10:34:10 rahul Exp $
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

package org.digijava.module.admin.action;

import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.module.admin.form.AdministrateUserForm;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.security.HttpLoginManager;

//Action for Become User
//Logs the admin as if s/he was selected user and redirects to the home page
//of the site-group

public class BecomeUser
    extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.
        Exception {

        AdministrateUserForm userForm = (AdministrateUserForm) form;
        User sourceUser = null;
        User user = null;
        String redirectTo = null;

        //check whether user is logged in, if not try to log in
        HttpLoginManager.becomeUser(userForm.getSelectedUserId().longValue(), request);
        // DgSecurityManager.isUserLogon(user.getEmail(), user.getPassword(), request);

        redirectTo = DgUtil.getSiteUrl(RequestUtils.getSite(request),request);
        response.sendRedirect(redirectTo);

        return null;
    }

}