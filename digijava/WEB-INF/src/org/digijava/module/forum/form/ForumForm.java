/*
*   ForumForm.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: ForumForm.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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


package org.digijava.module.forum.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.forum.dbentity.Forum;
import java.util.List;

public class ForumForm
    extends ActionForm {

    private Forum forum;
    private int newThreads;
    private int totalThreads;
    private int newPosts;
    private int totalPosts;
    private List lastPosts;

    public ForumForm() {
    }
    public Forum getForum() {
        return forum;
    }
    public void setForum(Forum forum) {
        this.forum = forum;
    }
    public int getNewPosts() {
        return newPosts;
    }
    public void setNewPosts(int newPosts) {
        this.newPosts = newPosts;
    }
    public int getNewThreads() {
        return newThreads;
    }
    public void setNewThreads(int newThreads) {
        this.newThreads = newThreads;
    }
    public int getTotalPosts() {
        return totalPosts;
    }
    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }
    public int getTotalThreads() {
        return totalThreads;
    }
    public void setTotalThreads(int totalThreads) {
        this.totalThreads = totalThreads;
    }
    public List getLastPosts() {
        return lastPosts;
    }
    public void setLastPosts(List lastPosts) {
        this.lastPosts = lastPosts;
    }

}