/*
 *   ShowEditContentItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 06,2004
 * 	 CVS-ID: $Id: ShowEditContentItem.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import javax.security.auth.Subject;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowEditContentItem
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    contItemForm.setProcessingMode(CMSContentItemForm.MODE_EDIT);

    CMS cms = null;
    CMSContentItem contentItem = null;

    User user = DgUtil.getCurrentUser(request);

    //get languges list
    Set languages = SiteUtils.getUserLanguages(RequestUtils.getSite(request));
    //
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
    contItemForm.setLanguages(sortedLanguages);

    //get countries list
    Collection countries = TrnUtil.getCountries(RequestUtils.
                                                getNavigationLanguage(request).
                                                getCode());
    List sortedCountries = new ArrayList(countries);
    Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
    contItemForm.setCountries(sortedCountries);
    //

    if (request.getParameter("reset") == null) {
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
          Subject subject = DgSecurityManager.getSubject(request);
          boolean isAdmin = DgSecurityManager.permitted(subject,
                                                            moduleInstance.getSite(),
                                                            moduleInstance,
                                                            ResourcePermission.INT_ADMIN);

          if (!isAdmin && (!user.getEmail().equals(contentItem.getAuthorUser().getEmail()) ||
              contentItem.isPublished() || contentItem.isRejected())) {
            return new ActionForward("/cms/viewMyContentItems.do", true);
          }

          contItemForm.setTitle(contentItem.getTitle());
          contItemForm.setDescription(contentItem.getDescription());
          if (contentItem.getUrl() != null) {
            contItemForm.setUrl(contentItem.getUrl());
          }
          else {
            contItemForm.setUrl(request.getScheme() + "://");
          }
          //
          if (contentItem.getFile() != null) {
            contItemForm.setFile(contentItem.getFile());
            contItemForm.setFileName(contentItem.getFileName());
            contItemForm.setContentType(contentItem.getContentType());
          }
          else {
            contItemForm.setFile(null);
            contItemForm.setFileName(null);
            contItemForm.setContentType(null);
          }
          //
          contItemForm.setLanguage(contentItem.getLanguage().getCode());
          contItemForm.setCountry(contentItem.getCountry().getIso());

        }
      }

      //Set owner category list
      List catIdList = new ArrayList();
      List catNameList = new ArrayList();
      if (contentItem.getCategories() != null) {
        Iterator it = contentItem.getCategories().iterator();
        while (it.hasNext()) {
          CMSCategory cat = (CMSCategory) it.next();
          catIdList.add(String.valueOf(cat.getId()));
          catNameList.add(cat.getName());
        }
      }
      contItemForm.setCategoryIdList(catIdList);
      contItemForm.setCategoryNameList(catNameList);

      contItemForm.setNoReset(true);
    }

    return mapping.findForward("showEditContentItem");
  }

}