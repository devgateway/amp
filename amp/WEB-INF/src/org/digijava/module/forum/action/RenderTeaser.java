/*
 *   RenderTeaser.java
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

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumForm;
import org.digijava.module.forum.util.ForumManager;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class RenderTeaser
    extends TilesAction {

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException,
        ServletException {
        ForumForm forumForm = (ForumForm) form;
        // Get forum for the current site and instance
        ActionErrors errors = new ActionErrors();
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForumForTeaser(moduleInstance);
        if (forum != null) {
            forumForm.setForum(forum);
            User loggedUser = RequestUtils.getUser(request);
            ForumUserSettings fUserRecord = null;
            Date lastActive = null;
            if (loggedUser != null) {
                try {
                    fUserRecord = ForumManager.getForumUser(
                        loggedUser, forum.getId());
                }
                catch (Exception ex) {
                    errors.add("forumGlobalError",
                           new ActionError("error.forum.teaser"));
                }
                if (fUserRecord != null) {
                    lastActive = fUserRecord.getLastActiveTime();
                }
            }

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }
/*
            forumForm.setTotalThreads(ForumManager.getTotalThreadCount(forum.getId()));
            forumForm.setNewThreads(ForumManager.getNewThreadCount(forum.getId(), lastActive));
            forumForm.setTotalPosts(ForumManager.getTotalPostCount(forum.getId()));
            forumForm.setNewPosts(ForumManager.getNewPostCount(forum.getId(), lastActive));*/
            forumForm.setLastPosts(ForumManager.getLastPosts(forum.getId(), 3));
        }
        return null;
    }
}