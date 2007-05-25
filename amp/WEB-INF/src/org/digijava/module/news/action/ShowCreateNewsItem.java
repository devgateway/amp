/*
 *   ShowCreateNewsItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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

package org.digijava.module.news.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.news.dbentity.News;
import org.digijava.module.news.form.NewsItemForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.module.common.util.ModuleUtil;

/**
 * Action renders form, whith which new news item is created
 */

public class ShowCreateNewsItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    NewsItemForm itemForm = (NewsItemForm) form;

    GregorianCalendar currentDate = new GregorianCalendar();

    itemForm.loadCalendar();

    Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
    //
    HashMap translations = new HashMap();
    Iterator iterator = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
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
    itemForm.setLanguages(sortedLanguages);

    //set user's navigation language as selected language

    User user = RequestUtils.getUser(request);
    UserLangPreferences userLangPreferences = user.getUserLangPreferences();
    Locale locale = null;
    if (userLangPreferences != null) {
      locale = userLangPreferences.getNavigationLanguage();
      if (locale == null) {
        locale = new Locale();
        locale.setCode("en");
      }
    }
    else {
      locale = new Locale();
      locale.setCode("en");
    }
    itemForm.setSelectedLanguage(locale.getCode());
    //
    Collection countries = TrnUtil.getCountries(RequestUtils.
                                                getNavigationLanguage(request).
                                                getCode());
    ArrayList sortedCountries = new ArrayList(countries);
    Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
    TrnCountry none = new TrnCountry(News.noneCountryIso,News.noneCountryName);
    sortedCountries.add(0,none);
    itemForm.setCountryResidence(sortedCountries);

    itemForm.setSourceUrl(request.getScheme() + "://");

    itemForm.setReleaseMonth(Integer.toString(currentDate.get(java.util.
        Calendar.MONTH) + 1));
    itemForm.setReleaseDay(Integer.toString(currentDate.get(java.util.Calendar.
        DAY_OF_MONTH)));
    itemForm.setReleaseYear(Integer.toString(currentDate.get(java.util.Calendar.
        YEAR)));

    currentDate.add(java.util.Calendar.DAY_OF_MONTH, 14);

    itemForm.setArchiveMonth(Integer.toString(currentDate.get(java.util.
        Calendar.MONTH) + 1));
    itemForm.setArchiveDay(Integer.toString(currentDate.get(java.util.Calendar.
        DAY_OF_MONTH)));
    itemForm.setArchiveYear(Integer.toString(currentDate.get(java.util.Calendar.
        YEAR)));
    //set delimiter Info

    //get delimiter character
    String delimiter = DbUtil.getShortVersionDelimiter(RequestUtils.getSite(
	  request).
	  getSiteId(),
	  RequestUtils.getModuleInstance(request).getInstanceName());
    if (delimiter != null && delimiter.trim().length() != 0 ) {
	itemForm.setShortVersionDelimiter(delimiter);

	RequestUtils.setTranslationAttribute(request, "delimiter",
					     itemForm.getShortVersionDelimiter());
    }

    return mapping.findForward("forward");

  }

}