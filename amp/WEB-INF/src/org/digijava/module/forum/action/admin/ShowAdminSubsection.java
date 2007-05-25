/*
 *   ShowAdminSubsection.java
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
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.util.DbUtil;
import java.util.List;
import java.util.Map;
import org.digijava.module.forum.util.ForumConstants;
import java.util.HashMap;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.Iterator;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

public class ShowAdminSubsection
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "showAdminSubsection";
        ActionErrors errors = new ActionErrors();
        Map visitedTopics = (Map) request.getSession().
            getAttribute(ForumConstants.VISITED_SESSION_MAP);
        if (visitedTopics == null) {
            visitedTopics = new HashMap();
            request.getSession().setAttribute(ForumConstants.
                                              VISITED_SESSION_MAP,
                                              visitedTopics);
        }

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);


        User loggedUser = RequestUtils.getUser(request);
        ForumUserSettings fUser = null;

        try {
            fUser = ForumManager.getForumUser(loggedUser,
                                              forum.getId());
        }
        catch (Exception ex1) {
        }
        adminPageForm.setVisitedThreadsMap(visitedTopics);
        adminPageForm.setForumUserSettings(fUser);

        ForumSubsection forumSubsection = null;
        try {
            forumSubsection =
                DbUtil.getSubsectionItem(adminPageForm.getSubsectionId());
        }
        catch (ForumException ex2) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingSubsections"));
        }

        adminPageForm.setSize(forumSubsection.getThreadCount());
        adminPageForm.setItemPerPage(forum.getTopicsPerPage());

        List threadList = null;
        try {
            threadList = DbUtil.getThreads(new Long(forumSubsection.getId()),
                                                adminPageForm.getStartForm(),
                                                adminPageForm.getItemPerPage());
        }
        catch (Exception ex) {
        }

        //Set "has new post" flags to threads
        if (fUser!=null && threadList !=null && !threadList.isEmpty()) {
            Iterator it = threadList.iterator();
            while (it.hasNext()) {
                ForumThread thread = (ForumThread) it.next();
                if (thread.getParentThread() == null) {
                    thread.setHasNewPost(ForumManager.isNewPostInThread(thread,
                        fUser, visitedTopics));
                }
            }
        }

        //Trail generation
        Map callbackMap = ForumManager.getAdminPageTrailCallbackMap();
        if (forumSubsection != null) {
            adminPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
                forumSubsection, callbackMap));
        }


        adminPageForm.populatePaginationItems();
        adminPageForm.setThreadList(threadList);
        adminPageForm.setForumSubsection(forumSubsection);
        return mapping.findForward(forward);
    }

}