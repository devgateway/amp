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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserAccountForm
    extends ActionForm {

    private Locale navigationLanguage;
    private Collection contentLanguages;

    private Collection topics;
    private String[] selectedTopics;

    private String firstName;
    private String lastName;
    private String email;
    private String mailingAddress;
    private String organizationName;
    private String organizationType;
    private String organizationTypeOther;
    private String countryOfResidence;
    private String url;

    private boolean displayMyProfile;
    private boolean receiveNewsletter;
    private String sourceUrl;

    private Long siteId;

    public Locale getNavigationLanguage() {
        return navigationLanguage;
    }

    public void setNavigationLanguage(Locale navigationLanguage) {
        this.navigationLanguage = navigationLanguage;
    }

    public Collection getContentLanguages() {
        return contentLanguages;
    }

    public void setContentLanguages(Collection contentLanguages) {
        this.contentLanguages = contentLanguages;
    }

    public boolean isDisplayMyProfile() {
        return displayMyProfile;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
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

    public String getOrganizationType() {
        return organizationType;
    }

    public String getUrl() {
        return url;
    }

    public boolean isReceiveNewsletter() {
        return receiveNewsletter;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setDisplayMyProfile(boolean displayMyProfile) {
        this.displayMyProfile = displayMyProfile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public void setReceiveNewsletter(boolean receiveNewsletter) {
        this.receiveNewsletter = receiveNewsletter;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getSelectedTopics() {
        return selectedTopics;
    }

    public void setSelectedTopics(String[] selectedTopics) {
        this.selectedTopics = selectedTopics;
    }

    public Collection getTopics() {
        return topics;
    }

    public void setTopics(Collection topics) {
        this.topics = topics;
    }

    public Long getSiteId() {
        return siteId;
    }

    public String getOrganizationTypeOther() {
        return organizationTypeOther;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void reset(ActionMapping actionMapping,
                      HttpServletRequest httpServletRequest) {
        siteId = null;

    }

}
