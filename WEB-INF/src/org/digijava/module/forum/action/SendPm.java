/*
 *   SendPM.java
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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
import org.digijava.module.forum.util.ForumConstants;
import java.util.Date;

public class SendPm
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showInbox";

        ForumUserSettings toUser = null;
        try {
            toUser = DbUtil.getForumUserItem(forumPageForm.getUserId());
        }
        catch (Exception ex1) {
        }
        if (toUser != null && forumUser != null) {
            if (forumPageForm.getPmId() == 0) {
                ForumPrivateMessage newPm = new ForumPrivateMessage(forumUser,
                    forumPageForm.getPostContent());
                newPm.setToUserId(toUser.getDigiUserId());
                newPm.setAllowHtml(forumPageForm.isAllowHtml());
                newPm.setEnableEmotions(forumPageForm.isEnableEmotions());
                newPm.setTitle(forumPageForm.getPostTitle());
                newPm.setFolderName(ForumConstants.PM_FOLDER_INBOX);
                try {
                    DbUtil.createPrivateMessage(newPm);
                }
                catch (ForumException ex) {
                    errors.add("forumGlobalError",
                               new ActionError("error.forum.createPm"));
                }
            }
            else {
                try {
                    ForumPrivateMessage pm =
                        DbUtil.getPrivateMsgItem(forumPageForm.getPmId());
                    if (pm != null) {
                        //forumPageForm.fillPM(pm);
                        pm.setTitle(forumPageForm.getPostTitle());
                        pm.setContent(forumPageForm.getPostContent());
                        pm.setEditedBy(forumUser);
                        pm.setEditedOn(new Date());
                        DbUtil.updatePrivateMsg(pm);
                    }
                }
                catch (ForumException ex) {
                    errors.add("forumGlobalError",
                               new ActionError("error.forum.editPm"));
                }
            }
        }
        else {
                    errors.add("forumGlobalError",
                       new ActionError("error.forum.pm.userNotFound"));
                       setCriticalError(true);
        }

        return mapping.findForward(forward);

    }
}