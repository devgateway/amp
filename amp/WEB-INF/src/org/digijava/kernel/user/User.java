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

package org.digijava.kernel.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.*;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.kernel.ampapi.endpoints.common.valueproviders.UserValueProvider;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.util.Identifiable;
import javax.persistence.*;
@InterchangeableValue(UserValueProvider.class)
@javax.persistence.Entity
@Table(name = "DG_USER")
public class User
    extends org.digijava.kernel.entity.Entity implements Serializable, Comparable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_user_seq")
    @SequenceGenerator(name = "dg_user_seq", sequenceName = "dg_user_seq", allocationSize = 1)    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "CREATION_IP")
    private String creationIP;

    @Column(name = "LAST_MODIFIED")
    private Date lastModified;

    @Column(name = "MODIFYING_IP")
    private String modifyingIP;

    @Column(name = "FIRST_NAMES")
    private String firstNames;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "EMAIL_VERIFIED")
    private boolean emailVerified;

    @Column(name = "EMAIL_BOUNCING")
    private boolean emailBouncing;

    @Column(name = "NO_ALERTS_UNTIL")
    private Date noAlertsUntil;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SALT")
    private String salt;

    @Column(name = "PASS_QUESTION")
    private String passQuestion;

    @Column(name = "PASS_ANSWER")
    private String passAnswer;

    @Column(name = "URL")
    private String url;

    @Column(name = "BANNED")
    private boolean banned;

    @Column(name = "REFERRAL")
    private String referral;

    @Column(name = "ORGANIZATION_TYPE_OTHER", columnDefinition = "text")
    private String organizationTypeOther;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ISO")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private AmpCategoryValueLocations region;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image photo;

    @ManyToOne
    @JoinColumn(name = "ORGANIZATION_TYPE_ID")
    private OrganizationType organizationType;

    @ManyToOne
    @JoinColumn(name = "REGISTRATION_LANGUAGE")
    private Locale registerLanguage;

    @Column(name = "GLOBAL_ADMIN")
    private boolean globalAdmin;

    @ManyToOne
    @JoinColumn(name = "user_ext_id")
    private AmpUserExtension userExtension;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Interests> interests = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DG_USER_GROUP",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private Set<Group> groups = new HashSet<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<UserContactInfo> contacts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "REGISTERED_THROUGH")
    private Site registeredThrough;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DG_USER_ORGS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ORG_ID"))
    private Set<AmpOrganisation> assignedOrgs = new HashSet<>();

    @Column(name = "PLEDGER")
    private boolean pledger;

    @Column(name = "PLEDGE_SUPER_USER", columnDefinition = "boolean default false")
    private boolean pledgeSuperUser;

    @Column(name = "PASSWORD_CHANGED_AT")
    private Date passwordChangedAt;

    @Column(name = "EXEMPT_FROM_DATA_FREEZING")
    private boolean exemptFromDataFreezing;

    @Column(name = "notification_email")
    private String notificationEmail;

    @Column(name = "notification_email_enabled")
    private Boolean notificationEmailEnabled;
    @Transient

    private Subject subject;
    @Transient

    private java.sql.Clob bio;
    @Transient

    private Image portrait;
    @Transient

    private String organizationName;
    @Transient

    private HashMap sitePreferences;
    @Transient

    private HashMap siteContentLocales;
    @Transient

    private UserPreferences userPreference;
    @Transient

    private UserLangPreferences userLangPreferences;


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
        //this.registeredThrough = Session.site;

    }

    @Override
    public Object getIdentifier() {
        return id;
    }

    /**
     * @deprecated do not call this method directly. use
     * UserUtils.getUserSubject(), UserUtils.fillUserSubject(),
     * RequestUtils.getSubject() methids instead
     * @return Subject subject, associated with the current user (filled by
     * RequestProcessor)
     */
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setEmailBouncing(boolean emailBouncing) {
        this.emailBouncing = emailBouncing;
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

//    public Set getContacts() {
//        return contacts;
//    }

    public void setInterests(Set interests) {
        this.interests = interests;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }

//    public void setContacts(Set contacts) {
//        this.contacts = contacts;
//    }

    /**
     * @return the pledger
     */
    public Boolean getPledger() {
        return pledger;
    }

    /**
     * @param pledger the pledger to set
     */
    public void setPledger(Boolean pledger) {
        this.pledger = pledger;
    }

    public Boolean getPledgeSuperUser() {
        return pledgeSuperUser;
    }

    public void setPledgeSuperUser(Boolean pledgeSuperUser) {
        this.pledgeSuperUser = pledgeSuperUser;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if(o == null || !(o instanceof User) ) return -1;
        User aux = (User)o;
        return this.getName().compareTo(aux.getName());
    }

    public Set<AmpOrganisation> getAssignedOrgs() {
        return assignedOrgs;
    }
     
    public void setAssignedOrgs(Set<AmpOrganisation> assignedOrgs) {
        this.assignedOrgs = assignedOrgs;
    }

    public void setUserExtension(AmpUserExtension userExt) {
        this.userExtension = userExt;
    }
    
    public AmpUserExtension getUserExtension() {
        return this.userExtension;
    }
    
    public boolean hasVerifiedOrganizationId(Long ampOrgId) {
        if(ampOrgId == null) return false;
        //If it's not there, check in the Set<AmpOrganisation> assignedOrgs
        Iterator<AmpOrganisation> it = this.assignedOrgs.iterator();
        while(it.hasNext()){
            AmpOrganisation currentOrganization = it.next();
            if(currentOrganization.getAmpOrgId().equals(ampOrgId))
                return true;
        }
        return false;
    }

    /**
     * Checks if user has a verified org and the org is role donor
     * @return
     */
    public boolean hasVerifiedDonor(){
        if (this.assignedOrgs.size() == 0) {
            return false;
        }

        Iterator<AmpOrganisation> it = this.assignedOrgs.iterator();
        while (it.hasNext()) {
            AmpOrganisation currentOrganization = it.next();
            if (org.digijava.module.aim.util.DbUtil.hasDonorRole(currentOrganization.getAmpOrgId()))
                return true;
        }
        return false;
    }
    public boolean hasNationalCoordinatorGroup(){
        boolean result = false;
        Set<Group> groups = this.groups;
        for (Group group : groups) {
            if (group.isNationalCoordinatorGroup()) {
                result = true;
                break;
            }
        }
        return result;
    }
    public AmpCategoryValueLocations getRegion() {
        return region;
    }

    public void setRegion(AmpCategoryValueLocations region) {
        this.region = region;
    }

    /**
     * @return the passwordChangedAt
     */
    public Date getPasswordChangedAt() {
        return passwordChangedAt;
    }

    /**
     * @param passwordChangedAt the passwordChangedAt to set
     */
    public void setPasswordChangedAt(Date passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public void updateLastModified() {
        setLastModified(new Date());
    }

    public Boolean getExemptFromDataFreezing() {
        return exemptFromDataFreezing;
    }

    public void setExemptFromDataFreezing(Boolean exemptFromDataFreezing) {
        this.exemptFromDataFreezing = exemptFromDataFreezing;
    }

    public Boolean isNotificationEmailEnabled() {
        return notificationEmailEnabled;
    }

    public void setNotificationEmailEnabled(Boolean notificationEmailEnabled) {
        this.notificationEmailEnabled = notificationEmailEnabled;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }
    
    /**
     * Get the email used for notification. 
     * If {@link #notificationEmailEnabled} is true, the {@link #notificationEmail} is returned.
     * Else, the #{@link #email}.
     * 
     * @return email address used for notification
     */
    public String getEmailUsedForNotification() {
        if (notificationEmailEnabled != null && notificationEmailEnabled) {
            return notificationEmail;
        }
        
        return email;
    }

}
