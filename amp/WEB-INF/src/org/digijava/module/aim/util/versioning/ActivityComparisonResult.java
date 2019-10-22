package org.digijava.module.aim.util.versioning;

import org.digijava.module.aim.annotations.activityversioning.CompareOutput;

import java.util.List;
import java.util.Map;

public class ActivityComparisonResult {

    private Long activityId;
    private String name;
    private Long ampAuditLoggerId;
    private Map<String, List<CompareOutput>> compareOutput;

    public ActivityComparisonResult(Long activityId, String name, Map<String, List<CompareOutput>> compareOutput) {
        this.activityId = activityId;
        this.name = name;
        this.compareOutput = compareOutput;
    }

    public ActivityComparisonResult(Long activityId, Map<String, List<CompareOutput>> compareOutput, String name,
                                    Long  ampAuditLoggerId) {
        this(activityId, name, compareOutput);
        this.ampAuditLoggerId = ampAuditLoggerId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<CompareOutput>> getCompareOutput() {
        return compareOutput;
    }

    public void setCompareOutput(Map<String, List<CompareOutput>> compareOutput) {
        this.compareOutput = compareOutput;
    }

    public Long getAmpAuditLoggerId() {
        return ampAuditLoggerId;
    }

    public void setAmpAuditLoggerId(Long ampAuditLoggerId) {
        this.ampAuditLoggerId = ampAuditLoggerId;
    }
}