/*
 *   ShowUserRegisterMarket.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserRegisterMarket.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.kernel.translator.util.TrnCountry;
import java.util.Collections;
import java.util.TreeSet;
import org.digijava.kernel.util.SiteUtils;

public class ShowUserRegisterMarket extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserRegisterForm registerForm = (UserRegisterForm) form;



        // set country resident data
        List countries = new ArrayList(TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode()));
        Collections.sort(countries, TrnUtil.countryNameComparator);
        if (countries != null) {
            countries.add(0, new TrnCountry("-1", "Select a country"));
        }
        registerForm.setSelectedCountryResidence("-1");
        registerForm.setCountryResidence(countries);
        // --------------------------

        // set default web site
        registerForm.setWebSite("http://");

        // set Navigation languages
//        Set languages = DgUtil.getCurrSiteUserLangs(request);
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

        if(RequestUtils.getNavigationLanguage(request) != null){
			registerForm.setSelectedLanguage(RequestUtils.getNavigationLanguage(request).getCode());
		}else{
			registerForm.setSelectedLanguage("en");
		}
        registerForm.setNavigationLanguages(languages);


        return null;
    }
}
