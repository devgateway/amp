/*
 *   User.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 2, 2003
 *	 CVS-ID: $Id: User.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.user;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import javax.security.auth.Subject;

import org.digijava.kernel.Entity;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Image;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.OrganizationType;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
//import org.digijava.kernel.entity.Organization;
import org.digijava.kernel.request.Site;

public class User
    extends Entity implements Serializable {

    private Subject subject;
    private String firstNames;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private boolean emailBouncing;
    private Date noAlertsUntil;
    private String password;
    private String salt;
    private String passQuestion;
    private String passAnswer;
    private String url;
    private Date lastVisit;
    private Date secondToLastVisit;
    private long numberOfSessions;
    private boolean active;
    private boolean banned;
    private Site registeredThrough;
    private Set interests;
    private java.sql.Clob bio;
    private Image portrait;
    private String organizationName;
    private OrganizationType organizationType;
    private String referral;
    private Country country;
    private HashMap sitePreferences;
    private Set groups;
    private HashMap siteContentLocales;
    private String address;
    private Image photo;
    private UserPreferences userPreference;
    private UserLangPreferences userLangPreferences;
    private Locale registerLanguage;
    private boolean globalAdmin;
    private String organizationTypeOther;

    public User() {}

    public User(String email, String firstNames, String lastName) {
        /**
         * Should throw InvalidUserEmailException if unique email contraint is violated
         */
        super(firstNames + " " + lastName);
        this.email = email;
        this.firstNames = firstNames;
        this.lastName = lastName;
        this.emailVerified = false;
        this.emailBouncing = false;
        this.noAlertsUntil = new Date(java.lang.System.currentTimeMillis());
        //this.password = ;
        //this.salt = ;
        //this.passQuestion = passQuestion;
        //this.passAnswer = passAnswer;
        //this.url = url;
        this.lastVisit = new Date(java.lang.System.currentTimeMillis());
        this.secondToLastVisit = new Date(java.lang.System.currentTimeMillis());
        this.numberOfSessions = 1;
        this.active = false;
        //this.registeredThrough = Session.site;

    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject s) {
        this.subject = s;
    }

    public String getName() {
        return new String(firstNames + " " + lastName);
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        /**
         * Should throw InvalidUserEmailException if unique email contraint is violated
         */
        this.email = email;
    }

    public boolean isEmailBouncing() {
        return emailBouncing;
    }

    public Date getNoAlertsUntil() {
        return noAlertsUntil;
    }

    public void setNoAlertsUntil(Date noAlertsUntil) {
        this.noAlertsUntil = noAlertsUntil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassQuestion() {
        return passQuestion;
    }

    public void setPassQuestion(String passQuestion) {
        this.passQuestion = passQuestion;
    }

    public String getPassAnswer() {
        return passAnswer;
    }

    public void setPassAnswer(String passAnswer) {
        this.passAnswer = passAnswer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public Date getSecondToLastVisit() {
        return secondToLastVisit;
    }

    public void setSecondToLastVisit(Date secondToLastVisit) {
        this.secondToLastVisit = secondToLastVisit;
    }

    public long getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(long numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public void setActivate(boolean a) {
        this.active = a;
    }

    public boolean isActivate() {
        return this.active;
    }

    public boolean isRegistrationComplete() {
        return false;
    }

    public boolean isBanned() {
        return banned;
    }

    public boolean setRegistrationComplete() {
        return false;
    }

    public boolean startPasswordReset() {
        return false;
    }

    public String getPreference(Site site, String name) {
        return (String) ( (HashMap)this.sitePreferences.get(site)).get(name);
    }

    public void addPreference(Site site, String name, String value) {
        HashMap map = (HashMap)this.sitePreferences.get(site);
        map.put(name, value);
    }

    public void removePreference(Site site, String name) {
        ( (HashMap)this.sitePreferences.get(site)).remove(name);
    }

    public Site getRegisteredThrough() {
        return registeredThrough;
    }

    public Set getGroups() {
        return groups;
    }

    public Image getPortrait() {
        return portrait;
    }

    public void setPortrait(Image portrait) {
        this.portrait = portrait;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public HashSet getContentLocales(Site site) {
        return (HashSet)this.siteContentLocales.get(site);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setEmailBouncing(boolean emailBouncing) {
        this.emailBouncing = emailBouncing;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setGroups(Set groups) {
        this.groups = groups;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public java.sql.Clob getBio() {
        return bio;
    }

    public void setBio(java.sql.Clob bio) {
        this.bio = bio;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

    public UserPreferences getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(UserPreferences userPreference) {
        this.userPreference = userPreference;
    }

    public UserLangPreferences getUserLangPreferences() {
        return userLangPreferences;
    }

    public void setUserLangPreferences(UserLangPreferences userLangPreferences) {
        this.userLangPreferences = userLangPreferences;
    }

    public void setRegisteredThrough(Site registeredThrough) {
        this.registeredThrough = registeredThrough;
    }

    public Locale getRegisterLanguage() {
        return registerLanguage;
    }

    public void setRegisterLanguage(Locale registerLanguage) {
        this.registerLanguage = registerLanguage;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }

    public Set getInterests() {
        return interests;
    }

    public String getOrganizationTypeOther() {
        return organizationTypeOther;
    }

    public void setInterests(Set interests) {
        this.interests = interests;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }

}
