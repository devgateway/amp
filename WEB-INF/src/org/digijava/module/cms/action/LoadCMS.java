/*
 *   LoadCMS.java
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

import javax.security.auth.Subject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.util.DbUtil;

public class LoadCMS
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) {
    String forward = "browseCategories";
    ActionErrors errors = new ActionErrors();

//get cms for current site and instance
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
      if (cms == null) {

        Subject subject = DgSecurityManager.getSubject(request);
        boolean isAdmin = DgSecurityManager.permitted(subject,
            moduleInstance.getSite(),
            moduleInstance,
            ResourcePermission.INT_ADMIN);

        CMS newCms = new CMS();
        newCms.setSiteId(siteId);
        newCms.setInstanceId(instanceId);
        DbUtil.createCMS(newCms);
        if (isAdmin) {
          forward = "showCMSSettings";
        }
        return mapping.findForward(forward);
      }
    }
    catch (CMSException ex1) {
      errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.gettingCMSObject"));
    }
    return mapping.findForward(forward);
  }
}