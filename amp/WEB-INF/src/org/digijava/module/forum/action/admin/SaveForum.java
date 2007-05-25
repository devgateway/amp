/*
 *   SaveForum.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created:
 *   CVS-ID: $Id$
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

package org.digijava.module.forum.action.admin;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumManager;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

public class SaveForum
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String forward = "showAdminIndex";
        ActionErrors errors = new ActionErrors();
        AdminPageForm adminPageForm = (AdminPageForm) form;

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);

        if (forum == null) {
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

            Forum newForum = new Forum(siteId, instanceId);
            adminPageForm.fillForum(newForum);
            try {
                DbUtil.createForum(newForum);
            }
            catch (Exception ex) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingForum"));
            }
        } else {
            adminPageForm.fillForum(forum);
            try {
                DbUtil.updateForum(forum);
            }
            catch (ForumException ex1) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingForum"));
            }
        }
        return mapping.findForward(forward);
    }
}