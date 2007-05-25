/*
*   ShowSubsection.java
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

public class ShowSubsection
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showSubsection";

        if (forumUser != null) {
            forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(forumUser));
            forumPageForm.setUnreadPmCount(DbUtil.getUnreadPrivateMsgCount(
                forumUser));
        }

        ForumSubsection forumSubsection = null;
        try {
            forumSubsection =
                DbUtil.getSubsectionItem(forumPageForm.getSubsectionId());
        }
        catch (ForumException ex1) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingSubsection"));
        }

        forumPageForm.setSize(forumSubsection.getThreadCount());
        forumPageForm.setItemPerPage(forum.getTopicsPerPage());

        List threadList = null;
        try {
            threadList = DbUtil.getThreads(new Long(forumSubsection.getId()),
                                           forumPageForm.getStartForm(),
                                           forumPageForm.getItemPerPage());
        }
        catch (Exception ex) {
        }

        //Set "has new post" flags to threads
        if (forumUser != null && threadList != null && !threadList.isEmpty()) {
            Iterator it = threadList.iterator();
            while (it.hasNext()) {
                ForumThread thread = (ForumThread) it.next();
                if (thread.getParentThread() == null) {
                    thread.setHasNewPost(ForumManager.isNewPostInThread(thread,
                        forumUser, forumPageForm.getVisitedThreadsMap()));
                }
                //Set author user
                if (thread.getAuthorUserId() != 0) {
                    ForumUserSettings author =
                        DbUtil.getForumUserItem(thread.getAuthorUserId());
                    if (author != null) {
                        thread.setAuthorUser(author);
                    }
                }
            }
        }

        //Trail generation
        Map callbackMap = ForumManager.getForumPageTrailCallbackMap();
        forumPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
            forumSubsection, callbackMap));

        forumPageForm.setThreadList(threadList);
        forumPageForm.setForumSubsection(forumSubsection);
        forumPageForm.populatePaginationItems();

        return mapping.findForward(forward);
    }

    /*
        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
            ForumPageForm forumPageForm = (ForumPageForm) form;
            String forward = "showSubsection";
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
            forumPageForm.setForum(forum);
            User loggedUser = RequestUtils.getUser(request);
            ForumUserSettings fUser = null;
            try {
                fUser = ForumManager.getForumUser(loggedUser,
                                                  forum.getId());
            }
            catch (Exception ex1) {
            }
                if (fUser != null) {
         forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(fUser));
         forumPageForm.setUnreadPmCount(DbUtil.getUnreadPrivateMsgCount(fUser));
                }
            forumPageForm.setVisitedThreadsMap(visitedTopics);
            forumPageForm.setForumUserSettings(fUser);
            forumPageForm.setIsDigiUser(loggedUser==null?false:true);
            ForumSubsection forumSubsection =
                DbUtil.getSubsectionItem(forumPageForm.getSubsectionId());
            forumPageForm.setSize(forumSubsection.getThreadCount());
            forumPageForm.setItemPerPage(forum.getTopicsPerPage());
            List threadList = null;
            try {
         threadList = DbUtil.getThreads(new Long(forumSubsection.getId()),
         forumPageForm.getStartForm(),
         forumPageForm.getItemPerPage());
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
            Map callbackMap = ForumManager.getForumPageTrailCallbackMap();
            forumPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(forumSubsection, callbackMap));
            forumPageForm.setThreadList(threadList);
            forumPageForm.setForumSubsection(forumSubsection);
            forumPageForm.populatePaginationItems();
            return mapping.findForward(forward);
        }*/

}