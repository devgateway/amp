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

package org.digijava.module.um.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.digijava.kernel.entity.Interests;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserUpdateForm
    extends ValidatorForm {

    private String firstNames;
    private String lastName;
    private String email;
    private String mailingAddress;
    private String organizationName;
    private Collection organizationType;
    private String selectedOrganizationType;
    private String webSite;
    private Collection countryResidence;
    private String selectedCountryResidence;
    private boolean newsLetterRadio;
    private boolean membersProfile;
    private Collection contentLanguages;
    private Collection navigationLanguages;
    private Collection contentAlerts;
    private Collection interests;
    private String selectedLanguage;
    private String[] contentSelectedLanguages;
    private boolean highlight;
    private String organizationTypeOther;

    // topics
    private ArrayList receiveTopics;
    private String[] selectedTopics = {};
    private Collection topics;
    // --------
    private Long siteId;

    public String[] getSelectedTopics() {
        return this.selectedTopics;
    }

    public void setSelectedTopics(String[] selectedTopics) {
        this.selectedTopics = selectedTopics;
    }

    public Collection getCountryResidence() {
        return (this.countryResidence);
    }

    public String getEmail() {
        return email;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean getMembersProfile() {
        return membersProfile;
    }

    public void setMembersProfile(boolean membersProfile) {
        this.membersProfile = membersProfile;
    }

    public void setCountryResidence(Collection countryResidence) {

        this.countryResidence = countryResidence;

    }

    public Collection getTopics() {
        return topics;
    }

    public void setTopics(Collection topics) {
        this.topics = topics;
    }

    public Collection getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Collection organizationType) {
        this.organizationType = organizationType;
    }

    public boolean isNewsLetterRadio() {
        return newsLetterRadio;
    }

    public void setNewsLetterRadio(boolean newsLetterRadio) {
        this.newsLetterRadio = newsLetterRadio;
    }

    public String getSelectedCountryResidence() {
        return selectedCountryResidence;
    }

    public void setSelectedCountryResidence(String selectedCountryResidence) {
        this.selectedCountryResidence = selectedCountryResidence;
    }

    public String getSelectedOrganizationType() {
        return selectedOrganizationType;
    }

    public void setSelectedOrganizationType(String selectedOrganizationType) {
        this.selectedOrganizationType = selectedOrganizationType;
    }

    public Collection getNavigationLanguages() {
        return navigationLanguages;
    }

    public void setNavigationLanguages(Collection navigationLanguages) {
        this.navigationLanguages = navigationLanguages;
    }

    public Collection getContentLanguages() {
        return contentLanguages;
    }

    public void setContentLanguages(Collection contentLanguages) {
        this.contentLanguages = contentLanguages;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedLanguage() {
        return this.selectedLanguage;
    }

    public String[] getContentSelectedLanguages() {
        return this.contentSelectedLanguages;
    }

    public void setContentSelectedLanguages(String[] contentSelectedLanguages) {
        this.contentSelectedLanguages = contentSelectedLanguages;
    }

    /**
     * Reset all input
     *
     * @param actionMapping
     * @param httpServletRequest
     */
    public void reset(ActionMapping actionMapping,
                      HttpServletRequest httpServletRequest) {
        siteId = null;
        // reset topics
        if (receiveTopics != null) {
            Iterator iter = receiveTopics.iterator();
            while (iter.hasNext()) {
                Interests item = (Interests) iter.next();
                item.setReceiveAlerts(false);
            }
        }

        if (topics != null) {
            Iterator iter = topics.iterator();
            while (iter.hasNext()) {
                Interests item = (Interests) iter.next();
                item.setReceiveAlerts(false);
            }
        }
        // ------------

        if (selectedTopics != null) {
            for (int i = 0; i < selectedTopics.length; i++) {
                selectedTopics[i] = null;
            }
        }

        organizationTypeOther = null;

        this.highlight = false;
    }

    /**
     * Validate user input
     *
     * @param actionMapping
     * @param httpServletRequest
     * @return
     */
    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        ActionErrors errors = super.validate(actionMapping, httpServletRequest);

        return errors;
    }

    public Collection getContentAlerts() {
        return contentAlerts;
    }

    public void setContentAlerts(Collection contentAlerts) {
        this.contentAlerts = contentAlerts;
    }

    public Collection getInterests() {
        return interests;
    }

    public void setInterests(Collection interests) {
        this.interests = interests;
    }

    public ArrayList getReceiveTopics() {
        return receiveTopics;
    }

    public void setReceiveTopics(ArrayList receiveTopics) {
        this.receiveTopics = receiveTopics;
    }

    public Interests getReceiveTopic(int index) {
        Interests info = null;
        int actualSize = receiveTopics.size();
        if (index >= actualSize) {
            // Expand the list
            for (int i = 0; i <= index - actualSize; i++) {
                receiveTopics.add(new Interests());
            }
        }

        return (Interests) receiveTopics.get(index);
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public String getOrganizationTypeOther() {
        return organizationTypeOther;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }
}
