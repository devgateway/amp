package org.digijava.kernel.ampapi.endpoints.activity.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.util.ValidationStatus;

import java.util.List;

public class ActivityInformation {

    @JsonProperty(ActivityEPConstants.AMP_ACTIVITY_ID)
    private Long ampActivityId;

    @JsonProperty(ActivityEPConstants.AMP_ACTIVITY_LAST_VERSION_ID)
    private Long ampActiviylastVersionId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(ActivityEPConstants.DAYS_FOR_AUTOMATIC_VALIDATION)
    private Integer daysForAutomaticValidation;

    @JsonProperty(ActivityEPConstants.ACTIVITY_WORKSPACE)
    private AmpTeam activityWorkspace;

    @JsonProperty(ActivityEPConstants.VALIDATION_STATUS)
    private ValidationStatus validationStatus;

    @JsonProperty(ActivityEPConstants.TEAM_MEMBER)
    private TeamMemberInformation teamMember;

    @JsonProperty(ActivityEPConstants.VERSION_HISTORY)
    private List<ActivityHistory> versionHistory;

    @JsonProperty(ActivityEPConstants.UPDATE_CURRENT_VERSION)
    private boolean updateCurrentVersion;

    @JsonProperty(ActivityEPConstants.ACTIVITY_TEAM_LEAD_DATA)
    private String activityWorkspaceLeadData;

    @JsonProperty(ActivityEPConstants.SHOW_CHANGE_SUMMARY)
    private boolean showChangeSummary;

    public ActivityInformation(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
        this.validationStatus = ValidationStatus.UNKNOWN;
    }

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public Long getAmpActiviylastVersionId() {
        return ampActiviylastVersionId;
    }

    public void setAmpActiviylastVersionId(Long ampActiviylastVersionId) {
        this.ampActiviylastVersionId = ampActiviylastVersionId;
    }

    public Integer getDaysForAutomaticValidation() {
        return daysForAutomaticValidation;
    }

    public void setDaysForAutomaticValidation(Integer daysForAutomaticValidation) {
        this.daysForAutomaticValidation = daysForAutomaticValidation;
    }

    public AmpTeam getActivityWorkspace() {
        return activityWorkspace;
    }

    public void setActivityWorkspace(AmpTeam activityTeam) {
        this.activityWorkspace = activityTeam;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public TeamMemberInformation getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMemberInformation teamMember) {
        this.teamMember = teamMember;
    }


    public List<ActivityHistory> getVersionHistory() {
        return versionHistory;
    }

    public void setVersionHistory(List<ActivityHistory> versionHistory) {
        this.versionHistory = versionHistory;
    }

    public boolean isUpdateCurrentVersion() {
        return updateCurrentVersion;
    }

    public void setUpdateCurrentVersion(boolean updateCurrentVersion) {
        this.updateCurrentVersion = updateCurrentVersion;
    }

    public String getActivityWorkspaceLeadData() {
        return activityWorkspaceLeadData;
    }

    public void setActivityWorkspaceLeadData(String activityWorkspaceLeadData) {
        this.activityWorkspaceLeadData = activityWorkspaceLeadData;
    }

    public boolean isShowChangeSummary() {
        return showChangeSummary;
    }

    public void setShowChangeSummary(boolean showChangeSummary) {
        this.showChangeSummary = showChangeSummary;
    }
}
