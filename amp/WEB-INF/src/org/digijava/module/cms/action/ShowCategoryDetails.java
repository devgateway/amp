/*
 *   ShowCategoryDetails.java
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

public class ShowCategoryDetails
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    ActionErrors errors = new ActionErrors();
    String forward = "showCategoryDetails";
    CMSForm cmsForm = (CMSForm) form;
    long categoryId = cmsForm.getCategoryId();
    CMSCategory category = null;

    try {
      category = DbUtil.getCategoryItem(categoryId);
      cmsForm.setCategory(category);
      cmsForm.setCategoryProperties(category);

      List parentList = new ArrayList();

      if (category.getPrimaryParent() != null) {
        cmsForm.setParentCategoryId(category.getPrimaryParent().getId());
      }
    }
    catch (Exception ex) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.showingCtegoryDetails"));
    }


    return mapping.findForward(forward);
  }
}