/*
 *   ShowContentItemDetails.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 11,2004
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
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.cms.util.CMSManager;

public class ShowContentItemDetails
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;
    CMSContentItem cmsItem = null;

    if (contItemForm.getItemId() != null) {
      cmsItem = DbUtil.getCMSContentItem(contItemForm.getItemId());
      if (cmsItem != null) {
        contItemForm.setTitle(cmsItem.getTitle());
        contItemForm.setDescription(cmsItem.getDescription());

        if (cmsItem.getUrl() != null) {
          contItemForm.setFileName(null);
          contItemForm.setUrl(cmsItem.getUrl());
        }

        if (cmsItem.getFile() != null) {
          contItemForm.setFileName(cmsItem.getFileName());
          contItemForm.setUrl(request.getScheme() + "://");
        }
        //
        Locale language = cmsItem.getLanguage();
        contItemForm.setLanguage(language.getCode());
        contItemForm.setLanguageKey("ln:" + language.getCode());

        Country country = cmsItem.getCountry();
        contItemForm.setCountry(country.getIso());
        contItemForm.setCountryName(country.getCountryName());
        contItemForm.setCountryKey("cn:" + country.getIso());

        if (cmsItem.getCategories() != null &&
            cmsItem.getCategories().size() != 0) {
          List categoryIdList = new ArrayList();
          List categoryNameList = new ArrayList();
          List categoryList = new ArrayList();

          categoryList.addAll(cmsItem.getCategories());
          CMSManager.sortCategoryList(categoryList);

          Iterator iter = categoryList.iterator();
          while (iter.hasNext()) {
            CMSCategory item = (CMSCategory) iter.next();

            categoryIdList.add(String.valueOf(item.getId()));
            categoryNameList.add(item.getName());
          }

          contItemForm.setCategoryIdList(categoryIdList);
          contItemForm.setCategoryNameList(categoryNameList);
        }
        else {
          contItemForm.setCategoryIdList(null);
          contItemForm.setCategoryNameList(null);
        }

      }
    }

    return mapping.findForward("showContentItemDetails");
  }
}