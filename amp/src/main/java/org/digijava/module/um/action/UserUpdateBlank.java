/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.um.action;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;

import java.util.Iterator;


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
    private static Logger logger = Logger.getLogger(UserUpdateBlank.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest
                                 request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

        logger.debug("execute() called");
        UserUpdateForm userRegisterForm = (UserUpdateForm) form;

        if (userRegisterForm == null) {
            logger.warn("UserRegisterForm is null");
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
       logger.debug("updating user");
       DbUtil.updateUser(user);

       return mapping.findForward("success");
    }
}
