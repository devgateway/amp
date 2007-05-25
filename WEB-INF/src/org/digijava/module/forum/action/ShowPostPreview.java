/*
 *   ShowPostPreview.java
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.common.exception.BBCodeException;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumManager;

public class ShowPostPreview
    extends ForumAction {

  public ActionForward process(ActionMapping mapping,
                               ForumBaseForm form,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Forum forum,
                               ForumUserSettings forumUser) {
    ForumPageForm forumPageForm = (ForumPageForm) form;
    String forward = "postDetails";
    ActionErrors errors = new ActionErrors();

    long postId = forumPageForm.getPostId();


      String parsedContent = forumPageForm.getPostContent();

      try {

        if (forumPageForm.getQuoteId() != 0) {
          ForumPost quotedPost = null;
          try {
            quotedPost =
                DbUtil.getPostItem(forumPageForm.getQuoteId());
          }
          catch (Exception ex1) {
          }
          if (quotedPost != null) {
            forumPageForm.setEnableEmotions(true);
            forumPageForm.setAllowHtml(true);
            ForumUserSettings userSet =
                quotedPost.getAuthorUserSettings();
            String userName = "";

            if (userSet != null) {
              userName = userSet.getNickName();
              if (quotedPost.getNotifyOnReply()) {
                /**@todo send email notification code*/
              }
            }

            StringBuffer quoteCode = new StringBuffer();
            quoteCode.append("[quote=\"");
            quoteCode.append(userName);
            quoteCode.append("\"]");
            quoteCode.append(quotedPost.getContent());
            quoteCode.append("[/quote]");

            forumPageForm.setPostContent(quoteCode.toString());
          }
        }

        //Upload file
        if (forumPageForm.getFormFile() != null) {
          String instanceId;
          if (getModuleInstance().getRealInstance() == null) {
            instanceId = getModuleInstance().getInstanceName();
          }
          else {
            instanceId = getModuleInstance().getRealInstance().
                getInstanceName();
          }

          String relPath = request.getContextPath() + "/forum/" + instanceId;

          String pContent = forumPageForm.getPostContent();
          pContent +=
              ForumManager.getUplodadedFileBBTag(forumPageForm.getFormFile(),
                                                 relPath, forumUser, errors);
          forumPageForm.setPostContent(pContent);

        }

        String content = forumPageForm.
            getPostContent();

        if (content == null) content = new String();

        parsedContent =
            BBCodeParser.parse(content,
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

      if (forumPageForm.getPostId() == 0) {
        forumPageForm.setPostTitle(forumPageForm.getThreadTitle());
      }



    return mapping.findForward(forward);

  }

}