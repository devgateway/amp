/*
 *   ShowCreateContentItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 06,2004
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

package org.digijava.module.cms.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;

public class ShowCreateContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;
    //
    contItemForm.setProcessingMode(CMSContentItemForm.MODE_NEW);

    //get languges list
    Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
    //
    HashMap translations = new HashMap();
    Iterator iterator = TrnUtil.getLanguages(RequestUtils.
                                             getNavigationLanguage(
        request).getCode()).iterator();
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
    contItemForm.setLanguages(sortedLanguages);
    //
    //get countries list
    Collection countries = TrnUtil.getCountries(RequestUtils.
                                                getNavigationLanguage(request).
                                                getCode());
    List sortedCountries = new ArrayList(countries);
    Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
    contItemForm.setCountries(sortedCountries);
    //

    if (request.getParameter("reset") == null) {
      contItemForm.setUrl(request.getScheme() + "://");
      contItemForm.setNoReset(false);
      contItemForm.setCategoryIdList(new ArrayList());
      contItemForm.setCategoryNameList(new ArrayList());
      contItemForm.setFormFile(null);
      if (request.getParameter("catId") != null) {
        String strCatId = request.getParameter("catId");
        long catId = Long.parseLong(strCatId);
        CMSCategory ownerCategory = DbUtil.getCategoryItem(catId);
        contItemForm.getCategoryIdList().add(strCatId);
        contItemForm.getCategoryNameList().add(ownerCategory.getName());
        contItemForm.setCategoryId(catId);
      }

    }
    else {
      contItemForm.setNoReset(true);
    }

    return mapping.findForward("showCreateContentItem");
  }
}