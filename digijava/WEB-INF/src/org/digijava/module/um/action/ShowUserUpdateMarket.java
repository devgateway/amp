/*
 *   ShowUserUpdateMarket.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserUpdateMarket.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserUpdateMarketForm;
import org.digijava.kernel.util.SiteUtils;

public class ShowUserUpdateMarket extends Action
{
	// log4J class initialize String
	private static Logger logger =
		I18NHelper.getKernelLogger(ShowUserUpdateMarket.class);

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		javax.servlet.http.HttpServletRequest request,
		javax.servlet.http.HttpServletResponse response)
		throws java.lang.Exception {
		UserUpdateMarketForm registerForm = (UserUpdateMarketForm) form;

        HashSet siteLangs = new HashSet();
        Iterator iter = SiteUtils.getUserLanguages(RequestUtils.getSite(request)).iterator();
        while (iter.hasNext()) {
            Locale item = (Locale)iter.next();
            siteLangs.add(item.getCode());
        }


		Set languages = new TreeSet(TrnUtil.localeNameComparator);
        iter = TrnUtil.getLanguages(RequestUtils.
            getNavigationLanguage(request).getCode()).iterator();
        while (iter.hasNext()) {
            TrnLocale item = (TrnLocale)iter.next();
            if (siteLangs.contains(item.getCode())) {
                languages.add(item);
            }
        }
		User user =
			(User) request.getSession().getAttribute(
				"org.digijava.kernel.user");
		if (user == null)
            logger.debug("User is NULL");
		else
		{
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
			// set Country
			if (user.getCountry() != null)
				registerForm.setSelectedCountryResidence(
					user.getCountry().getIso());
			// set URL
			registerForm.setWebSite(user.getUrl());
			// get navigation language
			if (user.getUserLangPreferences().getAlertsLanguage() != null)
			{
				registerForm.setSelectedLanguage(
					user
						.getUserLangPreferences()
						.getNavigationLanguage()
						.getCode());
				registerForm.setSelectedAlertLanguage(
					user
						.getUserLangPreferences()
						.getAlertsLanguage()
						.getCode());
			}
		}

		// set country resident data
        ArrayList countries = new ArrayList(TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode()));
        Collections.sort(countries, TrnUtil.countryNameComparator);
        registerForm.setCountryResidence(countries);
		// set Navigation languages
		registerForm.setNavigationLanguages(languages);
		// set alert languages
		registerForm.setAlertLanguages(languages);
		request.setAttribute("userUpdateMarketForm", registerForm);
		return mapping.findForward("forward");
	}
}
