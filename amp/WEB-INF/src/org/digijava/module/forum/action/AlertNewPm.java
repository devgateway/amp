/*
 *   AlertNewPm.java
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

package org.digijava.module.forum.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;


public class AlertNewPm extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "alertUserNewPm";

        if (forumUser != null) {
            forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(forumUser));
            forumUser.setLastPmNotify(new Date());
            try {
                DbUtil.updateForumUser(forumUser);
            }
            catch (ForumException ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.updateUser"));
            }
        }



        return mapping.findForward(forward);
    }

}
