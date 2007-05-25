/*
 *   ShowSelectCategory.java
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

import java.util.List;
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
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.util.CMSManager;
import org.digijava.module.cms.util.DbUtil;

public class ShowSelectCategory extends Action {
  public ActionForward execute(ActionMapping mapping,
                             ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) {
  CMSForm cmsForm = (CMSForm) form;
  String forward = "showSelectCategory";
  ActionErrors errors = new ActionErrors();
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
  CMS cms = null;
  try {
    cms = DbUtil.getCMSItem(siteId, instanceId);
  }
  catch (CMSException ex) {
    errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.gettingCMSObject"));
  }

  //Get cms
  List childCategories = DbUtil.getChildCategories(null, cms.getId());

  String treeAsXml = CMSManager.getCategoryTreeAsXml(childCategories);
  cmsForm.setCategoryTreeXml(treeAsXml);
  cmsForm.setCategoryList(childCategories);

  return mapping.findForward(forward);
}

}