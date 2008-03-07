/*
*   ShowSection.java
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.util.DbUtil;
import java.util.List;
import java.util.Map;
import org.digijava.module.forum.util.ForumConstants;
import java.util.HashMap;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.LocationTrailUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;

public class ShowSection
    extends ForumAction {

    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showSection";

        if (forumUser != null) {
            forumPageForm.setNewPmCount(DbUtil.getNewPrivateMsgCount(forumUser));
            forumPageForm.setUnreadPmCount(DbUtil.getUnreadPrivateMsgCount(
                forumUser));
        }

        ForumSection forumSection = null;
        try {
            forumSection =
                DbUtil.getSectionItem(forumPageForm.getSectionId());
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.gettingSection"));
        }

        //Trail generation
        Map callbackMap = ForumManager.getForumPageTrailCallbackMap();

        forumPageForm.setLocationTrailItems(LocationTrailUtil.
                                            getTrailItems(forumSection,
            callbackMap));

        forumPageForm.setForumSection(forumSection);

        return mapping.findForward(forward);
    }



}