/*
 *   AddCategory.java
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.util.DbUtil;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;



public class AddCategory extends Action {
  private static Logger logger = Logger.getLogger(AddCategory.class);
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {

    String forward = "showCategoryDetails";
    CMSForm cmsForm = (CMSForm) form;
    cmsForm.setProcessingMode(CMSForm.MODE_NEW);
    ActionErrors errors = new ActionErrors();

    long categoryId = cmsForm.getCategoryId();
    cmsForm.setCategoryId(0);
    cmsForm.setParentCategoryId(0);
    CMSCategory category = null;

    try {
      category = DbUtil.getCategoryItem(categoryId);
      cmsForm.setPrimaryParentCategory(category);
      String[] name = new String[1];
      name[0] = category.getName();
      String[] id = new String[1];
      id[0] = String.valueOf(category.getId());

      cmsForm.setPropertyArray(id);
      cmsForm.setNameArray(name);
      cmsForm.setPrimaryParentCategoryId(category.getId());
    }
    catch (Exception ex) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.addCategory"));
    }


    return mapping.findForward(forward);
  }

}