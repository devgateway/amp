package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.helper.ActivityHistory;

public class ViewActivityHistoryForm extends ActionForm implements Serializable {

    private Long activityId;
    private Boolean enableadvanceoptions;
    private List<ActivityHistory> activities;
    private String actionMethod;
    private Boolean enableSummaryChange;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<ActivityHistory> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityHistory> activities) {
        this.activities = activities;
    }

    public Boolean getEnableadvanceoptions() {
        return enableadvanceoptions;
    }

    public void setEnableadvanceoptions(Boolean enableadvanceoptions) {
        this.enableadvanceoptions = enableadvanceoptions;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public Boolean getEnableSummaryChange() {
        return enableSummaryChange;
    }

    public void setEnableSummaryChange(Boolean enableSummaryChange) {
        this.enableSummaryChange = enableSummaryChange;
    }
}
