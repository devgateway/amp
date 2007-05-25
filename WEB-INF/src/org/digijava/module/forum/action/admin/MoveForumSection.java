/*
 *   MoveForumSection.java
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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.ForumConstants;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.Forum;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

public class MoveForumSection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String forward = "showAdminIndex";
        AdminPageForm adminPageForm = (AdminPageForm) form;
        ActionErrors errors = new ActionErrors();

        long sectionId = adminPageForm.getSectionId();
        ForumSection section = null;
        try {
            section = DbUtil.getSectionItem(sectionId);
        }
        catch (ForumException ex1) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.movingSectons"));
        }
        int sectionIndex = 0;
        int swapSectionIndex = section.getOrderIndex();

        switch (adminPageForm.getMoveDirection()) {
            case (ForumConstants.MOVE_UP):
                sectionIndex = swapSectionIndex - 1;
                break;
            case (ForumConstants.MOVE_DOWN):
                sectionIndex = swapSectionIndex + 1;
                break;
        }

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);

        ForumSection swapSection = DbUtil.getSectionItem(forum.getId(),
            sectionIndex);

        swapSection.setOrderIndex(swapSectionIndex);
        section.setOrderIndex(sectionIndex);
        try {
            DbUtil.updateSection(swapSection);
            DbUtil.updateSection(section);
        }
        catch (Exception ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.movingSectons"));
        }

        return mapping.findForward(forward);
    }

}