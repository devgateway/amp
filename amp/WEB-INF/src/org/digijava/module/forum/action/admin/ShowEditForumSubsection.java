/*
 *   ShowEditForumSubsection.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: Mar 15, 2004
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


public class ShowEditForumSubsection extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response ) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "subsectionDetails";
        ActionErrors errors = new ActionErrors();
        long subsectionId = adminPageForm.getSubsectionId();
        ForumSubsection subsection = null;
        try {
            subsection =
                DbUtil.getSubsectionItem(subsectionId);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingSubsection"));
        }

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);
        adminPageForm.setForum(forum);


        adminPageForm.setSubsectionParams(subsection);
        return mapping.findForward(forward);
    }
}