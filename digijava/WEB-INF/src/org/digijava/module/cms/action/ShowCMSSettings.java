/*
 *   ShowCMSSettings.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 13,2004
     * 	 CVS-ID: $Id: ShowCMSSettings.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.cms.form.CMSAdminForm;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowCMSSettings
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

    String forward = "showCMSSettings";

    CMSAdminForm cmsAdminForm = (CMSAdminForm) form;

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

    CMS cms = DbUtil.getCMSItem(siteId, instanceId);

    cmsAdminForm.setModerated(cms.isModerated());
    cmsAdminForm.setPrivateMode(cms.isPrivateMode());

    cmsAdminForm.setSendApproveMsg(cms.isSendApproveMsg());
    cmsAdminForm.setSendRejectMsg(cms.isSendRejectMsg());
    cmsAdminForm.setSendRevokeMsg(cms.isSendRevokeMsg());

    //translation
    String moduleName = moduleInstance.getModuleName();
    String siteName = moduleInstance.getSite().getName();
    String instanceName = moduleInstance.getInstanceName();

    RequestUtils.setTranslationAttribute(request, "moduleName", moduleName);
    RequestUtils.setTranslationAttribute(request, "siteName", siteName);

    cmsAdminForm.setModuleName(moduleName);
    cmsAdminForm.setSiteName(siteName);
    cmsAdminForm.setInstanceName(instanceName);

    cmsAdminForm.setApprove(ItemStatus.PUBLISHED);
    cmsAdminForm.setReject(ItemStatus.REJECTED);
    cmsAdminForm.setRevoke(ItemStatus.REVOKE);


    return mapping.findForward(forward);

  }

}