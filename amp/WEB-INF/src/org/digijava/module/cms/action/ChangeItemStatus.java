/*
 *   ChangeItemStatus.java
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.kernel.user.User;
import org.digijava.module.cms.util.CMSEmailManager;
import org.digijava.kernel.util.DgUtil;

public class ChangeItemStatus
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception{

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;
    ActionErrors errors = new ActionErrors();
    String forward = "viewItems";
    String[] itemIds = contItemForm.getPropertyArray();

    ActionForward retForward = null;

    //getSourceURL
    if (contItemForm.getItemId() != null &&
        contItemForm.getItemId().longValue() > 0) {
      if (contItemForm.getRefPageUrl() != null &&
          contItemForm.getRefPageUrl().length() > 0) {
        try {
          CMSContentItem item = DbUtil.getCMSContentItem(contItemForm.getItemId());

          if (contItemForm.getItemStatus().equals(ItemStatus.PUBLISHED)) {
            item.setRejected(false);
            item.setPublished(true);
          }
          else if (contItemForm.getItemStatus().equals(ItemStatus.REJECTED)) {
            item.setRejected(true);
          }
          DbUtil.updateCMSContentItem(item);
          response.sendRedirect(contItemForm.getRefPageUrl());
        }
        catch (CMSException ex) {
          errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.changeItemStatus"));
        }
        catch (IOException ex1) {
          errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.changeItemStatus"));
        }
      } else {
        retForward = mapping.findForward(forward);
      }
    }
    else if (itemIds.length > 0) {
      try {
        for (int index = 0; index < itemIds.length; index++) {
          CMSContentItem item = DbUtil.getCMSContentItem(new Long(itemIds[index]));
          if (item != null) {
            if (contItemForm.getItemStatus().equals(ItemStatus.PUBLISHED)) {
              item.setRejected(false);
              item.setPublished(true);
            }
            else if (contItemForm.getItemStatus().equals(ItemStatus.REJECTED)) {
              item.setRejected(true);
            }
            else if (contItemForm.getItemStatus().equals(ItemStatus.PENDING)) {
              item.setRejected(false);
              item.setPublished(false);
            }
            DbUtil.updateCMSContentItem(item);


            User author = item.getAuthorUser();

            if (author != null && contItemForm.isSendMessage()) {

              CMSEmailManager.sendUserEmail(DgUtil.getRealModuleInstance(
                  request), author, item.getTitle(), item.getUrl(),item.getFileName(),
                                               contItemForm.getItemStatus());

            }

          }
        }
      }
      catch (NumberFormatException ex) {
        errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.changeItemStatus"));
      }
      catch (CMSException ex) {
        errors.add(ActionErrors.GLOBAL_ERROR,
                 new ActionError("error.cms.changeItemStatus"));
      }
      finally {
        retForward = mapping.findForward(forward);
      }
    }

    return retForward;
  }
}