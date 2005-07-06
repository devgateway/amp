/*
*   ForumPrivateMessage.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id: ForumPrivateMessage.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

public class ForumPrivateMessage extends ForumMessage {

    private boolean isNew;
    private long toUserId;
    private String folderName;
    private boolean isSentPost;

    protected ForumPrivateMessage() {
    }

    public ForumPrivateMessage(ForumUserSettings authorUserSettings,
                               String content) {
        super(authorUserSettings, content);
        isNew = true;
    }
    public boolean getIsNew() {
        return isNew;
    }
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    public long getToUserId() {
        return toUserId;
    }
    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }
  public String getFolderName() {
    return folderName;
  }
  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }
    public boolean getIsSentPost() {
        return isSentPost;
    }
    public void setIsSentPost(boolean isSentPost) {
        this.isSentPost = isSentPost;
    }
}