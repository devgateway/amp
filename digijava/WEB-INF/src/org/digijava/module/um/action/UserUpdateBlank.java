/**
 *   UserUpdateBlank.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: UserUpdateBlank.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import org.digijava.kernel.util.DgUtil;
import java.util.*;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.util.RequestUtils;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0 UpdateUserProfile
 */

public class UserUpdateBlank
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(UserUpdateMarket.class);

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
        User user = RequestUtils.getUser(request);

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


        // set country
        Iterator iter = userRegisterForm.getCountryResidence().iterator();
        while (iter.hasNext()) {
            TrnCountry item = (TrnCountry)iter.next();
            if( item.getIso().equals(userRegisterForm.getSelectedCountryResidence()) ) {
                Country country = new Country( item.getIso() );
                country.setCountryName(item.getName());
                user.setCountry( country );
               break;
            }
        }

        // ----- Fill user language preferences
        UserLangPreferences userLangPreferences = user.getUserLangPreferences();
        if( userLangPreferences != null ) {
            iter = userRegisterForm.getNavigationLanguages().iterator();
            while (iter.hasNext()) {
                TrnLocale item = (TrnLocale)iter.next();
                if( item.getCode().equals(userRegisterForm.getSelectedLanguage()) ) {
                    Locale language = new Locale();
                    language.setCode(item.getCode());
                    language.setName(item.getName());
                    // set DefaultLanguage
                    userLangPreferences.setAlertsLanguage(language);
                   break;
                }
            }
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
