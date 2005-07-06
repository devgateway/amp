/*
*   UserRegister.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserRegister.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UserRegisterAction
 */

public class UserRegister
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        UserRegister.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserRegisterForm userRegisterForm = (UserRegisterForm) form;

        if (userRegisterForm == null) {
            String debugKey = "Module.Um.UserRegisterAction.formnull";
            logger.l7dlog(Level.WARN, debugKey, null, null);
        }

        // create new user class
        User user = new User(userRegisterForm.getEmail().toLowerCase(),
                             userRegisterForm.getFirstNames(),
                             userRegisterForm.getLastName());

        // set client IP address
        user.setModifyingIP(RequestUtils.getRemoteAddress(request));

        // set password
        user.setPassword(userRegisterForm.getPassword().trim());
        user.setSalt(userRegisterForm.getPassword().trim());

        // set Website
        user.setUrl(userRegisterForm.getWebSite());

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());

        // set organization type
        user.setOrganizationType(new OrganizationType(userRegisterForm.getSelectedOrganizationType()));

        // set country
        user.setCountry( new Country(userRegisterForm.getSelectedCountryResidence()) );

        // set referral
        user.setReferral(userRegisterForm.getHowDidyouSelect());

        // set default language
        user.setRegisterLanguage(RequestUtils.getNavigationLanguage(request));

        // ----- Fill user preferences
        UserPreferences preference = user.getUserPreference();
        if( preference == null ) {
            SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);

            // ------------- SET USER PREFERENCE
            preference = new UserPreferences(user, siteDomain.getSite());

            preference.setPublicProfile(userRegisterForm.getMembersProfile());
            preference.setReceiveAlerts(userRegisterForm.isNewsLetterRadio());
            user.setUserPreference(preference);
            // -----------------

            // ------------- SET USER LANGUAGES
            UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(siteDomain.getSite()));
            Locale language = new Locale();
            language.setCode(userRegisterForm.getSelectedLanguage());

            // set alert language
            userLangPreferences.setAlertsLanguage(language);

            // set navigation language
            userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(request));


            // set content language
            Set list = new HashSet();
            for( int i =  0; i < userRegisterForm.getContentSelectedLanguages().length; i++ ) {
                language = new Locale();
                language.setCode(userRegisterForm.getContentSelectedLanguages()[i]);
                list.add(language);
            }
            userLangPreferences.setContentLanguages(list);

            user.setUserLangPreferences(userLangPreferences);
            // --------------------------------
        }
       // -----

       // if email register get error message
        if (DbUtil.isRegisteredEmail(user.getEmail())) {

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                String debugKey = "Module.Um.UserRegisterAction.emailexits";
                Object params[] = {
                    user.getEmail()};
                logger.l7dlog(Level.DEBUG, debugKey, params, null);
            }

            // email already exists
/*            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.registration.emailexits"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput())); */
            return (mapping.findForward("failure"));
        }
        else {

            // register user in database
            DbUtil.registerUser(user);

            // send mail
//            DgUtil.sendMail(userRegisterForm.getEmail(), "DIGI mailer",
//                          "Thank you Message");
            // -----------------------------------------

            // if set debug mode then print out
            if (logger.isDebugEnabled()) {
                String debugKey = "Module.Um.UserRegisterAction.mailsend";
                Object params[] = {
                    user.getEmail()};
                logger.l7dlog(Level.DEBUG, debugKey, params, null);
            }
        }
        //////////////
        return mapping.findForward("success");
    }

}
