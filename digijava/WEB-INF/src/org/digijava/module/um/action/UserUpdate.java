/*
 *   UserUpdate.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserUpdate.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;
import java.util.Iterator;
import org.digijava.kernel.entity.UserLangPreferences;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UpdateUserProfile
 */

public class UserUpdate
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(UserUpdate.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        UserUpdateForm userRegisterForm = (UserUpdateForm) form;

        if (userRegisterForm == null) {
            String debugKey = "Module.Um.UserRegisterAction.formnull";
            logger.l7dlog(Level.WARN, debugKey, null, null);
        }

        // get user object from session
        User user = (User) request.getSession(true).getAttribute(Constants.USER);

        if( user == null ) return null;

        // set first name
        user.setFirstNames(userRegisterForm.getFirstNames() );

        // set last name
        user.setLastName(userRegisterForm.getLastName() );

        // set email
        user.setEmail(userRegisterForm.getEmail());

        // set url
        user.setUrl(userRegisterForm.getWebSite());

        // set mailing address
        user.setAddress(userRegisterForm.getMailingAddress());

        // set organization name
        user.setOrganizationName(userRegisterForm.getOrganizationName());


        // set organization type
        Iterator iter = userRegisterForm.getOrganizationType().iterator();
        while (iter.hasNext()) {
            OrganizationType item = (OrganizationType)iter.next();
            if( item.getId().equals(userRegisterForm.getSelectedOrganizationType()) ) {
               user.setOrganizationType(item);
               break;
            }
        }

        // set country
        iter = userRegisterForm.getCountryResidence().iterator();
        while (iter.hasNext()) {
            Country item = (Country)iter.next();
            if( item.getIso().equals(userRegisterForm.getSelectedCountryResidence()) ) {
                user.setCountry( item );
               break;
            }
        }

        // ----- Fill user language preferences
        UserLangPreferences userLangPreferences = user.getUserLangPreferences();
        if( userLangPreferences != null ) {
            iter = userRegisterForm.getNavigationLanguages().iterator();
            while (iter.hasNext()) {
                Locale item = (Locale)iter.next();
                if( item.getCode().equals(userRegisterForm.getSelectedLanguage()) ) {
                    // set DefaultLanguage
                    userLangPreferences.setAlertsLanguage(item);
                   break;
                }
            }
            Locale language;
            Set list = new HashSet();
            for( int i =  0; i < userRegisterForm.getContentSelectedLanguages().length; i++ ) {
                language = new Locale();
                language.setCode(userRegisterForm.getContentSelectedLanguages()[i]);
                list.add(language);
            }
            userLangPreferences.setContentLanguages(list);
        }
       // -----

       // ----- Fill user preferences
       UserPreferences userPreferences = user.getUserPreference();
       if( userPreferences != null ) {
           userPreferences.setPublicProfile(userRegisterForm.getMembersProfile());
           userPreferences.setReceiveAlerts(userRegisterForm.isNewsLetterRadio());
       }
      // -----



       // register user in database
       DbUtil.updateUser(user);

       return mapping.findForward("success");
    }
}
