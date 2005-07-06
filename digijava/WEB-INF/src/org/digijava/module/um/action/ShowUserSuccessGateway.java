/*
*   ShowUserSuccessGateway.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: ShowUserSuccessGateway.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import org.digijava.kernel.Constants;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;


public class ShowUserSuccessGateway
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowUserSuccessGateway.class);

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

            // ---------- Topics
            Site site = SiteUtils.getSite(Constants.ALL_TOPICS_SITE);
            if( site == null )
                logger.warn("site " + Constants.ALL_TOPICS_SITE + " could not found");
            String[] selectedTopics = null;

            List sites = DbUtil.getTopicsSites(site);
            if( sites != null )
                selectedTopics = new String[sites.size()];
            Set sets = UmUtil.getUserInterests(user.getInterests(), sites, selectedTopics, request);
            accountForm.setTopics(sets);
            accountForm.setSelectedTopics(selectedTopics);
            // ----------

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