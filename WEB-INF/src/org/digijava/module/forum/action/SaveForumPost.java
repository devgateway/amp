/*
 *   SaveForumPost.java
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.kernel.entity.ModuleInstance;
import java.util.Date;
import java.util.Iterator;
import org.digijava.module.forum.exception.*;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;

public class SaveForumPost
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;

        String forward = "showThread";

        long postId = forumPageForm.getPostId();
        ForumPost post = null;
        try {
            post = DbUtil.getPostItem(postId);
        }
        catch (Exception ex1) {
            errors.add("forumGlobalError",
                           new ActionError("error.forum.savePost"));
        }

        forumPageForm.fillPost(post);

        try {
            post.setEditedOn(new Date());
            post.setEditedBy(forumUser);
            DbUtil.updatePost(post);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                           new ActionError("error.forum.savePost"));
        }

        long threadId = post.getThread().getId();
        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());
        path.append("?threadId=");
        path.append(threadId);
        retFwd = new ActionForward(path.toString(), true);
        return retFwd;

    }



}