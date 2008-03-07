/*
 *   SaveForumThreads.java
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

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.util.DbUtil;

public class SaveForumThreads
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "showSubsection";
        ActionErrors errors = new ActionErrors();

        Collection threads = new ArrayList();
        Collection shadowThreads = new ArrayList();
        boolean leaveShadow = adminPageForm.getLeaveShadow();

        ForumSubsection subsection = null; ForumSubsection shadowSubsection = null;
        try {
            subsection =
                DbUtil.getSubsectionItem(adminPageForm.getMoveToSectionId());
            shadowSubsection =
                DbUtil.getSubsectionItem(adminPageForm.getSubsectionId());
        }
        catch (ForumException ex2) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.savingThread"));
        }

        for (int threadIdIndex = 0;
             threadIdIndex < adminPageForm.getCheckboxList().length;
             threadIdIndex++) {
            long threadId =
                new Long(adminPageForm.
                         getCheckboxList()[threadIdIndex]).longValue();
            ForumThread forumThread = null;
            try {
                forumThread = DbUtil.getThreadItem(threadId);
            }
            catch (ForumException ex3) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingThread"));
            }
            forumThread.setSubsection(subsection);
            threads.add(forumThread);
            //Create shadow threads
            if (leaveShadow) {
                ForumThread shadowThread =
                    new ForumThread(null, null);
                shadowThread.setSubsection(shadowSubsection);
                if (forumThread.getParentThread() == null) {
                    shadowThread.setParentThread(forumThread);
                }
                else {
                    shadowThread.setParentThread(forumThread.getParentThread());
                }
                shadowThreads.add(shadowThread);
            }
        }

        try {
            DbUtil.updateThreadList(threads);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.savingThread"));
        }

        if (leaveShadow) {
            try {
                DbUtil.updateThreadList(shadowThreads);
            }
            catch (ForumException ex1) {
                errors.add("forumGlobalError",
                       new ActionError("error.forum.savingThread"));
            }

            /*
                         shadowSubsection.getThreads().addAll(shadowThreads);
                         try {
                DbUtil.updateSubsection(shadowSubsection);
                         }
                         catch (ForumException ex1) {
                         }*/

        }

        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());
        path.append("?subsectionId=");
        path.append(adminPageForm.getSubsectionId());
        retFwd = new ActionForward(path.toString(), true);

        return retFwd;
    }
}