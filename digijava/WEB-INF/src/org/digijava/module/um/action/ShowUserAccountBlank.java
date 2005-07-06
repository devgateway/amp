/*
 *   ShowUserAccountBlank.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserAccountBlank.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.apache.log4j.Level;
import org.digijava.module.um.form.UserAccountForm;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.um.util.DbUtil;
import java.util.List;
import java.util.Iterator;
import org.digijava.kernel.request.Site;
import java.util.HashSet;
import java.util.Set;
import org.digijava.module.um.util.UmUtil;
import org.digijava.kernel.util.RequestUtils;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowUserAccountBlank
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(ShowUserAccountMarket.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserAccountForm accountForm = (UserAccountForm)form;

        // get user object from session
        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        // get success action forward
        ActionForward forward = mapping.findForward("forward");

        // check if user loged in
        // if is true fill account form
        if( user != null ) {

            // Set first name
            accountForm.setFirstName(user.getFirstNames());

            // set last name
            accountForm.setLastName(user.getLastName());

            // set email
            accountForm.setEmail(user.getEmail());


            // set organization name
            accountForm.setOrganizationName(user.getOrganizationName());

            // set organization type if any
            if( user.getOrganizationType() != null )
                accountForm.setOrganizationType(user.getOrganizationType().getType());

            // set country of residence if any
            if( user.getCountry() != null )
                accountForm.setCountryOfResidence(user.getCountry().getCountryName());

            // set URL (Web site)
            accountForm.setUrl(user.getUrl());

            // set address
            accountForm.setMailingAddress(user.getAddress());

            // set receive newsletter
            accountForm.setReceiveNewsletter(user.getUserPreference().isReceiveAlerts());

            // set display my profile
            accountForm.setDisplayMyProfile(user.getUserPreference().isPublicProfile());

            // set navigation language
            Locale alertLanguge = user.getUserLangPreferences().getAlertsLanguage();
            if( alertLanguge == null ) {
                alertLanguge = RequestUtils.getNavigationLanguage(request);
            }
            accountForm.setNavigationLanguage(alertLanguge);

            // set content language
            accountForm.setContentLanguages(user.getUserLangPreferences().getContentLanguages());
        }
        else {
            // if user object can't found in session then
            // forward to home page
            forward = mapping.findForward("welcome");

            if (logger.isDebugEnabled()) {
                String errKey = "Module.Um.ShowAccountAction.userNotFound";
                logger.l7dlog(Level.ERROR, errKey, null, null );
            }
        }

        return null;
    }
}
