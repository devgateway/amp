/*
 *   ShowChangeItemStatus.java
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;
import org.apache.struts.action.ActionError;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.common.dbentity.ItemStatus;

public class ShowChangeItemStatus
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    CMSContentItemForm contItemForm = (CMSContentItemForm) form;

    //translation
    ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

    String moduleName = moduleInstance.getModuleName();
    String siteName = moduleInstance.getSite().getName();
    String instanceName = moduleInstance.getInstanceName();

    RequestUtils.setTranslationAttribute(request, "moduleName", moduleName);
    RequestUtils.setTranslationAttribute(request, "siteName", siteName);

    contItemForm.setModuleName(moduleName);
    contItemForm.setSiteName(siteName);
    contItemForm.setInstanceName(instanceName);

    ActionErrors errors = new ActionErrors();

    String forward = "confirm";

    List itemsList = new ArrayList();
    String[] selItemIds = contItemForm.getCheckboxArray();
    if (selItemIds != null && selItemIds.length > 0) {
      for (int index = 0; index < selItemIds.length; index++) {
        Long itemId = new Long(selItemIds[index]);
        try {
          CMSContentItem item = DbUtil.getCMSContentItem(itemId);
          itemsList.add(item);
        }
        catch (CMSException ex) {
          errors.add(ActionErrors.GLOBAL_ERROR,
                     new ActionError("error.cms.getContentItem"));
        }

      }
    }
    contItemForm.setItemsList(itemsList);

    CMS cms = DbUtil.getCMSItem(moduleInstance.getSite().getSiteId(),
                                instanceName);

    if (contItemForm.getItemStatus().equals(ItemStatus.PUBLISHED)) {
      contItemForm.setSendMessage(cms.isSendApproveMsg());
    }else
    if (contItemForm.getItemStatus().equals(ItemStatus.REJECTED)) {
      contItemForm.setSendMessage(cms.isSendRejectMsg());
    }else
    if (contItemForm.getItemStatus().equals(ItemStatus.REVOKE)) {
      contItemForm.setSendMessage(cms.isSendRevokeMsg());
    }

    return mapping.findForward(forward);
  }
}