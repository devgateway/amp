/*
 *   AddRelatedCategory.java
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
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.DbUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class AddRelatedCategory extends Action {

  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

      String forward = "showCategoryDetails";
      CMSForm cmsForm = (CMSForm) form;
      ActionErrors errors = new ActionErrors();

      long categoryId = cmsForm.getCategoryId();
      long parenCategoryId = cmsForm.getParentCategoryId();
      CMSCategory category = null;

      try {
        category = DbUtil.getCategoryItem(categoryId);
        if (parenCategoryId > 0) {
           CMSCategory newRelatedCategory = DbUtil.getCategoryItem(parenCategoryId);

           String[] parIds = cmsForm.getRelatedIdArray();
           String[] parNames = cmsForm.getRelatedNameArray();


           boolean contains = false;
           for (int arrayIndex = 0;
                arrayIndex < parIds.length;
                arrayIndex ++) {
             if (parIds[arrayIndex].equals(String.valueOf(newRelatedCategory.getId()))) {
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
               updatedParIds[parIds.length] = String.valueOf(newRelatedCategory.getId());
               updatedParNames[parIds.length] = newRelatedCategory.getName();

               cmsForm.setRelatedIdArray(updatedParIds);
               cmsForm.setRelatedNameArray(updatedParNames);
             } else {
               cmsForm.setRelatedIdArray(parIds);
               cmsForm.setRelatedNameArray(parNames);
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