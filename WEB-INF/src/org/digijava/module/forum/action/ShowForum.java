/*
*   ShowForum.java
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

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.apache.struts.action.ActionError;
import org.digijava.kernel.util.RequestUtils;



public class ShowForum extends ForumAction {


    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;

        if (forumUser != null) {
            forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(forumUser));
            forumPageForm.setUnreadPmCount(DbUtil.getUnreadPrivateMsgCount(forumUser));

            RequestUtils.setTranslationAttribute(request,
                                                 "forumPageForm.unreadPmCount",
                                                 String.valueOf (DbUtil.getUnreadPrivateMsgCount(forumUser)));


        }
        //Trail generation
        Map callbackMap = ForumManager.getForumPageTrailCallbackMap();
        if (forum != null) {
            forumPageForm.setLocationTrailItems(LocationTrailUtil.getTrailItems(
                forum, callbackMap));
        }

        String forward = "showForum";
        return mapping.findForward(forward);
    }
}