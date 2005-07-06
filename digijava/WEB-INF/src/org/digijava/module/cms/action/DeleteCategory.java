/*
 *   DeleteCategory.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 7, 2004
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.CMSManager;
import org.digijava.module.cms.util.DbUtil;

public class DeleteCategory
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    CMSForm cmsForm = (CMSForm) form;
    ActionErrors errors = new ActionErrors();
    String forward = "showTaxonomyManager";

    //Get cms
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
    try {
      CMS cms = DbUtil.getCMSItem(siteId, instanceId);
    }
    catch (CMSException ex) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.gettingCMSObject"));
    }
    //Get cms

    long delCategoryId = cmsForm.getCategoryId();
    long delCategoryParentId = cmsForm.getParentCategoryId();

    CMSCategory category = null;

    try {
      category = DbUtil.getCategoryItem(delCategoryId);
//      if (category.getPrimaryParent().getId() == delCategoryParentId) {

        if (category.getPrimaryParent() != null) {
          //Delete original object
          if (category.getPrimaryParent().getId() == delCategoryParentId) {
            category.getPrimaryParent().getPrimaryChildCategories().remove(
                category);
            DbUtil.updateCategory(category.getPrimaryParent());
            DbUtil.deleteCategory(category);
          } else {//Delete sortcut
            CMSCategory parCategory = DbUtil.getCategoryItem(delCategoryParentId);
            CMSManager.removeCategoryFromSet (parCategory.getChildCategories(), category);
            DbUtil.updateCategory(parCategory);
          }
        }
        else {
          if (delCategoryParentId > 0) { //Delete shortcut
            CMSCategory parCategory = DbUtil.getCategoryItem(delCategoryParentId);
            CMSManager.removeCategoryFromSet (parCategory.getChildCategories(), category);
            DbUtil.updateCategory(parCategory);
          } else {
            DbUtil.deleteCategory(category);
          }
        }

    }
    catch (Exception ex1) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.deleteCategory"));
    }
    return mapping.findForward(forward);
  }



}