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

import java.util.Set;
import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;
import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DG_USER_LANG_PREFERENCES")
public class UserLangPreferences implements Serializable{
    @Id
    @EmbeddedId
    private UserPreferencesPK id;

    @ManyToMany
    @JoinTable(
            name = "DG_USER_CONT_LANGUAGE_MAP",
            joinColumns = {
                    @JoinColumn(name = "USER_ID"),
                    @JoinColumn(name = "SITE_ID")
            },
            inverseJoinColumns = @JoinColumn(name = "CODE")
    )
    private Set<Locale> contentLanguages = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "NAV_LANGUAGE")
    private Locale navigationLanguage;

    @ManyToOne
    @JoinColumn(name = "ALERTS_LANGUAGE")
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
