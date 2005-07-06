/*
 *   AddParentCategory.java
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
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.DbUtil;

public class AddParentCategory
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {

    String forward = "showCategoryDetails";
    ActionErrors errors = new ActionErrors();
    CMSForm cmsForm = (CMSForm) form;


    long categoryId = cmsForm.getCategoryId();
    long parenCategoryId = cmsForm.getParentCategoryId();
    CMSCategory category = null;

    try {
      category = DbUtil.getCategoryItem(categoryId);
      if (parenCategoryId > 0) {
         CMSCategory newParentCategory = DbUtil.getCategoryItem(parenCategoryId);

         String[] parIds = cmsForm.getPropertyArray();
         String[] parNames = cmsForm.getNameArray();

         boolean contains = false;
         for (int arrayIndex = 0;
              arrayIndex < parIds.length;
              arrayIndex ++) {
           if (parIds[arrayIndex].equals(String.valueOf(newParentCategory.getId()))) {
             contains = true;
             break;
           }
         }

         if (!contains) {
           String[] updatedParIds = new String[parIds.length + 1];
           String[] updatedParNames = new String[parIds.length + 1];
           for (int arrayIndex = 0;
                arrayIndex < parIds.length;
                arrayIndex ++) {
             updatedParIds[arrayIndex] = parIds[arrayIndex];
             updatedParNames[arrayIndex] = parNames[arrayIndex];
             }
             updatedParIds[parIds.length] = String.valueOf(newParentCategory.getId());
             updatedParNames[parIds.length] = newParentCategory.getName();

             cmsForm.setPropertyArray(updatedParIds);
             cmsForm.setNameArray(updatedParNames);
           } else {
             cmsForm.setPropertyArray(parIds);
             cmsForm.setNameArray(parNames);
           }

         }
      cmsForm.setCategory(category);
      cmsForm.setCategoryProperties(category);
    }
    catch (Exception ex) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.addCategory"));
    }

    return mapping.findForward(forward);
  }

}