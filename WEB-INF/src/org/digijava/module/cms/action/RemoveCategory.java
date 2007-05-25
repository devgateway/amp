/*
 *   RemoveCategory.java
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
import java.util.List;
import java.util.ArrayList;
import org.digijava.module.cms.util.CMSManager;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class RemoveCategory extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    ActionErrors errors = new ActionErrors();
    String forward = "showCategoryDetails";
    CMSForm cmsForm = (CMSForm) form;
    long remCategoryId = cmsForm.getRemoveCategoryId();

    String[] selCategoryIds = null;
    String[] selCategoryNames = null;

    List newIds = new ArrayList();
    List newNames = new ArrayList();

    if (cmsForm.getSelType() == CMSForm.SELECT_CAT_FOR_PARENT) {
      selCategoryIds = cmsForm.getPropertyArray();
      selCategoryNames = cmsForm.getNameArray();
      if (selCategoryIds != null && selCategoryIds.length > 0) {
        for (int index = 0; index < selCategoryIds.length; index ++){
          if (!selCategoryIds[index].equals(String.valueOf (remCategoryId))) {
            newIds.add(selCategoryIds[index]);
            newNames.add(selCategoryNames[index]);
          }
        }
        if (remCategoryId == cmsForm.getPrimaryParentCategoryId()) {
            if (newIds.size() > 0) {
              cmsForm.setPrimaryParentCategoryId(Long.parseLong((String)
                  newIds.get(newIds.size()-1)));
            } else {
              cmsForm.setPrimaryParentCategoryId(0);
            }
        }
      }
      cmsForm.setPropertyArray(CMSManager.getListAsStringArray(newIds));
      cmsForm.setNameArray(CMSManager.getListAsStringArray(newNames));
    } else if (cmsForm.getSelType() == CMSForm.SELECT_CAT_FOR_RELATED) {
      selCategoryIds = cmsForm.getRelatedIdArray();
      selCategoryNames = cmsForm.getRelatedNameArray();
      if (selCategoryIds != null && selCategoryIds.length > 0) {
        for (int index = 0; index < selCategoryIds.length; index ++){
          if (!selCategoryIds[index].equals(String.valueOf (remCategoryId))) {
            newIds.add(selCategoryIds[index]);
            newNames.add(selCategoryNames[index]);
          }
        }
      }
      cmsForm.setRelatedIdArray(CMSManager.getListAsStringArray(newIds));
      cmsForm.setRelatedNameArray(CMSManager.getListAsStringArray(newNames));

    }

    return mapping.findForward(forward);
  }
}