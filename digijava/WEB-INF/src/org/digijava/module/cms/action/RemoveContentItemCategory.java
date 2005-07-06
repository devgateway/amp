/*
 *   RemoveContentItemCategory.java
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
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.dbentity.CMSContentItem;
import java.util.Iterator;


public class RemoveContentItemCategory extends Action {
  public ActionForward execute(ActionMapping mapping,
                             ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) {
  String forward = "showContentItem";
  CMSContentItemForm contentItemForm = (CMSContentItemForm) form;
  long categoryId = contentItemForm.getParentCategoryId();

  for (int index = 0;
       index < contentItemForm.getCategoryIdList().size();
       index ++ ) {
    if (contentItemForm.getCategoryIdList().
        get(index).equals(String.valueOf(categoryId))) {
      contentItemForm.getCategoryIdList().remove(index);
      contentItemForm.getCategoryNameList().remove(index);
    }
  }

  return mapping.findForward(forward);
  }
}