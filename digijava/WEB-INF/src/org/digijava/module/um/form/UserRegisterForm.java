/*
 *   UserRegisterForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: UserRegisterForm.java,v 1.1 2005-07-06 10:34:17 rahul Exp $
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

package org.digijava.module.um.form;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
//
import org.apache.struts.action.ActionErrors;
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

public class UserRegisterForm
    extends ValidatorForm {

    private String firstNames;
    private String lastName;
    private String email;
    private String emailConfirmation;
    private String password;
    private String passwordConfirmation;
    private String mailingAddress;
    private String organizationName;
    private Collection organizationType;
    private String selectedOrganizationType;
    private Collection howDidyouhear;
    private String howDidyouSelect;
    private String webSite;
    private Collection countryResidence;
    private String selectedCountryResidence;
    private boolean newsLetterRadio;
    private boolean membersProfile;
    private Collection contentLanguages;
    private Collection navigationLanguages;
    private String selectedLanguage;
    private String[] contentSelectedLanguages;
    private String organizationTypeOther;

//FocusBoxes
    private String[] selectedItems = {};
    // private String[] items ;
    private Collection items;

///
    private String[] topicselectedItems = {};
    private Collection topicitems;

    private Long siteId;

    public String[] getSelectedItems() {
        return this.selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
        this.selectedItems = selectedItems;
    }

//TopicBoxes

    public Collection getHowDidyouhear() {
        return (this.howDidyouhear);
    }

    public void setHowDidyouhear(Collection howDidyouhear) {
        this.howDidyouhear = howDidyouhear;
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

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
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

    public Collection getItems() {
        return items;
    }

    public void setItems(Collection items) {
        this.items = items;
    }

    public Collection getTopicitems() {
        return topicitems;
    }

    public void setTopicitems(Collection topicitems) {
        this.topicitems = topicitems;
    }

    public String[] getTopicselectedItems() {
        return topicselectedItems;
    }

    public void setTopicselectedItems(String[] topicselectedItems) {
        this.topicselectedItems = topicselectedItems;
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

    public String getHowDidyouSelect() {
        return howDidyouSelect;
    }

    public void setHowDidyouSelect(String howDidyouSelect) {
        this.howDidyouSelect = howDidyouSelect;
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

        setFirstNames(null);
        setLastName(null);
        setEmail(null);
        setEmailConfirmation(null);
        setPassword(null);
        setPasswordConfirmation(null);
        setMailingAddress(null);
        setOrganizationName(null);
 //       setWebSite(null);

        organizationTypeOther = null;
        newsLetterRadio = true;
        membersProfile = true;
        topicselectedItems = null;

        siteId = null;
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

/*       if ( (this.getSelectedCountryResidence() == null) ||
            (this.getSelectedCountryResidence().trim().length() <= 0) ||
            this.getSelectedCountryResidence().trim().equals("-1")) {
            ActionError error = new ActionError(
                "error.registration.noresidence");
            errors.add(null, error);
            }


            if (this.selectedOrganizationType != null &&
                this.selectedOrganizationType.equals("other") &&
                (this.organizationTypeOther == null ||
                 this.organizationTypeOther.trim().length() <= 0)) {

                ActionError error = new ActionError(
                    "error.registration.enterorganizationother");
                errors.add(null, error);
            }*/

        /*        if ( (this.getEmail() == null) || this.getEmail().trim().length() == 0) {
             ActionError error = new ActionError("error.registration.noemail");
                    errors.add(null, error);
                }
         else if (! (this.getEmail().equals(this.getEmailConfirmation()))) {
                    ActionError error = new ActionError(
                        "error.registration.noemailmatch");
                    errors.add(null, error);
                }
                if ( (this.getFirstNames() == null) ||
                    this.getFirstNames().trim().length() == 0) {
                    ActionError error = new ActionError(
                        "error.registration.FirstNameBlank");
                    errors.add(null, error);
                }
                if ( (this.getLastName() == null) ||
                    this.getLastName().trim().length() == 0) {
                    ActionError error = new ActionError(
                        "error.registration.LastNameBlank");
                    errors.add(null, error);
                }
                if ( (this.getSelectedCountryResidence() == null) ||
             (this.getSelectedCountryResidence().trim().length() <= 0)) {
         ActionError error = new ActionError("error.registration.noresidence");
                    errors.add(null, error);
                }
                if ( (this.getPassword() == null) ||
                    this.getPassword().trim().length() == 0) {
                    ActionError error = new ActionError(
                        "error.registration.passwordBlank");
                    errors.add(null, error);
                }
         else if (! (this.getPassword().equals(this.getPasswordConfirmation()))) {
                    ActionError error = new ActionError(
                        "error.registration.NoPasswordMatch");
                    errors.add(null, error);
                }*/

        if ( (this.getEmail() != null) && this.getEmail().trim().length() != 0) {
            if (! (this.getEmail().equals(this.getEmailConfirmation()))) {
                ActionError error = new ActionError(
                    "error.registration.noemailmatch");
                errors.add(null, error);
            }
        }

        if ( (this.getPassword() != null) &&
            this.getPassword().trim().length() != 0) {
            if (! (this.getPassword().equals(this.getPasswordConfirmation()))) {
                ActionError error = new ActionError(
                    "error.registration.NoPasswordMatch");
                errors.add(null, error);
            }
        }

        return errors.isEmpty() ? null : errors;
    }

    public Long getSiteId() {
        return siteId;
    }

    public String getOrganizationTypeOther() {
        return organizationTypeOther;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public void setOrganizationTypeOther(String organizationTypeOther) {
        this.organizationTypeOther = organizationTypeOther;
    }
}
