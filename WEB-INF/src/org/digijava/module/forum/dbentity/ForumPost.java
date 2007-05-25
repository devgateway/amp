/*
*   ForumPost.java
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

package org.digijava.module.forum.dbentity;

import java.util.Date;

public class ForumPost extends ForumMessage{
    private ForumThread thread;
    private boolean notifyOnReply;
    private boolean published;
    private String unregisteredFullName;
    private String unregisteredEmail;

    protected ForumPost() {
    }

    public ForumPost(ForumUserSettings authorUserSettings,
                     String content) {
        super(authorUserSettings, content);
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public boolean getNotifyOnReply() {
        return notifyOnReply;
    }
    public void setNotifyOnReply(boolean notifyOnReply) {
        this.notifyOnReply = notifyOnReply;
    }
    public boolean getPublished() {
        return published;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }
  public String getUnregisteredEmail() {
    return unregisteredEmail;
  }
  public void setUnregisteredEmail(String unregisteredEmail) {
    this.unregisteredEmail = unregisteredEmail;
  }
  public String getUnregisteredFullName() {
    return unregisteredFullName;
  }
  public void setUnregisteredFullName(String unregisteredFullName) {
    this.unregisteredFullName = unregisteredFullName;
  }
}