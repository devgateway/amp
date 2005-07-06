/*
*   ForumThread.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: ForumThread.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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
import java.util.HashSet;
import java.util.Set;

public class ForumThread {
    private long id;
    private ForumSubsection subsection;
    private String title;
    private String comment;
    private boolean locked;
    private Date creationDate;
    private long authorUserId;
    private ForumPost lastPost;
    private int postCount;
    private Set posts;
    private Set shadowThreads;
    private ForumThread parentThread;
    private Date lastPostDate;

    //Non persistent
    private boolean hasNewPost;
    private ForumUserSettings authorUser;

    private ForumThread() {
    }

    public ForumThread(String threadTitle,
                       String threadComment) {
        this.title = threadTitle;
        this.comment = threadComment;
        this.posts = new HashSet();
        this.shadowThreads = new HashSet();
        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ForumPost getLastPost() {
        return lastPost;
    }

    public void setLastPost(ForumPost lastPost) {
        this.lastPost = lastPost;
    }

    public ForumSubsection getSubsection() {
        return subsection;
    }

    public void setSubsection(ForumSubsection subsection) {
        this.subsection = subsection;
    }

    public Set getPosts() {
        return posts;
    }

    public void setPosts(Set posts) {
        this.posts = posts;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }
    public ForumThread getParentThread() {
        return parentThread;
    }
    public void setParentThread(ForumThread parentThread) {
        this.parentThread = parentThread;
    }
    public Set getShadowThreads() {
        return shadowThreads;
    }
    public void setShadowThreads(Set shadowThreads) {
        this.shadowThreads = shadowThreads;
    }

    public Date getLastPostDate() {
        return lastPostDate;
    }
    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }
    public boolean getHasNewPost() {
        return hasNewPost;
    }
    public void setHasNewPost(boolean hasNewPost) {
        this.hasNewPost = hasNewPost;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ForumUserSettings getAuthorUser() {
        return authorUser;
    }
    public void setAuthorUser(ForumUserSettings authorUser) {
        this.authorUser = authorUser;
    }
}