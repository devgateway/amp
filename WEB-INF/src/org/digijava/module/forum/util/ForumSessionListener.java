/*
*   ForumSessionListener.java
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

package org.digijava.module.forum.util;

import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.forum.dbentity.Forum;

public class ForumSessionListener
    implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent event) {
        /*
                if (event.getSession().
             getAttribute(ForumConstants.VISITED_SESSION_MAP) == null ) {
             event.getSession().setAttribute(ForumConstants.VISITED_SESSION_MAP,
                                                    new HashMap());
                }
         */
    }

    public void sessionDestroyed(HttpSessionEvent event) {

        User loggedUser = (User) event.getSession().getAttribute(Constants.USER);

        if (loggedUser != null) {
            ForumUserSettings fUser = null;
           // Get forum for the current site and instance
            ModuleInstance moduleInstance = (ModuleInstance) event.getSession().
                getAttribute(Constants.MODULE_INSTANCE_OBJECT);
            Forum forum = ForumManager.getForum(moduleInstance);

            try {
                fUser = DbUtil.getForumUserItem(loggedUser.getId(),
                                                new Long(forum.getId()));
            }
            catch (Exception ex1) {
            }
            if (fUser != null) {
                fUser.setLastActiveTime(new Date());
                try {
                    DbUtil.updateForumUser(fUser);
                }
                catch (Exception ex2) {
                }
            }
        }
    }
}