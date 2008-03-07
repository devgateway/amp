/*
 *   AddContentItemCategory.java
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
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;

public class AddContentItemCategory
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    String forward = "";
    ActionErrors errors = new ActionErrors();
    CMSContentItemForm contentItemForm = (CMSContentItemForm) form;

    if (contentItemForm.getProcessingMode() == CMSContentItemForm.MODE_NEW) {
      forward = "showCreateContentItem";
    } else {
      forward = "showEditContentItem";
    }



    long categoryId = contentItemForm.getParentCategoryId();

    if (categoryId > 0) {
      CMSCategory category = null;

      try {
        category = DbUtil.
            getCategoryItem(categoryId);

      }
      catch (Exception ex) {
        errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.addCategory"));
      }


        boolean contains = false;
        for (int index = 0;
             index < contentItemForm.getCategoryIdList().size();
             index++) {
          if ( ( (String) contentItemForm.getCategoryIdList().get(index)).
              equals(String.valueOf(categoryId))) {
            contains = true;
            break;
          }
        }

        if (!contains) {
          contentItemForm.getCategoryIdList().add(String.valueOf(categoryId));
          contentItemForm.getCategoryNameList().add(category.getName());
        }
      }


    return mapping.findForward(forward);
  }
}