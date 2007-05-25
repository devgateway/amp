/*
*   ShowEditForumPost.java
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
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumAction;

public class ShowEditForumPost
    extends ForumAction {
  public ActionForward process(ActionMapping mapping,
                               ForumBaseForm form,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Forum forum,
                               ForumUserSettings forumUser) {

        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "postDetails";

        long postId = forumPageForm.getPostId();
        ForumPost post = null;
        try {
            post = DbUtil.getPostItem(postId);
        }
        catch (Exception ex) {
        }

        forumPageForm.setPostParams(post);

        /*long threadId = post.getThread().getId();
                         ActionForward retFwd = mapping.findForward(forward);
             StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());
                         path.append("?postId=");
                         path.append(postId);
                         retFwd = new ActionForward(path.toString(), true);
                         return retFwd;*/
        return mapping.findForward(forward);
    }
}