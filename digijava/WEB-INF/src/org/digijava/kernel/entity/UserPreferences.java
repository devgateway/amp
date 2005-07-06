/*
*   UserPreferences.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: UserPreferences.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import java.io.Serializable;

public class UserPreferences implements Serializable {

    private boolean publicProfile;
    private boolean receiveAlerts;
    private UserPreferencesPK id;
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
