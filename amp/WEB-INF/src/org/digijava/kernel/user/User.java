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

import org.digijava.kernel.ampapi.endpoints.common.valueproviders.UserValueProvider;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.*;
import org.digijava.kernel.request.Site;
import org.digijava.module.aim.annotations.interchange.InterchangeableValue;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.util.Identifiable;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.*;

@InterchangeableValue(UserValueProvider.class)
public class User
    extends Entity implements Serializable, Comparable, Identifiable {

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
    private boolean banned;
    private Boolean pledger;
    private Boolean pledgeSuperUser;
    private Site registeredThrough;
    private Set interests;
    private java.sql.Clob bio;
    private Image portrait;
    private String organizationName;
    private OrganizationType organizationType;
    private String referral;
    private Country country;
    private AmpCategoryValueLocations region;
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
    private Set contacts;
    private AmpUserExtension userExtension;
    private Boolean exemptFromDataFreezing;
    private Boolean notificationEmailEnabled = false;
    private String notificationEmail;

    private Set<AmpOrganisation> assignedOrgs;
    private Date passwordChangedAt;

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

    public Set getContacts() {
        return contacts;
    }

    public void setInterests(Set interests) {
        this.interests = interests;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }

    public void setContacts(Set contacts) {
        this.contacts = contacts;
    }

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
