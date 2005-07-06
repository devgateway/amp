/*
*   Forum.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: Forum.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

import java.util.HashSet;
import java.util.Set;

public class Forum {

    private Forum() {
    }

    public Forum(String siteId, String instanceId) {
        this.siteId = siteId;
        this.instanceId = instanceId;
        this.sections = new HashSet();
    }

    private long id;
    private String siteId;
    private String instanceId;
    private String name;
    private Set sections;
    private int topicsPerPage;
    private int postsPerPage;
    private int systemTimezone;

    private int minMessageLength;
    private int maxImageSize;
    private int maxImageWidth;
    private int maxImageHeigth;
    private int maxUploadSize;
    private int postIntervalLimit;

    private boolean allowUnregisteredUserPost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Set getSections() {
        return sections;
    }

    public void setSections(Set sections) {
        this.sections = sections;
    }
    public int getPostsPerPage() {
        return postsPerPage;
    }
    public void setPostsPerPage(int postsPerPage) {
        this.postsPerPage = postsPerPage;
    }
    public int getSystemTimezone() {
        return systemTimezone;
    }
    public void setSystemTimezone(int systemTimezone) {
        this.systemTimezone = systemTimezone;
    }
    public int getTopicsPerPage() {
        return topicsPerPage;
    }
    public void setTopicsPerPage(int topicsPerPage) {
        this.topicsPerPage = topicsPerPage;
    }

    public int getMaxImageHeigth() {
        return maxImageHeigth;
    }
    public void setMaxImageHeigth(int maxImageHeigth) {
        this.maxImageHeigth = maxImageHeigth;
    }
    public int getMaxImageSize() {
        return maxImageSize;
    }
    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }
    public int getMaxImageWidth() {
        return maxImageWidth;
    }
    public void setMaxImageWidth(int maxImageWidth) {
        this.maxImageWidth = maxImageWidth;
    }
    public int getMaxUploadSize() {
        return maxUploadSize;
    }
    public void setMaxUploadSize(int maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }
    public int getMinMessageLength() {
        return minMessageLength;
    }
    public void setMinMessageLength(int minMessageLength) {
        this.minMessageLength = minMessageLength;
    }
    public int getPostIntervalLimit() {
        return postIntervalLimit;
    }
    public void setPostIntervalLimit(int postIntervalLimit) {
        this.postIntervalLimit = postIntervalLimit;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
  public boolean getAllowUnregisteredUserPost() {
    return allowUnregisteredUserPost;
  }
  public void setAllowUnregisteredUserPost(boolean allowUnregisteredUserPost) {
    this.allowUnregisteredUserPost = allowUnregisteredUserPost;
  }

}