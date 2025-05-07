package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompareActivityVersionsForm extends ActionForm {

    private Long activityOneId;

    private Long activityTwoId;

    private List<CompareOutput> outputCollection;

    private AmpActivityVersion activityOne;

    private AmpActivityVersion activityTwo;

    private boolean showMergeColumn;

    private String method;

    private AmpActivityVersion oldActivity;

    private String[] mergedValues = new String[] {};

    private Long ampActivityId;
    
    private boolean advancemode;

    private Map<String, List<CompareOutput>> outputCollectionGrouped;

    public Long getActivityOneId() {
        return activityOneId;
    }

    public void setActivityOneId(Long activityOneId) {
        this.activityOneId = activityOneId;
    }

    public Long getActivityTwoId() {
        return activityTwoId;
    }

    public void setActivityTwoId(Long activityTwoId) {
        this.activityTwoId = activityTwoId;
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

    public List<CompareOutput> getOutputCollection() {
        return outputCollection;
    }

    public void setOutputCollection(List<CompareOutput> outputCollection) {
        this.outputCollection = outputCollection;
    }

    public boolean isShowMergeColumn() {
        return showMergeColumn;
    }

    public void setShowMergeColumn(boolean showMergeColumn) {
        this.showMergeColumn = showMergeColumn;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public AmpActivityVersion getOldActivity() {
        return oldActivity;
    }

    public void setOldActivity(AmpActivityVersion oldActivity) {
        this.oldActivity = oldActivity;
    }

    public void setMergedValues(String[] mergedValues) {
        this.mergedValues = mergedValues;
    }

    public String[] getMergedValues() {
        return mergedValues;
    }

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public boolean isAdvancemode() {
        return advancemode;
    }

    public void setAdvancemode(boolean advancemode) {
        this.advancemode = advancemode;
    }

    public Map<String, List<CompareOutput>> getOutputCollectionGrouped() {
        return outputCollectionGrouped;
    }

    public void setOutputCollectionGrouped(Map<String, List<CompareOutput>> outputCollectionGrouped) {
        this.outputCollectionGrouped = outputCollectionGrouped;
    }

    public Set<Map.Entry<String, List<CompareOutput>>> getOutputCollectionGroupedAsSet() {
        return this.outputCollectionGrouped.entrySet();
    }
}
