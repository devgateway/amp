/*
 *   ShowUserRegisterBlank.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShowUserRegisterBlank.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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
import java.util.HashMap;
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
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.util.SiteUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowUserRegisterBlank extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {
        UserRegisterForm registerForm = (UserRegisterForm) form;



        // set country resident data
        List countries = DbUtil.getCountries();
        HashMap countriesMap = new HashMap();
        Iterator iterator = TrnUtil.getCountries(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnCountry item = (TrnCountry)iterator.next();
            countriesMap.put(item.getIso(), item);
        }
       //sort countries
        List sortedCountries = new ArrayList();
        iterator = countries.iterator();
        while (iterator.hasNext()) {
            Country item = (Country)iterator.next();
            sortedCountries.add(countriesMap.get(item.getIso()));
        }
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);

        if( sortedCountries != null ) {
//            sortedCountries.add(0, new Country( new Long( 0 ),null,"Select a country",null,null,null,null ) );
            sortedCountries.add(0, new TrnCountry( "-1","Select a country") );
        }
        registerForm.setCountryResidence(sortedCountries);
        // --------------------------

        // set default web site
        registerForm.setWebSite("http://");

        // set Navigation languages
        Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));

        HashMap translations = new HashMap();
        iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
        while (iterator.hasNext()) {
            TrnLocale item = (TrnLocale)iterator.next();
            translations.put(item.getCode(), item);
        }
       //sort languages
        List sortedLanguages = new ArrayList();
        iterator = languages.iterator();
        while (iterator.hasNext()) {
            Locale item = (Locale)iterator.next();
            sortedLanguages.add(translations.get(item.getCode()));
        }
        Collections.sort(sortedLanguages, TrnUtil.localeNameComparator);

        registerForm.setNavigationLanguages(sortedLanguages);

        return null;
    }
}
