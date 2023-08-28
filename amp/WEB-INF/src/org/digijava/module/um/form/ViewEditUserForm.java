package org.digijava.module.um.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.TruBudgetIntent;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.CountryBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class ViewEditUserForm extends ActionForm {

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.pledger = false;
        this.pledgeSuperUser = false;
        this.nationalCoordinator = false;
        this.exemptFromDataFreezing = false;
        this.notificationEmailEnabled = false;
    }
    
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    private String contacts;
    private String firstNames;
    private String lastName;
    private String name;
    private String url;
    private String mailingAddress;
    
    private Boolean notificationEmailEnabled;
    private String notificationEmail;

    private Long selectedOrgGroupId;
    private Collection<AmpOrgGroup> orgGroups;

    private String selectedOrgTypeId;
    private Collection<AmpOrgType> orgTypes;

    private String selectedOrgName;
    private Long selectedOrgId;
    private Collection<AmpOrganisation> orgs;

    /* this is the attached org related with gateperm OrgRoleGate */
    private Long assignedOrgId;

    private String selectedCountryIso;
    private Collection<CountryBean> countries;

    private Long selectedRegionId;
    private Collection<AmpCategoryValueLocations> regions;
    
    private String selectedLanguageCode;
    private Collection languages;

    private String event;
    private Boolean ban;
    private Boolean pledger;
    private Boolean pledgeSuperUser;
    private String confirmNewPassword;
    private String newPassword;
    private Boolean displaySuccessMessage;
    
    private Collection<AmpOrganisation> assignedOrgs;
    private Long selAssignedOrgs[];
    
    private boolean addWorkspace;
    private Collection workspaces;
    private Collection ampRoles;
    private Collection assignedWorkspaces;
    private Collection<TruBudgetIntent> truBudgetIntents;
    private Long teamId;
    private Long role;
    private boolean emailerror;
    private boolean banReadOnly;
    private Boolean exemptFromDataFreezing;
    private boolean nationalCoordinator;
    private String[] selectedTruBudgetIntents;


    public boolean isEmailerror() {
        return emailerror;
    }

    public void setEmailerror(boolean emailerror) {
        this.emailerror = emailerror;
    }
    
    public Collection<AmpOrganisation> getAssignedOrgs() {
        return assignedOrgs;
    }

    public void setAssignedOrgs(Collection<AmpOrganisation> assignedOrgs) {
        this.assignedOrgs = assignedOrgs;
    }

    public Long[] getSelAssignedOrgs() {
        return selAssignedOrgs;
    }

    public void setSelAssignedOrgs(Long[] selAssignedOrgs) {
        this.selAssignedOrgs = selAssignedOrgs;
    }

    public ViewEditUserForm() {

    }

    public String getContacts() {
        return contacts;
    }

    public Collection<CountryBean> getCountries() {
        return countries;
    }

    public String getEmail() {
        return email;
    }

    public String getEvent() {
        return event;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public Long getId() {
        return id;
    }

    public Collection getLanguages() {
        return languages;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getName() {
        return name;
    }

    public Collection<AmpOrgGroup> getOrgGroups() {
        return orgGroups;
    }

    public Collection<AmpOrganisation> getOrgs() {
        return orgs;
    }

    public Collection<AmpOrgType> getOrgTypes() {
        return orgTypes;
    }

    public String getSelectedCountryIso() {
        return selectedCountryIso;
    }

    public String getSelectedLanguageCode() {
        return selectedLanguageCode;
    }

    public Long getSelectedOrgGroupId() {
        return selectedOrgGroupId;
    }

    public String getSelectedOrgName() {
        return selectedOrgName;
    }

    public String getSelectedOrgTypeId() {
        return selectedOrgTypeId;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getBan() {
        return ban;
    }

    public String getConfirmNewPassword() {

        return confirmNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public Boolean getDisplaySuccessMessage() {
        return displaySuccessMessage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSelectedOrgTypeId(String selectedOrgTypeId) {
        this.selectedOrgTypeId = selectedOrgTypeId;
    }

    public void setSelectedOrgName(String selectedOrgName) {
        this.selectedOrgName = selectedOrgName;
    }

    public void setSelectedOrgGroupId(Long selectedOrgGroupId) {
        this.selectedOrgGroupId = selectedOrgGroupId;
    }

    public void setSelectedLanguageCode(String selectedLanguageCode) {
        this.selectedLanguageCode = selectedLanguageCode;
    }

    public void setSelectedCountryIso(String selectedCountryIso) {
        this.selectedCountryIso = selectedCountryIso;
    }

    public void setOrgTypes(Collection<AmpOrgType> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public void setOrgs(Collection<AmpOrganisation> orgs) {
        this.orgs = orgs;
    }

    public void setOrgGroups(Collection<AmpOrgGroup> orgGroups) {
        this.orgGroups = orgGroups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLanguages(Collection languages) {
        this.languages = languages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountries(Collection<CountryBean> countries) {
        this.countries = countries;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setBan(Boolean ban) {
        this.ban = ban;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {

        this.confirmNewPassword = confirmNewPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setDisplaySuccessMessage(Boolean displaySuccessMessage) {
        this.displaySuccessMessage = displaySuccessMessage;
    }

    public Long getAssignedOrgId() {
        return assignedOrgId;
    }

    public void setAssignedOrgId(Long assignedOrgId) {
        this.assignedOrgId = assignedOrgId;
    }

    public Long getSelectedOrgId() {
        return selectedOrgId;
    }

    public void setSelectedOrgId(Long selectedOrgId) {
        this.selectedOrgId = selectedOrgId;
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

    public boolean isAddWorkspace() {
        return addWorkspace;
    }

    public void setAddWorkspace(boolean addWorkspace) {
        this.addWorkspace = addWorkspace;
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

    public Collection getAssignedWorkspaces() {
        return assignedWorkspaces;
    }

    public void setAssignedWorkspaces(Collection assignedWorkspaces) {
        this.assignedWorkspaces = assignedWorkspaces;
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

    public Long getSelectedRegionId() {
        return selectedRegionId;
    }

    public void setSelectedRegionId(Long selectedRegionId) {
        this.selectedRegionId = selectedRegionId;
    }

    public Collection<AmpCategoryValueLocations> getRegions() {
        return regions;
    }

    public void setRegions(Collection<AmpCategoryValueLocations> regions) {
        this.regions = regions;
    }

    public boolean isBanReadOnly() {
        return banReadOnly;
    }

    public void setBanReadOnly(boolean banReadOnly) {
        this.banReadOnly = banReadOnly;
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

    public Collection<TruBudgetIntent> getTruBudgetIntents() {
        return truBudgetIntents;
    }

    public void setTruBudgetIntents(Collection<TruBudgetIntent> truBudgetIntents) {
        this.truBudgetIntents = truBudgetIntents;
    }

    public String[] getSelectedTruBudgetIntents() {
        return selectedTruBudgetIntents;
    }

    public void setSelectedTruBudgetIntents(String[] selectedTruBudgetIntents) {
        this.selectedTruBudgetIntents = selectedTruBudgetIntents;
    }
}
