/*
 *   SplitTopic.java
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

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPrivateMessage;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.dbentity.ForumThread;
import java.util.List;
import org.digijava.module.forum.dbentity.ForumSubsection;
import java.util.Date;
import org.digijava.module.forum.dbentity.ForumPost;
import java.util.Iterator;
import org.apache.struts.action.ActionError;

public class SplitTopic
    extends ForumAction {
    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "showThread";

        ForumThread newThread = null;
        if (adminPageForm.getThreadTitle() != null &&
            adminPageForm.getThreadTitle().length() > 0) {

            ArrayList movePostList = new ArrayList();
            String[] postIdList = adminPageForm.getCheckboxList();
            try {
                if (postIdList != null && postIdList.length > 0) {

                    newThread =
                        new ForumThread(adminPageForm.getThreadTitle(), "");

                    ForumSubsection subsection =
                        DbUtil.getSubsectionItem(adminPageForm.getSubsectionId());
                    newThread.setSubsection(subsection);
                    newThread.setCreationDate(new Date());
                    //DbUtil.createThread(newThread);


                    if (request.getParameter("from") != null) {
                        ForumPost fromPost =
                            DbUtil.getPostItem(new Long(postIdList[0]).
                                               longValue());
                        List updateList = DbUtil.
                            getPostsAfterDate(new Long(fromPost.getThread().
                            getId()),
                                              fromPost.getPostTime());
                        movePostList.addAll(updateList);
                    }
                    else {
                        for (int idIndex = 0;
                             idIndex < postIdList.length;
                             idIndex++) {
                            ForumPost updtPost =
                                DbUtil.getPostItem(new Long(postIdList[idIndex]).
                                longValue());
                            movePostList.add(updtPost);
                        }
                    }

                    Iterator it = movePostList.iterator();
                    while (it.hasNext()) {
                        ForumPost post = (ForumPost) it.next();
                        post.setThread(newThread);
                    }

                    newThread.setLastPost((ForumPost)movePostList.
                                          get(movePostList.size() - 1));
                    DbUtil.createThread(newThread);

                    DbUtil.updatePostList(movePostList);

                }
            }
            catch (ForumException ex) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.splitTopics"));
            }

        }
        else {

        }

        ActionForward retFwd = null;
        if (newThread != null) {
            long newThreadId = newThread.getId();
            retFwd = mapping.findForward(forward);
            StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                                 getPath());
            path.append("?threadId=");
            path.append(newThreadId);
            retFwd = new ActionForward(path.toString(), true);
        }
        else {
            retFwd = mapping.findForward("showRoot");
        }

        return retFwd;

    }
}