/*
*   ShowUserInbox.java
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.common.exception.BBCodeException;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import java.util.List;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.kernel.user.User;
import org.digijava.module.forum.exception.*;
import org.digijava.module.forum.util.ForumConstants;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;

public class ShowUserInbox
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showInbox";

        String folderName = forumPageForm.getFolderName();
        if (folderName == null || folderName.length() == 0) {
            folderName = ForumConstants.PM_FOLDER_INBOX;
            forumPageForm.setFolderName(folderName);
        }

        forumPageForm.setItemPerPage(forum.getTopicsPerPage());

        List userMessages = null;
        if (forumUser != null) {
            try {
                int totalCount = 0;

                if (folderName.equals(ForumConstants.PM_FOLDER_OUTBOX)) {
                    totalCount = DbUtil.getOutboxPrivateMsgCount(forumUser);
                    userMessages = DbUtil.getOutboxPrivateMessages(new Long(
                        forumUser.getDigiUserId()),
                        forumPageForm.getStartForm(),
                        forumPageForm.getItemPerPage());

                }
                else if (folderName.equals(ForumConstants.PM_FOLDER_SENTBOX)) {
                    totalCount = DbUtil.getSentPrivateMsgCount(forumUser);
                    userMessages = DbUtil.getSentPrivateMessages(new Long(
                        forumUser.getDigiUserId()),
                        forumPageForm.getStartForm(),
                        forumPageForm.getItemPerPage());
                }
                else {

                    totalCount = DbUtil.getPrivateMsgCount(forumUser,
                        folderName);
                    userMessages = DbUtil.getPrivateMessages(new Long(
                        forumUser.getDigiUserId()),
                        folderName,
                        forumPageForm.getStartForm(),
                        forumPageForm.getItemPerPage());
                }
                forumPageForm.setSize(totalCount);
                forumPageForm.setUserPmList(userMessages);
            }
            catch (ForumException ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.gettingPms"));
            }
        }

        forumPageForm.populatePaginationItems();

        return mapping.findForward(forward);

    }
}