/*
 *   UnlockForumThreads.java
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.AdminPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.apache.struts.action.ActionError;

public class UnlockForumThreads extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AdminPageForm adminPageForm = (AdminPageForm) form;
        String forward = "showAdminSubsection";
        ActionErrors errors = new ActionErrors();

        String [] delThreadIDs = adminPageForm.getCheckboxList();

        Collection lockThreadIds = new ArrayList();
        if (delThreadIDs.length > 0) {
            for (int item = 0; item < delThreadIDs.length; item ++) {
                Long threadId = new Long(delThreadIDs[item]);
                lockThreadIds.add(threadId);
            }
        }

        try {
            DbUtil.lockThreadList(lockThreadIds, false);
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.lockingThread"));
        }

        long subsectionId = adminPageForm.getSubsectionId();
        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).getPath());
        path.append("?subsectionId=");
        path.append(subsectionId);
        retFwd = new ActionForward (path.toString(), true);
        return retFwd;
    }
}