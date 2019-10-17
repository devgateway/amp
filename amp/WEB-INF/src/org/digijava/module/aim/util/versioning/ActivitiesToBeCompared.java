package org.digijava.module.aim.util.versioning;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.ActivityHistory;

public class ActivitiesToBeCompared {
    private AmpActivityVersion activityOne;
    private AmpActivityVersion activityTwo;
    private ActivityHistory auditHistoryOne;
    private ActivityHistory auditHistoryTwo;

    public ActivitiesToBeCompared(AmpActivityVersion activityOne, AmpActivityVersion activityTwo,
                                  ActivityHistory auditHistoryOne, ActivityHistory auditHistoryTwo) {
        this.activityOne = activityOne;
        this.activityTwo = activityTwo;
        this.auditHistoryOne = auditHistoryOne;
        this.auditHistoryTwo = auditHistoryTwo;
    }

    public AmpActivityVersion getActivityOne() {
        return activityOne;
    }

    public void setActivityOne(AmpActivityVersion activityOne) {
        this.activityOne = activityOne;
    }

    public AmpActivityVersion getActivityTwo() {
        return activityTwo;
    }

    public void setActivityTwo(AmpActivityVersion activityTwo) {
        this.activityTwo = activityTwo;
    }

    public ActivityHistory getAuditHistoryOne() {
        return auditHistoryOne;
    }

    public void setAuditHistoryOne(ActivityHistory auditHistoryOne) {
        this.auditHistoryOne = auditHistoryOne;
    }

    public ActivityHistory getAuditHistoryTwo() {
        return auditHistoryTwo;
    }

    public void setAuditHistoryTwo(ActivityHistory auditHistoryTwo) {
        this.auditHistoryTwo = auditHistoryTwo;
    }
}