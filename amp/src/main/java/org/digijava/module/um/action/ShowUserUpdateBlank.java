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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.util.UmUtil;

import java.util.*;

public class ShowUserUpdateBlank
    extends Action {

    // log4J class initialize String
    private static Logger logger = Logger.getLogger(ShowUserUpdateBlank.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserUpdateForm registerForm = (UserUpdateForm) form;

        User user = RequestUtils.getUser(request);

        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
        HashMap translations = new HashMap();
        Iterator iterator = TrnUtil.getLanguages(RequestUtils.
                                                 getNavigationLanguage(request).
                                                 getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale) iterator.next();
            translations.put(item.getCode(), item);
        }
        //sort languages
        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale) iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

        List organizationType = DbUtil.getOrganizationTypes();
        Collections.sort(organizationType, UmUtil.organizationNameComparator);

        // set First Name
        registerForm.setFirstNames(user.getFirstNames());

        // set Last Name
        registerForm.setLastName(user.getLastName());

        // set E-Mail
        registerForm.setEmail(user.getEmail());

        // set mailing address
        registerForm.setMailingAddress(user.getAddress());

        // set Organization Name
        registerForm.setOrganizationName(user.getOrganizationName());

        // set Organization Type
        if (user.getCountry() != null)
            registerForm.setSelectedCountryResidence(user.getCountry().getIso());

            // set country resident data
        List countries = new ArrayList(TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode()));
        Collections.sort(countries, TrnUtil.countryNameComparator);
        registerForm.setCountryResidence(countries);

        // set URL
        registerForm.setWebSite(user.getUrl());

        // get navigation language
        if (user.getUserLangPreferences() != null &&
            user.getUserLangPreferences().getAlertsLanguage() != null) {
            registerForm.setSelectedLanguage(user.getUserLangPreferences().
                                             getAlertsLanguage().getCode());
        }

        // set Navigation languages
        registerForm.setNavigationLanguages(sortedLanguages);

        return null;
    }

}
