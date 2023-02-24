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

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserProfileForm
    extends ValidatorForm {

    private String firstNames;
    private String lastName;
    private String membersinceMonth;
    private String membersinceDay;
    private String membersinceYear;
    private String organizationName;
    private String bioText;
    private Collection memberShips;
    private boolean contact;
    private boolean memberShip;
    private Long activeUserId;
    private Long activeUserImage;
    private Collection topics;
    private String[] selectedTopics;
    private String url;
    private String mailingAddress;

    private boolean owner;
    private boolean publicProfile;
    private List sites;

    private Long siteId;

    public String getBioText() {
        return bioText;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMembersinceDay() {
        return membersinceDay;
    }

    public String getMembersinceYear() {
        return membersinceYear;
    }

    public String getMembersinceMonth() {
        return membersinceMonth;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setBioText(String bioText) {
        this.bioText = bioText;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMembersinceDay(String membersinceDay) {
        this.membersinceDay = membersinceDay;
    }

    public void setMembersinceMonth(String membersinceMonth) {
        this.membersinceMonth = membersinceMonth;
    }

    public void setMembersinceYear(String membersinceYear) {
        this.membersinceYear = membersinceYear;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(Long activeUserId) {
        this.activeUserId = activeUserId;
    }

    public boolean isContact() {
        return contact;
    }

    public boolean isMemberShip() {
        return memberShip;
    }

    public Collection getMemberShips() {
        return memberShips;
    }

    public void setMemberShips(Collection memberShips) {
        this.memberShips = memberShips;
    }

    public void setMemberShip(boolean memberShip) {
        this.memberShip = memberShip;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
    }

    public Long getActiveUserImage() {
        return activeUserImage;
    }

    public void setActiveUserImage(Long activeUserImage) {
        this.activeUserImage = activeUserImage;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setBioText(null);
        setFirstNames(null);
        setLastName(null);
        setMembersinceDay(null);
        setMembersinceMonth(null);
        setMembersinceYear(null);
        setOrganizationName(null);
        setContact(false);
        setMemberShip(false);
        setActiveUserImage(null);
        setSelectedTopics(null);
        setTopics(null);
        setActiveUserId(null);

        setOwner(false);
        setPublicProfile(false);
        setSites(null);

        siteId = null;
    }

    public String[] getSelectedTopics() {
        return selectedTopics;
    }

    public Collection getTopics() {
        return topics;
    }

    public void setSelectedTopics(String[] selectedTopics) {
        this.selectedTopics = selectedTopics;
    }

    public void setTopics(Collection topics) {
        this.topics = topics;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    public List getSites() {
        return sites;
    }

    public void setSites(List sites) {
        this.sites = sites;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

}
