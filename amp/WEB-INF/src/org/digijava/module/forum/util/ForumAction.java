/*
 *   ForumAction.java
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

package org.digijava.module.forum.util;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumUserSettings;

import java.util.Map;
import java.util.HashMap;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;
import java.util.Date;
import org.digijava.kernel.security.DgSecurityManager;
import javax.security.auth.Subject;
import org.digijava.kernel.security.ResourcePermission;

public class ForumAction
    extends Action {

  private boolean isAdmin;
  private ModuleInstance moduleInstance;
  public ActionErrors errors = null;

  private boolean criticalError;

  public ForumAction() {
    isAdmin = false;
  }

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    ForumBaseForm forumBaseForm = (ForumBaseForm) form;

    errors = new ActionErrors();
    criticalError = false;

    Map visitedTopics = (Map) request.getSession().
        getAttribute(ForumConstants.VISITED_SESSION_MAP);
    if (visitedTopics == null) {
      visitedTopics = new HashMap();
      request.getSession().setAttribute(ForumConstants.
                                        VISITED_SESSION_MAP,
                                        visitedTopics);
    }
    // Get forum for the current site and instance
    moduleInstance = RequestUtils.getModuleInstance(
        request);
    Forum forum = ForumManager.getForum(moduleInstance);
    forumBaseForm.setForum(forum);
    User loggedUser = RequestUtils.getUser(request);
    ForumUserSettings fUser = null;
    if (forum != null && loggedUser != null) {
      try {
        fUser = ForumManager.getForumUser(loggedUser,
                                          forum.getId());

        //Create user settings record if doesnot exist
        if (fUser == null) {
          String newNickName = loggedUser.getFirstNames() + " " +
              loggedUser.getLastName();
          fUser =
              new ForumUserSettings(loggedUser.getId().longValue(),
                                    newNickName);
          fUser.setForumId(forum.getId());
          fUser.setRegisterDate(new Date());
          DbUtil.createForumUser(fUser);
        }
      }
      catch (ForumException ex) {
        errors.add("forumGlobalError",
                   new ActionError(
            "error.forum.errorGettingUserSettings"));

      }
      Subject subject = DgSecurityManager.getSubject(request);
      isAdmin = DgSecurityManager.permitted(subject,
                                            moduleInstance.getSite(),
                                            moduleInstance,
                                            ResourcePermission.INT_ADMIN);
    }
    else {
      isAdmin = false;
    }

    forumBaseForm.setVisitedThreadsMap(visitedTopics);
    forumBaseForm.setForumUserSettings(fUser);
    ActionForward retFwd = process(mapping,
                                   forumBaseForm,
                                   request,
                                   response,
                                   forum,
                                   fUser);

    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      if (criticalError) {
        retFwd = new ActionForward("/showLayout.do?layout=forum_showForumError");
      }
    }

    return retFwd;
  }

  public ActionForward process(ActionMapping mapping,
                               ForumBaseForm form,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Forum forum,
                               ForumUserSettings forumUser) {
    return null;
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }

  public ModuleInstance getModuleInstance() {
    return moduleInstance;
  }

  public void setModuleInstance(ModuleInstance moduleInstance) {
    this.moduleInstance = moduleInstance;
  }

  public boolean isCriticalError() {
    return criticalError;
  }

  public void setCriticalError(boolean criticalError) {
    this.criticalError = criticalError;
  }
}