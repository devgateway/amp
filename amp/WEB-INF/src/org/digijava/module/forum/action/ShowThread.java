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


package org.digijava.module.forum.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.exception.BBCodeException;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumPostEx;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.apache.struts.action.ActionError;
import org.digijava.module.forum.util.ForumConstants;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowThread
    extends ForumAction {

  public ActionForward process(ActionMapping mapping,
                               ForumBaseForm form,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Forum forum,
                               ForumUserSettings forumUser) {
    ForumPageForm forumPageForm = (ForumPageForm) form;
    String forward = "showThread";

    if (forumUser != null) {
      forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(forumUser));
      forumPageForm.setUnreadPmCount(DbUtil.getUnreadPrivateMsgCount(forumUser));
    }

    forumPageForm.setEnableEmotions(true);
    forumPageForm.setAllowHtml(true);
    forumPageForm.setNotifyOnReply(false);

    ForumThread forumThread = null;
    try {
      forumThread =
          DbUtil.getThreadItem(forumPageForm.getThreadId());
      forumPageForm.setPrevThreadId(DbUtil.getPrevThreadID(forumThread));
      forumPageForm.setNextThreadId(DbUtil.getNextThreadID(forumThread));
    }
    catch (ForumException ex) {
      errors.add("forumGlobalError",
                 new ActionError("error.forum.gettingThread"));
      setCriticalError(true);
    }

    if (forumThread != null) {
      forumPageForm.setForumThread(forumThread);
      forumPageForm.setPostContent("");

      List posts = null;

      forumPageForm.setSize(forumThread.getPostCount());
      forumPageForm.setItemPerPage(forum.getPostsPerPage());

      boolean isAdmin = false;
      List threadPostIds = null;
      if (forumThread.getSubsection().getRequiresPublishing()) {
        // If forum requires publishing
        isAdmin = getIsAdmin();
        // If forum requires publishing
        try {
          if (isAdmin) {
            threadPostIds = DbUtil.getThreadPostIds(forumPageForm.
                getThreadId());
          }
          else {
            threadPostIds = DbUtil.getThreadPublishedPostIds(
                forumPageForm.
                getThreadId(), forumUser);

          }
          forumPageForm.setSize(threadPostIds.size());
        }
        catch (ForumException ex3) {
        }

      }
      //If post id is specified, set appropreate page number
      if (forumPageForm.getPostId() != 0) {
        int postPage = 0;
        try {
          if (!forumThread.getSubsection().getRequiresPublishing()) {
            threadPostIds = DbUtil.getThreadPostIds(forumPageForm.
                getThreadId());
          }

          if (threadPostIds != null && !threadPostIds.isEmpty()) {
            Iterator it = threadPostIds.iterator();
            for (int postIndex = 0; it.hasNext(); postIndex++) {
              Long postId = (Long) it.next();
              if (postId.longValue() == forumPageForm.getPostId()) {
                postPage = postIndex -
                    postIndex % forumPageForm.getItemPerPage();
                break;
              }
            }
            forumPageForm.setStartForm(postPage);
          }
        }
        catch (ForumException ex1) {
        }
      }

      //Pagination
      if (request.getParameter("firstPost") != null) {
        forumPageForm.setToFirstPage();
      }
      if (request.getParameter("lastPost") != null) {
        forumPageForm.setToLastPage();
      }

      try {

        if (forumThread.getSubsection().getRequiresPublishing()) {
          // If forum requires publishing
          if (isAdmin) {
            posts = DbUtil.getPosts(new Long(forumThread.getId()),
                                    forumPageForm.getFilterPostsFrom(),
                                    forumPageForm.getSortOrder(),
                                    forumPageForm.getStartForm(),
                                    forumPageForm.getItemPerPage());
          }
          else {
            posts = DbUtil.getPublishedPosts(new Long(forumThread.getId()),
                                             forumUser,
                                             forumPageForm.getFilterPostsFrom(),
                                             forumPageForm.getSortOrder(),
                                             forumPageForm.getStartForm(),
                                             forumPageForm.getItemPerPage());

          }

        }
        else {
          posts = DbUtil.getPosts(new Long(forumThread.getId()),
                                  forumPageForm.getFilterPostsFrom(),
                                  forumPageForm.getSortOrder(),
                                  forumPageForm.getStartForm(),
                                  forumPageForm.getItemPerPage());

        }

        //Get thread last post (display delete post only if post is last in thread)
        if (forumThread.getPostCount() > 0) {
          List tmpPostList =
              DbUtil.getPosts(new Long(forumThread.getId()),
                              ForumConstants.FILTER_POSTS_ALL,
                              ForumConstants.SORT_DESC,
                              forumThread.getPostCount() - 1, 1);
          if (tmpPostList != null && tmpPostList.iterator().hasNext()) {
            ForumPost lastPost = (ForumPost)
                tmpPostList.iterator().next();
            forumPageForm.setPostId(lastPost.getId());
          }
        }
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
                                                 post.getEnableEmotions(),
                                                 !post.getAllowHtml(),
                                                 request);
            }
          }
          catch (IOException ex2) {
          }
          catch (BBCodeException ex2) {
          }
          ForumPostEx postEx = new ForumPostEx(parsedContent);
          postEx.setPost(post);

          if (post.getAuthorUserSettings() == null &&
              post.getDigiUserId()!=0) {
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
        forumPageForm.setToLastPage();

        if (!postExList.isEmpty()) {
          ForumPostEx lastPost =
              (ForumPostEx) postExList.get(postExList.size() - 1);
          long lastPostId = lastPost.getId();
          //Add inpage link to scroll to the last post

          request.getRequestURL().append("#");
          request.getRequestURL().append(lastPostId);
        }
      }

      //Trail generation
      Map callbackMap = ForumManager.getForumPageTrailCallbackMap();
      forumPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
          forumThread, callbackMap));

      forumPageForm.populatePaginationItems();
      forumPageForm.setPostList(postExList);
      forumPageForm.getVisitedThreadsMap().put(new Long(forumThread.getId()),
                                               new Date());
    }

    return mapping.findForward(forward);
  }

}