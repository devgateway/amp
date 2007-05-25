/*
 *   SaveUserSettings.java
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

package org.digijava.module.forum.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.ForumManager;
import java.util.Map;
import org.digijava.module.forum.util.ForumConstants;
import java.util.HashMap;
import org.digijava.kernel.user.User;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.dbentity.Forum;
import java.util.Date;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;

public class SaveUserSettings
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showForum";

        if (forumUser != null) { //Update existing user settings
            forumPageForm.fillUserSetting(forumUser);
            try {
                DbUtil.updateForumUser(forumUser);
            }
            catch (Exception ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.updateUser"));
            }
        }
        return mapping.findForward(forward);

    }

/*
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showForum";

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);

        //Get logged user settings
        User loggedUser = RequestUtils.getUser(request);
        ForumUserSettings fUser = null;
        try {
            fUser = ForumManager.getForumUser(loggedUser,
                                              forum.getId());
        }
        catch (Exception ex1) {
        }
        if (fUser != null) { //Update existing user settings
            forumPageForm.fillUserSetting(fUser);
            try {
                DbUtil.updateForumUser(fUser);
            }
            catch (Exception ex) {
            }
        }
        else { //Create new user
            fUser = new ForumUserSettings(loggedUser.getId().longValue(),
                                          forumPageForm.getNickName());
            fUser.setAvatarUrl(forumPageForm.getAvatarUrl());
            fUser.setForumId(forum.getId());
            fUser.setRegisterDate(new Date());
            try {
                DbUtil.createForumUser(fUser);
            }
            catch (Exception ex) {
            }

        }
        return mapping.findForward(forward);
    }*/
}