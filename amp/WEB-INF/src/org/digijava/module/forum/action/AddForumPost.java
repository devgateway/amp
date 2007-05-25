/*
 *   AddForumPost.java
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
import java.util.Iterator;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumAsset;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;
import org.digijava.module.common.util.ModuleEmailManager;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AddForumPost
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


        ForumThread forumThread = null;
        try {
            forumThread = DbUtil.getThreadItem(forumPageForm.getThreadId());
        } catch (ForumException ex) {

        }
        ForumSubsection forumSubsection = forumThread.getSubsection();
        ForumPost newPost = new ForumPost(forumUser,
                                          forumPageForm.getPostContent());
        forumPageForm.fillPost(newPost);
        newPost.setThread(forumThread);
        if (forumUser != null) {
          newPost.setDigiUserId(forumUser.getDigiUserId());
        }

        boolean isAdmin = false;
        if (forumThread.getSubsection().getRequiresPublishing()) {
            // If forum requires publishing
            isAdmin = getIsAdmin();
        }

        //Upload file
        if (forumPageForm.getFormFile() != null) {
            String instanceId;
            if (getModuleInstance().getRealInstance() == null) {
              instanceId = getModuleInstance().getInstanceName();
            }
            else {
              instanceId = getModuleInstance().getRealInstance().
                  getInstanceName();
            }

            String relPath = request.getContextPath() + "/forum/" + instanceId;

            String pContent = newPost.getContent();
            pContent +=
                ForumManager.getUplodadedFileBBTag(forumPageForm.getFormFile(),
                                                   relPath, forumUser, errors);
            newPost.setContent(pContent);

        }


        //Set "not published" for forum in publishing mode
        if (forumThread.getSubsection().getRequiresPublishing() && !isAdmin) {
            newPost.setPublished(false);
        }
        else {
            newPost.setPublished(true);
        }
        try {
            DbUtil.createPost(newPost);
        }
        catch (ForumException ex2) {
            errors.add("forumGlobalError",
                           new ActionError("error.forum.createNewPost"));
        }

        forumThread.setLastPost(newPost);
        forumThread.setLastPostDate(new Date());
        if (forumThread.getShadowThreads().size() != 0) {
            Iterator it = forumThread.getShadowThreads().iterator();
            while (it.hasNext()) {
                ForumThread shadowThr = (ForumThread) it.next();
                shadowThr.setLastPostDate(forumThread.getLastPostDate());
            }
        }
        forumSubsection.setLastPost(newPost);

        try {
            DbUtil.updateThread(forumThread);
            DbUtil.updateSubsection(forumSubsection);
        }
        catch (ForumException ex3) {
            errors.add("forumGlobalError",
                           new ActionError("error.forum.createNewPost"));
        }


        if (forumUser != null) {
            forumUser.setTotalPosts(forumUser.getTotalPosts() + 1);
            try {
                DbUtil.updateForumUser(forumUser);
            }
            catch (ForumException ex1) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.createNewPost"));
            }
        }

        long newThreadId = forumThread.getId();
        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());

        path.append("?threadId=");
        path.append(newThreadId);
        path.append("&lastPost=true");

        //Send notification email to module admin if recuired
        if (forumThread.getSubsection().getRequiresPublishing() && !isAdmin) {
          try {

            StringBuffer modulePath = new StringBuffer (RequestUtils.getFullModuleUrl(request));
            modulePath.append("showThread.do?threadId=");
            modulePath.append(newThreadId);
            modulePath.append("&lastPost=true");

            StringBuffer title = new StringBuffer ("New post in topic \"");
            title.append(forumThread.getTitle());
            title.append("\"");

            StringBuffer description = new StringBuffer("New post is submited in topic \"");
            description.append(forumThread.getTitle());
            description.append("\" by ");
            if (forumUser != null) {
              description.append(forumUser.getNickName());
            } else {
              description.append(newPost.getUnregisteredFullName());
              description.append(" (");
              description.append(newPost.getUnregisteredEmail());
              description.append(")");
            }


            ModuleEmailManager.sendAdminEmail(this.getModuleInstance(),
                                              title.toString(),
                                              "",
                                              "",
                                              description.toString() ,
                                              modulePath.toString());
          }
          catch (Exception ex4) {
          }
        }

        retFwd = new ActionForward(path.toString(), true);

        return retFwd;

    }
}



