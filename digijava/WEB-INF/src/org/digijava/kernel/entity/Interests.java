/*
 *   Interests.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 7, 2003
 *   CVS-ID: $Id: Interests.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.entity;

import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;
import java.io.Serializable;
import java.util.Date;

public class Interests
    implements Serializable {

    private Long id;
    private Site site;
    private boolean receiveAlerts;
    private ContentAlert contentAlert;
    private String siteUrl;
    private String siteDescription;
    private User user;
    private Date joinDate;
    private Long addedBy;
    private Date lastAlertDate;
    private Date nextAlertDate;

    public Interests() {}

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ContentAlert getContentAlert() {
        return contentAlert;
    }

    public boolean isReceiveAlerts() {
        return receiveAlerts;
    }

    public void setContentAlert(ContentAlert contentAlert) {
        this.contentAlert = contentAlert;
    }

    public void setReceiveAlerts(boolean receiveAlerts) {
        this.receiveAlerts = receiveAlerts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }

    public Date getLastAlertDate() {
        return lastAlertDate;
    }

    public void setLastAlertDate(Date lastAlertDate) {
        this.lastAlertDate = lastAlertDate;
    }

    public Date getNextAlertDate() {
        return nextAlertDate;
    }

    public void setNextAlertDate(Date nextAlertDate) {
        this.nextAlertDate = nextAlertDate;
    }

}