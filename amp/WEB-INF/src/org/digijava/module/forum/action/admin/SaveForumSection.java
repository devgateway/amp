/*
 *   SaveForumSection.java
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.form.AdminPageForm;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveForumSection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        AdminPageForm adminPageForm = (AdminPageForm) form;
        //String forward = "index";
        String forward = "showAdminIndex";
        ActionErrors errors = new ActionErrors();

        long sectionId = adminPageForm.getSectionId();

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);

        if (sectionId == 0) { //Create new section
            ForumSection section = new ForumSection(adminPageForm.
                getSectionTitle(),
                adminPageForm.getSectionComment());
            User loggedUser = RequestUtils.getUser(request);
            ForumUserSettings fUser = null;
            try {
                fUser = ForumManager.getForumUser(loggedUser,
                                                  forum.getId());
            }
            catch (Exception ex1) {
            }
            section.setOrderIndex(forum.getSections().size());
            section.setForum(forum);
            forum.getSections().add(section);
            try {
                DbUtil.updateForum(forum);
            }
            catch (Exception ex) {
            }
        }
        else {
            ForumSection section = null;
            try {
                section = DbUtil.getSectionItem(sectionId);
            }
            catch (ForumException ex3) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSection"));
            }
            adminPageForm.fillSection(section);
            try {
                DbUtil.updateSection(section);
            }
            catch (Exception ex2) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingSection"));
            }
        }
        return mapping.findForward(forward);
    }
}