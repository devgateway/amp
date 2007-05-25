/*
 *   SaveForumThread.java
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

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveForumThread
    extends ForumAction {

  public ActionForward process(ActionMapping mapping,
                               ForumBaseForm form,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Forum forum,
                               ForumUserSettings forumUser) {
    ForumPageForm forumPageForm = (ForumPageForm) form;
    String forward = "showThread";

    ForumSubsection forumSubsection = null;

    try {
      forumSubsection = DbUtil.getSubsectionItem(forumPageForm.
                                                 getSubsectionId());
    }
    catch (ForumException ex) {

    }

    ForumThread newThread = new ForumThread(forumPageForm.getThreadTitle(),
                                            forumPageForm.getThreadComment());
    newThread.setSubsection(forumSubsection);

    ForumPost newPost = new ForumPost(forumUser,
                                      forumPageForm.getPostContent());

    forumPageForm.fillPost(newPost);
    newPost.setTitle(newThread.getTitle());
    newPost.setDigiUserId(forumUser.getDigiUserId());
    newPost.setThread(newThread);

    //Set "not published" for forum in publishing mode
    if (forumSubsection.getRequiresPublishing()) {
      newPost.setPublished(false);
    }
    newThread.setLastPost(newPost);
    if (forumUser != null) {
      newThread.setAuthorUserId(forumUser.getId());
    }
    newThread.setLastPost(newPost);
    newThread.setLastPostDate(new Date());

    forumSubsection.setLastPost(newPost);
    try {
//      DbUtil.createPost(newPost);
      DbUtil.createThread(newThread);
    }
    catch (ForumException ex3) {
    }

    try {
      DbUtil.updateSubsection(forumSubsection);
    }
    catch (ForumException ex) {
    }

    if (forumUser != null) {
      forumUser.setTotalPosts(forumUser.getTotalPosts() + 1);
      try {
        DbUtil.updateForumUser(forumUser);
      }
      catch (ForumException ex1) {
        errors.add("forumGlobalError",
                   new ActionError("error.forum.updateUser"));
      }
    }

    long newThreadId = newThread.getId();
    ActionForward retFwd = mapping.findForward(forward);
    StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                         getPath());
    path.append("?threadId=");
    path.append(newThreadId);
    retFwd = new ActionForward(path.toString(), true);

    return retFwd;
  }
}