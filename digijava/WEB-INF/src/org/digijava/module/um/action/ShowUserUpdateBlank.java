/*
 *   ShowUserUpdateBlank.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserUpdateBlank.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.ItemBeanInfo;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.module.um.form.UserUpdateForm;
import org.digijava.module.um.util.DbUtil;
import java.util.Iterator;
import org.digijava.kernel.util.DgUtil;
import java.util.Set;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.translator.util.TrnUtil;
import java.util.HashMap;
import org.digijava.kernel.translator.util.TrnLocale;
import java.util.Collections;
import org.digijava.module.um.util.UmUtil;
import org.digijava.kernel.util.SiteUtils;

public class ShowUserUpdateBlank
    extends Action {

    // log4J class initialize String
    private static Logger logger = I18NHelper.getKernelLogger(
        ShowUserUpdateMarket.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserUpdateForm registerForm = (UserUpdateForm) form;

        User user = (User) request.getSession(true).getAttribute(Constants.USER);

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