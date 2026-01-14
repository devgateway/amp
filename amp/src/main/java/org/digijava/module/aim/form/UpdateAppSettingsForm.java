package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.helper.CrConstants;

import java.util.Collection;
import java.util.List;

public class UpdateAppSettingsForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long appSettingsId = null;

    private Integer defRecsPerPage;

    private String numberOfPagesToDisplay;

    private String language = null;

    private String validation = null;
    
    private Long currencyId = null;

    private Long fisCalendarId = null;

    private int reportStartYear;

    private int reportEndYear;

    /**
     * null - creating, "default" - saving
     */
    private String type = null;

    private String workspaceType = null;

    private Collection currencies = null;

    private Collection fisCalendars = null;

    private String teamName = null;

    private String memberName = null;

    private String restore = null;

    private String save = null;

    private boolean updateFlag = false;

    private boolean updated = false;

    private boolean errors = false;

    private Long defaultReportForTeamId = new Long(0);

    private List languages;

    private Collection reports;

    private int defReportsPerPage;

    private Collection<KeyValue> possibleValsAddTR;
    
    // share resources among workspaces allowed by
    private Collection<KeyValue> shareResAmongWorkspacesPossibleVals; 

    private Collection<KeyValue> publishResourcesPossibleVals;

    private Integer allowAddTeamRes = CrConstants.TEAM_RESOURCES_ADD_ONLY_WORKSP_MANAGER;

    private Integer allowShareAccrossWRK = CrConstants.SHARE_AMONG_WRKSPACES_ALLOWED_WM;

    private Integer allowPublishingResources = CrConstants.PUBLISHING_RESOURCES_ALLOWED_ONLY_TL;

    private Long[] selTeamMembers;
    private List<TeamMember> teamMembers;
    private Boolean resetTeamMembers;

    
    private Boolean showAllCountries;

    public Boolean getShowAllCountries() {
        return showAllCountries;
    }

    public void setShowAllCountries(Boolean showAllCountries) {
        this.showAllCountries = showAllCountries;
    }

    public Collection getReports() {
        return reports;
    }

    public void setReports(Collection reports) {
        this.reports = reports;
    }

    public boolean getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(boolean flag) {
        updateFlag = flag;
    }

    public boolean getUpdated() {
        return updated;
    }

    public void setUpdated(boolean flag) {
        updated = flag;
    }

    public String getRestore() {
        return this.restore;
    }

    public void setRestore(String restore) {
        this.restore = restore;
    }

    public String getSave() {
        return this.save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public Collection getCurrencies() {
        return this.currencies;
    }

    public void setCurrencies(Collection currencies) {
        this.currencies = currencies;
    }

    public Collection getFisCalendars() {
        return fisCalendars;
    }

    public void setFisCalendars(Collection fisCalendars) {
        this.fisCalendars = fisCalendars;
    }

    public Long getAppSettingsId() {
        return this.appSettingsId;
    }

    public void setAppSettingsId(Long appSettingsId) {
        this.appSettingsId = appSettingsId;
    }

    public Integer getDefRecsPerPage() {
        return this.defRecsPerPage;
    }

    public void setDefRecsPerPage(Integer defRecsPerPage) {
        this.defRecsPerPage = defRecsPerPage;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getFisCalendarId() {
        return this.fisCalendarId;
    }

    public void setFisCalendarId(Long fisCalendarId) {
        this.fisCalendarId = fisCalendarId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeamName() {
        return (this.teamName);
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getMemberName() {
        return (this.memberName);
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     * @return Returns the languages.
     */
    public List getLanguages() {
        return languages;
    }

    /**
     * @param languages
     *            The languages to set.
     */
    public void setLanguages(List languages) {
        this.languages = languages;
    }

    public Long getDefaultReportForTeamId() {
        return defaultReportForTeamId;
    }

    public void setDefaultReportForTeamId(Long defaultReportForTeamId) {
        this.defaultReportForTeamId = defaultReportForTeamId;
    }

    public void setDefReportsPerPage(int defReportsPerPage) {
        this.defReportsPerPage = defReportsPerPage;
    }

    public int getDefReportsPerPage() {
        return defReportsPerPage;
    }

    public int getReportStartYear() {
        return reportStartYear;
    }

    public void setReportStartYear(int reportStartYear) {
        this.reportStartYear = reportStartYear;
    }

    public int getReportEndYear() {
        return reportEndYear;
    }

    public void setReportEndYear(int reportEndYear) {
        this.reportEndYear = reportEndYear;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
    
    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    /**
     * @return the allowAddTeamRes
     */
    public Integer getAllowAddTeamRes() {
        return allowAddTeamRes;
    }

    /**
     * @param allowAddTeamRes
     *            the allowAddTeamRes to set
     */
    public void setAllowAddTeamRes(Integer allowAddTeamRes) {
        this.allowAddTeamRes = allowAddTeamRes;
    }

    /**
     * @return the possibleValsAddTR
     */
    public Collection<KeyValue> getPossibleValsAddTR() {
        return possibleValsAddTR;
    }

    /**
     * @param possibleValsAddTR
     *            the possibleValsAddTR to set
     */
    public void setPossibleValsAddTR(Collection<KeyValue> possibleValsAddTR) {
        this.possibleValsAddTR = possibleValsAddTR;
    }

    public Collection<KeyValue> getShareResAmongWorkspacesPossibleVals() {
        return shareResAmongWorkspacesPossibleVals;
    }

    public void setShareResAmongWorkspacesPossibleVals(
            Collection<KeyValue> shareResAmongWorkspacesPossibleVals) {
        this.shareResAmongWorkspacesPossibleVals = shareResAmongWorkspacesPossibleVals;
    }

    public Integer getAllowShareAccrossWRK() {
        return allowShareAccrossWRK;
    }

    public void setAllowShareAccrossWRK(Integer allowShareAccrossWRK) {
        this.allowShareAccrossWRK = allowShareAccrossWRK;
    }

    public Collection<KeyValue> getPublishResourcesPossibleVals() {
        return publishResourcesPossibleVals;
    }

    public void setPublishResourcesPossibleVals(
            Collection<KeyValue> publishResourcesPossibleVals) {
        this.publishResourcesPossibleVals = publishResourcesPossibleVals;
    }

    public Integer getAllowPublishingResources() {
        return allowPublishingResources;
    }

    public void setAllowPublishingResources(Integer allowPublishingResources) {
        this.allowPublishingResources = allowPublishingResources;
    }

    public Long[] getSelTeamMembers() {
        return selTeamMembers;
    }

    public void setSelTeamMembers(Long[] selTeamMembers) {
        this.selTeamMembers = selTeamMembers;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Boolean getResetTeamMembers() {
        return resetTeamMembers;
    }

    public void setResetTeamMembers(Boolean resetTeamMembers) {
        this.resetTeamMembers = resetTeamMembers;
    }

    public String getWorkspaceType() {
        return workspaceType;
    }

    public void setWorkspaceType(String workspaceType) {
        this.workspaceType = workspaceType;
    }

    public String getNumberOfPagesToDisplay() {
        return "null".equals(numberOfPagesToDisplay) ? ""
                : numberOfPagesToDisplay;
    }

    public void setNumberOfPagesToDisplay(String numberOfPagesToDisplay) {
        this.numberOfPagesToDisplay = numberOfPagesToDisplay;
    }

}
