/*
 *   PublishForumPost.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created:
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;


public class PublishForumPost extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showThread";
        ActionErrors errors = new ActionErrors();
        long postId = forumPageForm.getPostId();
        ForumPost publishPost = null;
        try {
            publishPost = DbUtil.getPostItem(postId);
        }
        catch (Exception ex1) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.publishingPost"));
        }

        publishPost.setPublished(true);

        try {
            DbUtil.updatePost(publishPost);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.publishingPost"));
        }

        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());
        path.append("?threadId=");
        path.append(publishPost.getThread().getId());
        path.append("&postId=");
        path.append(postId);
        retFwd = new ActionForward(path.toString(), true);

        return retFwd;
    }


}