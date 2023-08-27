package org.digijava.module.um.form;

/*
 *   UserRegisterForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: AddUserForm.java,v 1.2 2008-11-26 01:25:52 msotero Exp $
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


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AddUserForm  extends ValidatorForm {
    private Collection assignedWorkspaces;
    private boolean addWorkspace;
    private Long teamId;
    private Long role;
    private Long teamMemberId;
    private Collection workspaces;
    private Collection ampRoles;
    private boolean sendEmail;
    private boolean registrationByEmail;
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
    private Long selectedOrgType;       // added for Donor access
    private Long selectedOrgGroup;      // added for Donor access
    //private Collection orgGroupColl;  // added for Donor access
    private TreeSet orgGroupColl;   // added for Donor access
    //private Collection orgTypeColl;       // added for Donor access
    private TreeSet orgTypeColl;        // added for Donor access    
    private Collection orgColl;         // added for Donor access
    private String orgGrp;              // hidden form field - added for Donor access
    private String orgType;             // hidden form field - added for Donor access
    private Collection howDidyouhear;
    private String howDidyouSelect;
    private String webSite;
    private Collection countryResidence;
    private String selectedCountryResidence;
    private boolean newsLetterRadio;
    private boolean membersProfile;
    private Boolean pledger;
    private Boolean pledgeSuperUser;
    private Collection contentLanguages;
    private Collection navigationLanguages;
    private Collection truBudgetIntents;
    private Collection selectedTruBudgetIntents;
    private String selectedLanguage;
    private String[] contentSelectedLanguages;
    private String organizationTypeOther;
    private Long selectedOrganizationId;
    private HashMap<String,String> errors=null;
//FocusBoxes
    private String[] selectedItems = {};
    // private String[] items ;
    private Collection items;

///
    private String[] topicselectedItems = {};
    private Collection topicitems;

    private Long siteId;    
    private Boolean exemptFromDataFreezing;
    
    private boolean nationalCoordinator = false;
    
    private Boolean notificationEmailEnabled;
    private String notificationEmail;
    
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
    
    /**
     * @return Returns the selectedOrgType.
     */
    public Long getSelectedOrgType() {
        return selectedOrgType;
    }
    /**
     * @param selectedOrgType The selectedOrgType to set.
     */
    public void setSelectedOrgType(Long selectedOrgType) {
        this.selectedOrgType = selectedOrgType;
    }
    /**
     * @return Returns the selectedOrgGroup.
     */
    public Long getSelectedOrgGroup() {
        return selectedOrgGroup;
    }
    /**
     * @param selectedOrgGroup The selectedOrgGroup to set.
     */
    public void setSelectedOrgGroup(Long selectedOrgGroup) {
        this.selectedOrgGroup = selectedOrgGroup;
    }
    
    /**
     * @return Returns the orgGroupColl.
     */
    public Collection getOrgGroupColl() {
        return orgGroupColl;
    }
    /**
     * @param orgGroupColl The orgGroupColl to set.
     */
    public void setOrgGroupColl(Collection orgGroupColl) {
        
        TreeSet aux=new TreeSet();
        aux.addAll(orgGroupColl);
        this.orgGroupColl = aux;
    }
    
    /**
     * @return Returns the orgTypeColl.
     */
    public Collection getOrgTypeColl() {
        return orgTypeColl;
    }
    /**
     * @param orgTypeColl The orgTypeColl to set.
     */
    public void setOrgTypeColl(Collection orgTypeColl) {
        TreeSet aux=new TreeSet();
        aux.addAll(orgTypeColl);
        this.orgTypeColl = aux;
    }
    /**
     * @return Returns the orgColl.
     */
    public Collection getOrgColl() {
        return orgColl;
    }
    /**
     * @param orgColl The orgColl to set.
     */
    public void setOrgColl(Collection orgColl) {
        this.orgColl = orgColl;
    }
    
    /**
     * @return Returns the orgGrp.
     */
    public String getOrgGrp() {
        return orgGrp;
    }
    /**
     * @param orgGrp The orgGrp to set.
     */
    public void setOrgGrp(String orgGrp) {
        this.orgGrp = orgGrp;
    }
    /**
     * @return Returns the orgType.
     */
    public String getOrgType() {
        return orgType;
    }
    /**
     * @param orgType The orgType to set.
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
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
        selectedOrganizationId=null;
        selectedOrgGroup=null;
        selectedOrgType=null;
        setOrgGrp(null);
        setOrgType(null);
        setNotificationEmailEnabled(false);
        setNotificationEmail(null);
        setPledger(false);
        setPledgeSuperUser(false);
 //       setWebSite(null);

        organizationTypeOther = null;
        newsLetterRadio = true;
        membersProfile = true;
        topicselectedItems = null;

        siteId = null;
        addWorkspace=false;
        nationalCoordinator = false;
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

        ActionErrors olderrors = new ActionErrors();
        errors = new HashMap<String, String>();

            if ( (this.getFirstNames() == null) ||
                this.getFirstNames().trim().length() == 0) {
                errors.put("error.registration.FirstNameBlank", "First Name is Blank");
            }
            if ( (this.getLastName() == null) ||
                this.getLastName().trim().length() == 0) {
                
                errors.put("error.registration.LastNameBlank", "LastName is Blank");
            }
            if ( (this.getEmail() == null) || this.getEmail().trim().length() == 0) {
                errors.put("error.registration.noemail", "Please enter a valid email address.");
                   }
            if ( (this.getPassword() == null) ||
                    this.getPassword().trim().length() == 0) {
                    errors.put("error.registration.passwordBlank", "Password field is Blank");
                }            
            

        if ( (this.getEmail() != null) && this.getEmail().trim().length() != 0) {
            if (! (this.getEmail().equals(this.getEmailConfirmation()))) {
                errors.put("error.registration.noemailmatch", "Emails in both fields must be the same");
            }
        }

        if ( (this.getPassword() != null) &&
            this.getPassword().trim().length() != 0) {
            if (! (this.getPassword().equals(this.getPasswordConfirmation()))) {
                errors.put("error.registration.NoPasswordMatch", "Passwords in both fields must be the same");
            }
        }      
       if(selectedOrgType==null||selectedOrgType.equals(-1l)){
           errors.put("error.registration.enterorganizationother", "Please enter Organization Type");
       }
       
        
        if (null == selectedOrgGroup || selectedOrgGroup .equals(-1l)) {
            errors.put("error.registration.NoOrgGroup", "Please Select Organization Group");
        }

        if ( selectedOrganizationId==null || selectedOrganizationId .equals(-1l)) {
            errors.put("error.registration.NoOrganization", "Please Select Organization");
        }
        
        return errors.isEmpty()? null : olderrors ;
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

    public Long getSelectedOrganizationId() {
        return selectedOrganizationId;
    }

    public void setSelectedOrganizationId(Long selectedOrganizationId) {
        this.selectedOrganizationId = selectedOrganizationId;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }
    public void addError(String key, String value) {
        this.errors.put(key, value) ;
    }

    public void clearMessages(){
        this.errors.clear();
    }
    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
    public Collection getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(Collection workspaces) {
        this.workspaces = workspaces;
    }

    public Collection getAmpRoles() {
        return ampRoles;
    }

    public void setAmpRoles(Collection ampRoles) {
        this.ampRoles = ampRoles;
    }
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
    public boolean isAddWorkspace() {
        return addWorkspace;
    }

    public void setAddWorkspace(boolean addWorkspace) {
        this.addWorkspace = addWorkspace;
    }
    public Collection getAssignedWorkspaces() {
        return assignedWorkspaces;
    }

    public void setAssignedWorskpaces(Collection assignedWorkspaces) {
        this.assignedWorkspaces = assignedWorkspaces;
    }
    
    public void setAssignedWorkspaces(Collection assignedWorkspaces) {
        this.assignedWorkspaces = assignedWorkspaces;
    }

    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
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

    public boolean isRegistrationByEmail() {
        return registrationByEmail;
    }

    public void setRegistrationByEmail(boolean registrationByEmail) {
        this.registrationByEmail = registrationByEmail;
    }

    public Boolean getExemptFromDataFreezing() {
        return exemptFromDataFreezing;
    }

    public void setExemptFromDataFreezing(Boolean exemptFromDataFreezing) {
        this.exemptFromDataFreezing = exemptFromDataFreezing;
    }

    public boolean getNationalCoordinator() {
        return nationalCoordinator;
    }

    public void setNationalCoordinator(boolean nationalCoordinator) {
        this.nationalCoordinator = nationalCoordinator;
    }

    public Boolean getNotificationEmailEnabled() {
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

    public Collection getTruBudgetIntents() {
        return truBudgetIntents;
    }

    public void setTruBudgetIntents(Collection truBudgetIntents) {
        this.truBudgetIntents = truBudgetIntents;
    }

    public Collection getSelectedTruBudgetIntents() {
        return selectedTruBudgetIntents;
    }

    public void setSelectedTruBudgetIntents(Collection selectedTruBudgetIntents) {
        this.selectedTruBudgetIntents = selectedTruBudgetIntents;
    }
}
