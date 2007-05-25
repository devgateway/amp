/*
 *   CreateContentItem.java
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

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.CMSEmailManager;
import org.digijava.module.cms.util.DbUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CreateContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
        request);
    String siteId;
    String instanceId;

    User user = RequestUtils.getUser(request);

    if (moduleInstance.getRealInstance() == null) {
      siteId = moduleInstance.getSite().getSiteId();
      instanceId = moduleInstance.getInstanceName();
    }
    else {
      siteId = moduleInstance.getRealInstance().getSite().getSiteId();
      instanceId = moduleInstance.getRealInstance().getInstanceName();
    }

    CMS cms = DbUtil.getCMSItem(siteId, instanceId);
    CMSContentItem contentItem = null;

    if (cms != null) {
      contentItem = new CMSContentItem(cms);

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

          contentItem.setFile(contItemForm.getFile());
          contentItem.setFileName(contItemForm.getFileName());
          contentItem.setContentType(contItemForm.getContentType());
        }
      }

      Locale locale = new Locale();
      locale.setCode(contItemForm.getLanguage());
      contentItem.setLanguage(locale);

      Country country = new Country();
      country.setIso(contItemForm.getCountry());
      contentItem.setCountry(country);

      //Set owner categories
      if (contItemForm.getCategoryIdList() != null) {
        Iterator it = contItemForm.getCategoryIdList().iterator();
        while (it.hasNext()) {
          long catId = Long.parseLong((String)it.next());
          CMSCategory category = DbUtil.getCategoryItem(catId);
          contentItem.getCategories().add(category);
        }
      }

      // add author
      contentItem.setAuthorUser(user);
      //
      contentItem.setPublished(true);

      if (cms.isModerated() &&
          (!DgUtil.isModuleInstanceAdministrator(request))) {
           //make content item pending
        contentItem.setPublished(false);
        contentItem.setRejected(false);
      }

      DbUtil.createCMSContentItem(contentItem);

      if (cms.isModerated() &&
          (!DgUtil.isModuleInstanceAdministrator(request))) {

        String link = request.getScheme() + "://" +
            request.getServerName() + ":" +
            new Long(request.getServerPort()).toString() +
            SiteConfigUtils.buildDgURL(request, false) +
            SiteConfigUtils.getCurrentModuleURL(request) +
            "/viewContentItems.do~viewMode=pending";

        CMSEmailManager.sendAdminEmail(moduleInstance,
                                          contentItem.getTitle(),
                                          contentItem.getUrl(),
                                          contentItem.getFileName(),
                                          contentItem.getDescription(),
                                          contentItem.getCategories(),
                                          link);

      }


      contItemForm.setNoReset(false);
    }

//   return mapping.findForward("createContentItem");

    long categoryId = contItemForm.getCategoryId();
    if (categoryId == 0 && contItemForm.getCategoryIdList() != null &&
        contItemForm.getCategoryIdList().size() != 0) {
      Long catId = new Long((String)contItemForm.getCategoryIdList().get(0));
      categoryId = catId.longValue();
    }

    ActionForward retFwd = mapping.findForward("createContentItem");
    StringBuffer path = new StringBuffer(mapping.findForward("createContentItem").
                                         getPath());
    if (categoryId != 0) {
      path.append("?categoryId=");
      path.append(categoryId);
    }
    retFwd = new ActionForward(path.toString(), true);

    return retFwd;

  }

}
