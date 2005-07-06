/*
 *   UserLangPreferences.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 09, 2003
 * 	 CVS-ID: $Id: UserLangPreferences.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.util.Set;
import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;
import java.io.Serializable;

public class UserLangPreferences implements Serializable {

    private UserPreferencesPK id;
    private Locale navigationLanguage;
    private Set contentLanguages;
    private Locale alertsLanguage;

    public UserLangPreferences() {}

    public UserLangPreferences(User user, Site site) {
        this.id = new UserPreferencesPK(user, site);
    }

    public Locale getAlertsLanguage() {
        return alertsLanguage;
    }

    public void setAlertsLanguage(Locale alertsLanguage) {
        this.alertsLanguage = alertsLanguage;
    }

    public Set getContentLanguages() {
        return contentLanguages;
    }

    public void setContentLanguages(Set contentLanguages) {
        this.contentLanguages = contentLanguages;
    }

    public Locale getNavigationLanguage() {
        return navigationLanguage;
    }

    public void setNavigationLanguage(Locale navigationLanguage) {
        this.navigationLanguage = navigationLanguage;
    }

    public UserPreferencesPK getId() {
        return id;
    }

    public void setId(UserPreferencesPK id) {
        this.id = id;
    }
}