/*
 *   ShowThread.java
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumConstants;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import java.util.Iterator;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumPostEx;
import org.digijava.module.common.util.BBCodeParser;
import java.io.*;
import org.digijava.module.common.exception.*;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.forum.exception.*;
import org.digijava.kernel.security.DgSecurityManager;
import javax.security.auth.Subject;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.digijava.module.forum.util.LocationTrailItemCallback;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumSection;
import java.util.Collection;
import org.digijava.module.forum.form.AdminPageForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowAdminThread
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "showAdminThread";
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

        adminPageForm.setForum(forum);

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

        ForumThread forumThread = null;
        try {
            forumThread =
                DbUtil.getThreadItem(adminPageForm.getThreadId());
        }
        catch (ForumException ex4) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingThreads"));
        }

        adminPageForm.setForumThread(forumThread);

        List posts = null;

        adminPageForm.setSize(forumThread.getPostCount());
        adminPageForm.setItemPerPage(forum.getPostsPerPage());

        boolean isAdmin = false;
        List threadPostIds = null;
        if (forumThread.getSubsection().getRequiresPublishing()) {
            /* If forum requires publishing */
            Subject subject = DgSecurityManager.getSubject(request);
            isAdmin = DgSecurityManager.permitted(subject,
                                                  moduleInstance.getSite(),
                                                  moduleInstance,
                                                  ResourcePermission.INT_ADMIN);
             try {

                 threadPostIds = DbUtil.getThreadPostIds(adminPageForm.
                     getThreadId());
                 adminPageForm.setSize(threadPostIds.size());
            }
            catch (ForumException ex3) {
            }

        }


        //Pagination
        if (request.getParameter("firstPost") != null) {
            adminPageForm.setToFirstPage();
        }
        if (request.getParameter("lastPost") != null) {
            adminPageForm.setToLastPage();
        }

        try {


                posts = DbUtil.getPosts(new Long(forumThread.getId()),
                                        ForumConstants.FILTER_POSTS_ALL,
                                        ForumConstants.SORT_DESC,
                                             adminPageForm.getStartForm(),
                                             adminPageForm.getItemPerPage());


        }
        catch (Exception ex) {
        }

        List postExList = new ArrayList();
        if (posts != null && !posts.isEmpty()) {
            Iterator it = posts.iterator();
            while (it.hasNext()) {
                ForumPost post = (ForumPost) it.next();
                String parsedContent = "";
                try {
                    if (post.getContent() != null) {
                        parsedContent = BBCodeParser.parse(post.getContent(),
                            post.getEnableEmotions(), !post.getAllowHtml(),
                            request);
                    }
                }
                catch (IOException ex2) {
                }
                catch (BBCodeException ex2) {
                }
                ForumPostEx postEx = new ForumPostEx(parsedContent);
                postEx.setPost(post);

                if (post.getAuthorUserSettings() == null) {
                    User posterUser =
                        DgUtil.getUser(new Long(post.getDigiUserId()));
                    StringBuffer buff = new StringBuffer();
                    buff.append(posterUser.getFirstNames());
                    buff.append(" ");
                    buff.append(posterUser.getLastName());
                    postEx.setUserName(buff.toString());
                }

                postExList.add(postEx);
            }
        }

        if (request.getParameter("lastPost") != null) {
            adminPageForm.setToLastPage();

            if (!postExList.isEmpty()){
                ForumPostEx lastPost =
                    (ForumPostEx) postExList.get(postExList.size() - 1);
                long lastPostId = lastPost.getId();
                //Add inpage link to scroll to the last post

                request.getRequestURL().append("#");
                request.getRequestURL().append(lastPostId);
            }
        }

        //Trail generation
        Map callbackMap = ForumManager.getAdminPageTrailCallbackMap();
        if (forumThread != null) {
            adminPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
                forumThread, callbackMap));
        }

        adminPageForm.populatePaginationItems();
        adminPageForm.setPostList(postExList);
        visitedTopics.put(new Long(forumThread.getId()), new Date());
        return mapping.findForward(forward);
    }
}