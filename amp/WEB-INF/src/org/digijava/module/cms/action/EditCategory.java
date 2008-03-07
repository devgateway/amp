/*
 *   EditCategory.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class EditCategory extends Action {
  public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
      String forward = "showCategoryDetails";
      ActionErrors errors = new ActionErrors();
      CMSForm cmsForm = (CMSForm) form;
      cmsForm.setProcessingMode(CMSForm.MODE_EDIT);

      long categoryId = cmsForm.getCategoryId();
      CMSCategory category = null;


      try {
        category = DbUtil.getCategoryItem(categoryId);
        cmsForm.setCategory(category);
        cmsForm.setCategoryProperties(category);

        if (category.getPrimaryParent() != null) {
          cmsForm.setPrimaryParentCategoryId(category.getPrimaryParent().getId());
        }

        //Set parent categories
        List parentList = new ArrayList();
        parentList.addAll(category.getParentCategories());
        if (category.getPrimaryParent() != null) {
          cmsForm.setParentCategoryId(category.getPrimaryParent().getId());
          parentList.add(category.getPrimaryParent());
        }
        cmsForm.setCategoryList(parentList);

        String parCategoryIds[] = new String[parentList.size()];
        String parCategoryNames[] = new String[parentList.size()];

        Iterator it = parentList.iterator();
        for (int catIndex=0; it.hasNext(); catIndex ++) {
          CMSCategory parCategory = (CMSCategory) it.next();
          parCategoryIds[catIndex] = String.valueOf(parCategory.getId());
          parCategoryNames[catIndex] = parCategory.getName();

        }
        cmsForm.setPropertyArray(parCategoryIds);
        cmsForm.setNameArray(parCategoryNames);

        //Set related categories
        String[] relIds = new String[category.getRelatedCategories().size()];
        String[] relNames = new String[category.getRelatedCategories().size()];
        Iterator relIt = category.getRelatedCategories().iterator();
        for (int relIndex = 0;
             relIt.hasNext();
             relIndex ++) {
          CMSCategory relCat = (CMSCategory) relIt.next();
          relIds[relIndex] = String.valueOf(relCat.getId());
          relNames[relIndex] = relCat.getName();
        }

        cmsForm.setRelatedIdArray(relIds);
        cmsForm.setRelatedNameArray(relNames);
      }
      catch (Exception ex) {
        errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.editCategory"));
      }
      return mapping.findForward(forward);
    }
}