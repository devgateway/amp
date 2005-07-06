/*
 *   BrowseCategories.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 10,2004
 * 	 CVS-ID: $Id: BrowseCategories.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.CMSManager;
import org.digijava.module.cms.util.DbUtil;
import javax.security.auth.Subject;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.kernel.util.DgUtil;
//
public class BrowseCategories
    extends PaginationAction {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {
    String forward="browseCategories";
    CMSForm cmsForm = (CMSForm) form;

    ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
        request);

    String refUrl = RequestUtils.getSourceURL(request);
    cmsForm.setRefPageUrl(refUrl);

    List dbCategoryList = null;
    List dbSubCategoryList = new ArrayList();

    List categoryList = new ArrayList();
    List subCategoryList = new ArrayList();

    List dbContentItemList = null;

    //get cms for current site and instance
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
    CMS cms = DbUtil.getCMSItem(siteId, instanceId);

    Subject subject = DgSecurityManager.getSubject(request);

    //get categories list
    if (cmsForm.getCategoryId() == 0) {
      dbCategoryList = DbUtil.getChildCategories(null, cms.getId());
    } else {
      CMSCategory category = DbUtil.getCategoryItem(cmsForm.getCategoryId());

      cmsForm.setBradcrampItems(CMSManager.getCategoryBradcrump(category));

      Set dbChildCategoryList = category.getChildCategories();
      Set dbPrimaryCategoryList = category.getPrimaryChildCategories();
      dbCategoryList = new ArrayList();

      if (dbChildCategoryList != null) {
        dbCategoryList.addAll(dbChildCategoryList);
      }
      if (dbPrimaryCategoryList != null) {
        dbCategoryList.addAll(dbPrimaryCategoryList);
      }

      cmsForm.setCategoryName(category.getName());
      cmsForm.setCategoryId(category.getId());

      doStartPagination(cmsForm, cmsForm.getItemsPerPage());

      dbContentItemList = DbUtil.getContentItems(category.getId(),cms.getId(), getOffset(), cmsForm.getItemsPerPage() + 1);


      if (dbContentItemList != null && dbContentItemList.size() > 0) {

          int n;
          if ( (cmsForm.getItemsPerPage() + 1) == dbContentItemList.size())
              n = dbContentItemList.size() - 1;
          else {
              n = dbContentItemList.size();
          }

          cmsForm.setContentItemList(dbContentItemList.subList(0, n));

      } else {
        cmsForm.setContentItemList(null);
      }

      forward = "browseSubCategories";

      endPagination((dbContentItemList != null) ? dbContentItemList.size() : 0);

    }


    //
    if (dbCategoryList != null && dbCategoryList.size() != 0) {
      CMSManager.sortCategoryList(dbCategoryList);
      //
      Iterator iter = dbCategoryList.iterator();
      while (iter.hasNext()) {

        CMSCategory item = (CMSCategory) iter.next();
        item.setSubCategories(new ArrayList());

        Set dbChildCategoryList = item.getChildCategories();
        Set dbPrimaryCategoryList = item.getPrimaryChildCategories();

        if (dbChildCategoryList != null) {
          dbSubCategoryList.addAll(dbChildCategoryList);
        }
        if (dbPrimaryCategoryList != null) {
          dbSubCategoryList.addAll(dbPrimaryCategoryList);
        }
        CMSManager.sortCategoryList(dbSubCategoryList);

        int n = 3;
        for (int i = 0; i < n && i < dbSubCategoryList.size(); i++) {
          CMSCategory subItem = (CMSCategory) dbSubCategoryList.get(i);
          subCategoryList.add(subItem);
        }

        if (subCategoryList.size() != 0) {
          item.getSubCategories().addAll(subCategoryList);
        }
        else {
          item.setSubCategories(null);
        }
        categoryList.add(item);

        dbSubCategoryList.clear();
        subCategoryList.clear();

      }
    }

    if (categoryList.size() != 0) {
      cmsForm.setCategoryList(categoryList);
    }
    else {
      cmsForm.setCategoryList(null);
    }


    return mapping.findForward(forward);
  }
}
