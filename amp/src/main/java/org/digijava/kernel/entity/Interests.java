/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.entity;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;

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
