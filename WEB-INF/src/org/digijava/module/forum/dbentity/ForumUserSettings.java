/*
*   ForumUserSettings.java
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

public class ForumUserSettings {

    private long id;
    private long digiUserId;
    private long forumId;
    private String nickName;
    private String signature;
    private int totalPosts;
    private String avatarUrl;
    private Date lastActiveTime;
    private String location;
    private Date registerDate;
    private Date lastPmNotify;

    public ForumUserSettings(long id, String nickName) {
        this.digiUserId = id;
        this.nickName = nickName;
        this.totalPosts = 0;
        this.lastActiveTime = new Date();
    }

    private ForumUserSettings() {

    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }
    public long getForumId() {
        return forumId;
    }
    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Date getRegisterDate() {
        return registerDate;
    }
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public long getDigiUserId() {
        return digiUserId;
    }
    public void setDigiUserId(long digiUserId) {
        this.digiUserId = digiUserId;
    }
  public Date getLastPmNotify() {
    return lastPmNotify;
  }
  public void setLastPmNotify(Date lastPmNotify) {
    this.lastPmNotify = lastPmNotify;
  }

}