/*
*   ShowPMDetails.java
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
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;

public class ShowPMDetails
    extends ForumAction {
    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showPmDetails";

        if (forumPageForm.getPostContent() == null ||
            forumPageForm.getPostContent().length() == 0) {
            forumPageForm.setAllowHtml(true);
            forumPageForm.setEnableEmotions(true);
        }
        else {
            String parsedContent = forumPageForm.getPostContent();
            try {
                parsedContent = BBCodeParser.parse(forumPageForm.getPostContent(),
                    forumPageForm.isEnableEmotions(),
                    !forumPageForm.isAllowHtml(),
                    request);
            }
            catch (IOException ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.bbcode"));
            }
            catch (BBCodeException ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.bbcode"));
            }

            forumPageForm.setParsedContent(parsedContent);
        }

        ForumUserSettings toUser = DbUtil.getForumUserItem(forumPageForm.
            getUserId());

        if (toUser != null) {
            forumPageForm.setNickName(toUser.getNickName());
            forumPageForm.setUserId(toUser.getId());
        }
        return mapping.findForward(forward);

    }
    /*
        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
            ForumPageForm forumPageForm = (ForumPageForm) form;
            String forward = "showPmDetails";
            // Get forum for the current site and instance
            RequestUtils.getModuleInstance(request);
            ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
                request);
            Forum forum = ForumManager.getForum(moduleInstance);
            forumPageForm.setForum(forum);
            if (forumPageForm.getPostContent()==null ||
                forumPageForm.getPostContent().length()==0) {
                forumPageForm.setAllowHtml(true);
                forumPageForm.setEnableEmotions(true);
            } else {
                String parsedContent = forumPageForm.getPostContent();
                try {
         parsedContent = BBCodeParser.parse(forumPageForm.getPostContent(),
                        forumPageForm.isEnableEmotions(),
                        !forumPageForm.isAllowHtml(),
                        request);
                }
                catch (IOException ex) {
                }
                catch (BBCodeException ex) {
                }
                forumPageForm.setParsedContent(parsedContent);
            }
         ForumUserSettings toUser = DbUtil.getForumUserItem(forumPageForm.getUserId());
            if (toUser != null) {
                forumPageForm.setNickName(toUser.getNickName());
                forumPageForm.setUserId(toUser.getId());
            }
            return mapping.findForward(forward);
        }*/

}