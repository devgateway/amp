/*
*   ShowPM.java
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
import org.digijava.module.forum.dbentity.ForumPrivateMessage;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.util.ForumManager;
import org.digijava.kernel.user.User;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionError;


public class ShowPm extends ForumAction {


    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showPm";

        ForumPrivateMessage pm = null;
                try {
                    pm = DbUtil.getPrivateMsgItem(
                        forumPageForm.getPmId());
                }
                catch (ForumException ex) {
                }



                if (pm != null){
                    String parsedContent = pm.getContent();
                    try {
                        parsedContent = BBCodeParser.parse(pm.getContent(),
                            pm.getEnableEmotions(),
                            !pm.getAllowHtml(),
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
                    forumPageForm.setPostTitle(pm.getTitle());
                    forumPageForm.setForumUserSettings(pm.getAuthorUserSettings());
                    forumPageForm.setPrivateMessage(pm);

                    if (pm.getToUserId() == forumUser.getDigiUserId() &&
                        pm.getIsNew()) {

                        pm.setIsNew(false);
                        try {
                            //Cerate a copy of the message for senders sentbox
                            ForumPrivateMessage sentPm =
                                new ForumPrivateMessage(pm.getAuthorUserSettings(),
                                                        pm.getContent());

                            sentPm.setDigiUserId(pm.getDigiUserId());
                            sentPm.setAllowHtml(pm.getAllowHtml());
                            sentPm.setEnableEmotions(pm.getEnableEmotions());
                            sentPm.setEditedBy(pm.getEditedBy());
                            sentPm.setEditedOn(pm.getEditedOn());
                            sentPm.setIsNew(false);
                            sentPm.setIsSentPost(true);
                            sentPm.setPostTime(pm.getPostTime());
                            sentPm.setTitle(pm.getTitle());
                            sentPm.setToUserId(pm.getToUserId());

                            DbUtil.updatePrivateMsg(pm);
                            DbUtil.createPrivateMessage(sentPm);
                        }
                        catch (ForumException ex1) {
                            errors.add("forumGlobalError",
                           new ActionError("error.forum.pmProcess"));
                        }

                    }
                    forumPageForm.setFolderName(pm.getFolderName());
                }
                return mapping.findForward(forward);

    }



/*


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        ForumPageForm forumPageForm = (ForumPageForm) form;
        String forward = "showPm";

        // Get forum for the current site and instance
        RequestUtils.getModuleInstance(request);
        ModuleInstance moduleInstance = RequestUtils.getModuleInstance(
            request);
        Forum forum = ForumManager.getForum(moduleInstance);
        forumPageForm.setForum(forum);

        User loggedUser = RequestUtils.getUser(request);
        ForumUserSettings fUser = null;

        if (forum != null) {
            try {
                fUser = ForumManager.getForumUser(loggedUser,
                                                  forum.getId());
            }
            catch (Exception ex2) {
            }
        }


        ForumPrivateMessage pm = null;
        try {
            pm = DbUtil.getPrivateMsgItem(
                forumPageForm.getPmId());
        }
        catch (ForumException ex) {
        }



        if (pm != null){
            String parsedContent = pm.getContent();
            try {
                parsedContent = BBCodeParser.parse(pm.getContent(),
                    pm.getEnableEmotions(),
                    !pm.getAllowHtml(),
                    request);
            }
            catch (IOException ex) {
            }
            catch (BBCodeException ex) {
            }
            forumPageForm.setParsedContent(parsedContent);
            forumPageForm.setPostTitle(pm.getTitle());
            forumPageForm.setForumUserSettings(pm.getAuthorUserSettings());

            if (pm.getToUserId() == fUser.getDigiUserId()) {
                pm.setIsNew(false);
            }
            try {
                DbUtil.updatePrivateMsg(pm);
            }
            catch (ForumException ex1) {
            }
            forumPageForm.setFolderName(pm.getFolderName());
        }
        return mapping.findForward(forward);
    }*/
}