/*
 *   RenderTeaser.java
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

import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.cms.form.CMSForm;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.cms.util.DbUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.util.List;
import java.util.Iterator;
import org.digijava.module.cms.dbentity.CMSCategory;
import java.util.ArrayList;
import org.digijava.module.cms.util.CMSManager;

public class RenderTeaser
    extends TilesAction {
    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException,
        ServletException {
      CMSForm cmsForm = (CMSForm) form;
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
      if (cms != null) {
        List childCategories = DbUtil.getChildCategories(null, cms.getId());
        if (childCategories != null && childCategories.size() > 0) {
          Iterator it = childCategories.iterator();
          while (it.hasNext()) {
            CMSCategory category = (CMSCategory) it.next();
            List children = new ArrayList(category.getPrimaryChildCategories());
            children.addAll(category.getChildCategories());
            CMSManager.sortCategoryList(children);
            category.setSubCategories(children);
          }
        }
        cmsForm.setCategoryList(childCategories);
      } else {
        cmsForm.setCategoryList(new ArrayList());
      }



        return null;
    }

}