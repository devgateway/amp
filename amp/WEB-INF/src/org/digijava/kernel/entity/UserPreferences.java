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

import java.io.Serializable;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "DG_USER_PREFERENCES")
public class UserPreferences implements Serializable{

    @Column(name = "SHOW_PUBLIC_P")
    private boolean publicProfile;
    @Column(name = "RECEIVE_ALERTS_P")
    private boolean receiveAlerts;
    @EmbeddedId
    @Id
    private UserPreferencesPK id;
    @Column(name = "BIOGRAPHY", columnDefinition = "text")
    private String biography;

    public UserPreferences() {}

    public UserPreferences(User user, Site site) {
        this.id = new UserPreferencesPK(user, site);
    }

    public boolean isPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    public boolean isReceiveAlerts() {
        return receiveAlerts;
    }

    public void setReceiveAlerts(boolean receiveAlerts) {
        this.receiveAlerts = receiveAlerts;
    }

    public UserPreferencesPK getId() {
        return id;
    }

    public void setId(UserPreferencesPK id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

}
