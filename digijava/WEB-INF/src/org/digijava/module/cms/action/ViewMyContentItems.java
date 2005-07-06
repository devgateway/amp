/*
 *   ViewMyContentItems.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: May 06,2004
 * 	 CVS-ID: $Id: ViewMyContentItems.java,v 1.1 2005-07-06 10:34:09 rahul Exp $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.module.cms.util.CmsTabItem;
import org.digijava.module.cms.util.DbUtil;
import org.digijava.module.common.action.PaginationAction;

/**
 * Action Previews the news before publcation
 */

public class ViewMyContentItems
    extends PaginationAction {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CMSContentItemForm contItemForm = (CMSContentItemForm) form;
    List dbItemsList = null;
    List itemsList = new ArrayList();
    User user = RequestUtils.getUser(request);

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
    //

    //Prepare tabs (temporary code)
    List tabsList = new ArrayList();
    CmsTabItem all = new CmsTabItem("All", CMSContentItemForm.VIEW_MODE_ALL);
    tabsList.add(all);
    CmsTabItem published = new CmsTabItem("Published",
                                          CMSContentItemForm.VIEW_MODE_PUBLISHED);
    tabsList.add(published);
    CmsTabItem pending = new CmsTabItem("pending",
                                        CMSContentItemForm.VIEW_MODE_PENDING);
    tabsList.add(pending);
    CmsTabItem rejected = new CmsTabItem("rejected",
                                         CMSContentItemForm.VIEW_MODE_REJECTED);
    tabsList.add(rejected);
    if (contItemForm.getViewMode() == null) {
      contItemForm.setViewMode(CMSContentItemForm.VIEW_MODE_ALL);
      all.setActive(true);
    }
    else if (contItemForm.getViewMode().equals(CMSContentItemForm.VIEW_MODE_ALL)) {
      all.setActive(true);
    }
    else if (contItemForm.getViewMode().equals(CMSContentItemForm.
                                               VIEW_MODE_PUBLISHED)) {
      published.setActive(true);
    }
    else if (contItemForm.getViewMode().equals(CMSContentItemForm.
                                               VIEW_MODE_PENDING)) {
      pending.setActive(true);
    }
    else if (contItemForm.getViewMode().equals(CMSContentItemForm.
                                               VIEW_MODE_REJECTED)) {
      rejected.setActive(true);
    }
    else {
      contItemForm.setViewMode(CMSContentItemForm.VIEW_MODE_ALL);
      all.setActive(true);
    }
    contItemForm.setTabs(tabsList);

    if (user != null) {
      doStartPagination(contItemForm, contItemForm.getItemsPerPage());

      dbItemsList = DbUtil.getContentItems(cms.getId(),
                                           contItemForm.getViewMode(),
                                           user, getOffset(),
                                           contItemForm.getItemsPerPage() + 1);

      endPagination( (dbItemsList != null) ? dbItemsList.size() : 0);

      if (dbItemsList != null && dbItemsList.size() > 0) {

        int n;
        if ( (contItemForm.getItemsPerPage() + 1) == dbItemsList.size())
          n = dbItemsList.size() - 1;
        else {
          n = dbItemsList.size();
        }

        for (int i = 0; i < n; i++) {
          CMSContentItem item = (CMSContentItem) dbItemsList.get(i);
          CMSContentItemForm.CMSContentItemInfo ci = new
              CMSContentItemForm.CMSContentItemInfo();

          ci.setTitle(item.getTitle());
          ci.setUrl(item.getUrl());
          ci.setFileName(item.getFileName());
          ci.setId(new Long(item.getId()));
          ci.setCreationDate(item.getSubmissionDate());

          User authorUser = item.getAuthorUser();
          ci.setAuthorUser(authorUser);

          ci.setEditable(false);
          if (DgUtil.isModuleInstanceAdministrator(request)) {
            ci.setEditable(true);
          }
          else {

            if (!item.isPublished() && !item.isRejected() &&
                authorUser.getEmail().equals(user.getEmail())) {
              ci.setEditable(true);
            }
          }

          itemsList.add(ci);
        }
        if (itemsList.size() != 0) {
          contItemForm.setItemsList(itemsList);
        }
        else {
          contItemForm.setItemsList(null);
        }
      }
      else {
        contItemForm.setItemsList(null);
      }
    }

    return mapping.findForward("viewMyContentItems");
  }

}