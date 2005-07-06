/*
 *   EditContentItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 06,2004
 * 	 CVS-ID: $Id: EditContentItem.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.digijava.module.cms.form.CMSContentItemForm;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.dbentity.CMS;
import java.util.Set;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.RequestUtils;
import java.util.HashMap;
import java.util.Iterator;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import java.util.List;
import java.util.ArrayList;
import org.digijava.kernel.entity.Locale;
import java.util.Collections;
import java.util.Collection;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.kernel.dbentity.Country;
import org.apache.struts.upload.FormFile;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.util.CMSManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EditContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    CMS cms = null;
    CMSContentItem contentItem = null;

    ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
        request);
    String siteId;
    String instanceId;

    if (moduleInstance.getRealInstance() == null) {
      siteId = moduleInstance.getSite().getSiteId();
      instanceId = moduleInstance.getInstanceName();
    }
    else {
      siteId = moduleInstance.getRealInstance().getSite().getSiteId();
      instanceId = moduleInstance.getRealInstance().getInstanceName();
    }

    cms = DbUtil.getCMSItem(siteId, instanceId);

    if (contItemForm.getItemId() != null) {
      contentItem = DbUtil.getCMSContentItem(contItemForm.getItemId());

      if (contentItem != null) {
        contentItem.setTitle(contItemForm.getTitle());
        contentItem.setDescription(contItemForm.getDescription());

        if (contItemForm.getUrl() != null && contItemForm.getUrl().trim().length() != 0 &&
            !contItemForm.getUrl().equals(request.getScheme()+"://")) {
          contentItem.setUrl(contItemForm.getUrl());
          contentItem.setFile(null);

          contentItem.setFileName(null);
          contentItem.setContentType(null);
        } else {
          if (contItemForm.getFile() != null) {
            contentItem.setUrl(null);
            //
            contentItem.setFile(contItemForm.getFile());
            contentItem.setFileName(contItemForm.getFileName());
            contentItem.setContentType(contItemForm.getContentType());
          }
/*          else {
            contentItem.setFile(null);
            contentItem.setFileName(null);
            contentItem.setContentType(null);
          }*/
        }

        //Set owner categories
        Set categories = contentItem.getCategories();

        List selIds = contItemForm.getCategoryIdList();
        //Add new ones
        for (int index = 0;
             index < contItemForm.getCategoryIdList().size();
             index ++) {

          boolean contains = false;
          Iterator it = contentItem.getCategories().iterator();
          String catId = (String) contItemForm.getCategoryIdList().get(index);
          while (it.hasNext()) {
            CMSCategory category = (CMSCategory) it.next();
            if (catId.equals(String.valueOf(category.getId()))) {
              contains = true;
              break;
            }
          }
          if (!contains) {
            CMSCategory newCategory = DbUtil.getCategoryItem(Long.parseLong(catId));
            contentItem.getCategories().add(newCategory);
          }
        }

        //Remove
        Iterator it = contentItem.getCategories().iterator();
        while (it.hasNext()) {
          CMSCategory category = (CMSCategory) it.next();
          boolean contains = false;
          String catId = "";
          for (int index = 0;
               index < contItemForm.getCategoryIdList().size();
               index++) {
            catId = (String) contItemForm.getCategoryIdList().get(index);
            if (catId.equals(String.valueOf(category.getId()))) {
              contains = true;
              break;
            }
          }
          if (!contains) {
            it.remove();
          }
        }



        Locale locale = new Locale();
        locale.setCode(contItemForm.getLanguage());
        contentItem.setLanguage(locale);

        Country country = new Country();
        country.setIso(contItemForm.getCountry());
        contentItem.setCountry(country);

        DbUtil.updateCMSContentItem(contentItem);
      }
    }

    contItemForm.setNoReset(false);

    return mapping.findForward("editContentItem");
  }

}
