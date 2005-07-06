/*
*   ForumPostEx.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: ForumPostEx.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

public class ForumPostEx
    extends ForumPost {
    private String parsedContent;
    private String userName;

    public ForumPostEx(String parsedContent) {
        this.parsedContent = parsedContent;
    }

    public String getParsedContent() {
        return parsedContent;
    }

    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
    }

    public void setPost (ForumPost post){
        super.setAuthorUserSettings(post.getAuthorUserSettings());
        super.setContent(post.getContent());
        super.setId(post.getId());
        super.setPostTime(post.getPostTime());
        super.setThread(post.getThread());
        super.setTitle(post.getTitle());
        super.setPublished(post.getPublished());
        super.setEditedBy(post.getEditedBy());
        super.setEditedOn(post.getEditedOn());
        super.setUnregisteredFullName(post.getUnregisteredFullName());
        super.setUnregisteredEmail(post.getUnregisteredEmail());
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}