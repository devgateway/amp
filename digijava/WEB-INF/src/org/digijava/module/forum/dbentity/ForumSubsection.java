/*
*   ForumSubsection.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: ForumSubsection.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

public class ForumSubsection {
    private long id;
    private ForumSection section;
    private String title;
    private String comment;
    private Set threads;
    private int orderIndex;
    private int threadCount;
    private int totalPosts;
    private Date creationDate;
    private ForumPost lastPost;

    private boolean locked;
    private boolean requiresPublishing;
    private boolean sendApproveMsg;
    private String approveMsg;
    private boolean sendDeleteMsg;
    private String deleteMsg;

    private ForumSubsection() {
    }

    public ForumSubsection(String subsectionTitle,
                           String subsectionComment) {
        this.title = subsectionTitle;
        this.comment = subsectionComment;
        this.creationDate = new Date();
        this.threads = new HashSet();
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

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Set getThreads() {
        return threads;
    }

    public void setThreads(Set threads) {
        this.threads = threads;
    }

    public ForumSection getSection() {
        return section;
    }

    public void setSection(ForumSection section) {
        this.section = section;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public ForumPost getLastPost() {
        return lastPost;
    }

    public void setLastPost(ForumPost lastPost) {
        this.lastPost = lastPost;
    }
    public String getApproveMsg() {
        return approveMsg;
    }
    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }
    public String getDeleteMsg() {
        return deleteMsg;
    }
    public void setDeleteMsg(String deleteMsg) {
        this.deleteMsg = deleteMsg;
    }
    public boolean getLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean getSendApproveMsg() {
        return sendApproveMsg;
    }
    public void setSendApproveMsg(boolean sendApproveMsg) {
        this.sendApproveMsg = sendApproveMsg;
    }
    public boolean getSendDeleteMsg() {
        return sendDeleteMsg;
    }
    public void setSendDeleteMsg(boolean sendDeleteMsg) {
        this.sendDeleteMsg = sendDeleteMsg;
    }
    public boolean getRequiresPublishing() {
        return requiresPublishing;
    }
    public void setRequiresPublishing(boolean requiresPublishing) {
        this.requiresPublishing = requiresPublishing;
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

}