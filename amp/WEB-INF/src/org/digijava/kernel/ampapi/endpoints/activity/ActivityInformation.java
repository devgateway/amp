package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.ActivityHistory;
import org.digijava.module.aim.util.ValidationStatus;

import java.util.List;

public class ActivityInformation {

    @JsonProperty(ActivityEPConstants.AMP_ACTIVITY_ID)
    private Long ampActivityId;

    @JsonProperty(ActivityEPConstants.AMP_ACTIVITY_LAST_VERSION_ID)
    private Long ampActiviylastVersionId;

    private boolean edit;

    private boolean validate;

    @JsonProperty(ActivityEPConstants.VALIDATION_STATUS)
    private ValidationStatus validationStatus;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty(ActivityEPConstants.DAYS_FOR_AUTOMATIC_VALIDATION)
    private Integer daysForAutomaticValidation;

    @JsonProperty(ActivityEPConstants.ACTIVITY_TEAM)
    private AmpTeam activityTeam;

    @JsonProperty(ActivityEPConstants.VERSION_HISTORY)
    private List<ActivityHistory> versionHistory;

    @JsonProperty(ActivityEPConstants.UPDATE_CURRENT_VERSION)
    private boolean updateCurrentVersion;

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

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public Integer getDaysForAutomaticValidation() {
        return daysForAutomaticValidation;
    }

    public void setDaysForAutomaticValidation(Integer daysForAutomaticValidation) {
        this.daysForAutomaticValidation = daysForAutomaticValidation;
    }

    public AmpTeam getActivityTeam() {
        return activityTeam;
    }

    public void setActivityTeam(AmpTeam activityTeam) {
        this.activityTeam = activityTeam;
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
}
