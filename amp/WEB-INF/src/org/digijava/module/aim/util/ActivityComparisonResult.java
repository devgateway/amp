package org.digijava.module.aim.util;

import org.digijava.module.aim.annotations.activityversioning.CompareOutput;

import java.util.List;
import java.util.Map;

public class ActivityComparisonResult {

    private Long activityId;
    private String name;
    private Map<String, List<CompareOutput>> compareOutput;

    public ActivityComparisonResult(Long activityid, Map<String, List<CompareOutput>> compareOutput, String name) {
        this.activityId = activityid;
        this.compareOutput = compareOutput;
        this.name = name;
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
}
