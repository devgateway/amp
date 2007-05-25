/*
*   ForumMessage.java
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

package org.digijava.module.forum.dbentity;

import java.util.Date;

public class ForumMessage {
    protected ForumMessage() {
    }

    public ForumMessage(ForumUserSettings authorUserSettings,
                         String content) {
        this.authorUserSettings = authorUserSettings;
        this.content = content;
        this.postTime = new Date();
    }
    private long id;
    private ForumUserSettings authorUserSettings;
    private long digiUserId;
    private String title;
    private Date postTime;
    private String content;
    private boolean enableEmotions;
    private boolean allowHtml;

    private ForumUserSettings editedBy;
    private Date editedOn;

    public boolean getAllowHtml() {
        return allowHtml;
    }
    public void setAllowHtml(boolean allowHtml) {
        this.allowHtml = allowHtml;
    }
    public ForumUserSettings getAuthorUserSettings() {
        return authorUserSettings;
    }
    public void setAuthorUserSettings(ForumUserSettings authorUserSettings) {
        this.authorUserSettings = authorUserSettings;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getDigiUserId() {
        return digiUserId;
    }
    public void setDigiUserId(long digiUserId) {
        this.digiUserId = digiUserId;
    }
    public boolean getEnableEmotions() {
        return enableEmotions;
    }
    public void setEnableEmotions(boolean enableEmotions) {
        this.enableEmotions = enableEmotions;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getPostTime() {
        return postTime;
    }
    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
  public ForumUserSettings getEditedBy() {
    return editedBy;
  }
  public void setEditedBy(ForumUserSettings editedBy) {
    this.editedBy = editedBy;
  }
  public Date getEditedOn() {
    return editedOn;
  }
  public void setEditedOn(Date editedOn) {
    this.editedOn = editedOn;
  }
}