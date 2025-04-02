/*
 * ActivityIndicator.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.helper;

import java.util.Collection;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class ActivityIndicator {

    private Long connectionId;
    private Long indicatorId;
    private Long indicatorValId;
    private Float baseVal;
    private Float targetVal;
    private Float revisedTargetVal;
    private Float actualVal;
    private String baseValDate;
    private String targetValDate;
    private String revisedTargetValDate;
    private String actualValDate;
    private String baseValComments;
    private String targetValComments;
    private String revisedTargetValComments;
    private String actualValComments;   
    
    //private Long logframeValueId;
    private AmpCategoryValue indicatorsCategory;
    
    private String ampCategoryValue;
    
    private String indicatorName;
    private String indicatorCode;
    private Float currentVal;
    private String currentValDate;
    private String currentValComments;
    //private String comments;
    private Long risk;
    private String riskName;

    private Collection priorValues;
    
    private Long activityId;
    private boolean defaultInd;
    
    private String progress;

    
    /**
     * @return Returns the baseVal.
     */
    public Float getBaseVal() {
        return baseVal;
    }
    /**
     * @param baseVal The baseVal to set.
     */
    public void setBaseVal(Float baseVal) {
        this.baseVal = baseVal;
    }
    /**
     * @return Returns the baseValDate.
     */
    public String getBaseValDate() {
        return baseValDate;
    }
    /**
     * @param baseValDate The baseValDate to set.
     */
    public void setBaseValDate(String baseValDate) {
        this.baseValDate = baseValDate;
    }
    /**
     * @return Returns the indicatorId.
     */
    public Long getIndicatorId() {
        return indicatorId;
    }
    /**
     * @param indicatorId The indicatorId to set.
     */
    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
    /**
     * @return Returns the indicatorValId.
     */
    public Long getIndicatorValId() {
        return indicatorValId;
    }
    /**
     * @param indicatorValId The indicatorValId to set.
     */
    public void setIndicatorValId(Long indicatorValId) {
        this.indicatorValId = indicatorValId;
    }
    /**
     * @return Returns the targetVal.
     */
    public Float getTargetVal() {
        return targetVal;
    }
    /**
     * @param targetVal The targetVal to set.
     */
    public void setTargetVal(Float targetVal) {
        this.targetVal = targetVal;
    }
    /**
     * @return Returns the targetValDate.
     */
    public String getTargetValDate() {
        return targetValDate;
    }
    /**
     * @param targetValDate The targetValDate to set.
     */
    public void setTargetValDate(String targetValDate) {
        this.targetValDate = targetValDate;
    }
    /**
     * @return Returns the indicatorCode.
     */
    public String getIndicatorCode() {
        return indicatorCode;
    }
    /**
     * @param indicatorCode The indicatorCode to set.
     */
    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }
    /**
     * @return Returns the indicatorName.
     */
    public String getIndicatorName() {
        return indicatorName;
    }
    /**
     * @param indicatorName The indicatorName to set.
     */
    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }
    /**
     * @return Returns the currentVal.
     */
    public Float getCurrentVal() {
        return currentVal;
    }
    /**
     * @param currentVal The currentVal to set.
     */
    public void setCurrentVal(Float currentVal) {
        this.currentVal = currentVal;
    }
    /**
     * @return Returns the risk.
     */
    public Long getRisk() {
        return risk;
    }
    /**
     * @param risk The risk to set.
     */
    public void setRisk(Long risk) {
        this.risk = risk;
    }
    /**
     * @return Returns the currentValDate.
     */
    public String getCurrentValDate() {
        return currentValDate;
    }
    /**
     * @param currentValDate The currentValDate to set.
     */
    public void setCurrentValDate(String currentValDate) {
        this.currentValDate = currentValDate;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof ActivityIndicator) {
            ActivityIndicator actInd = (ActivityIndicator) obj;
            return indicatorId.longValue() == actInd.getIndicatorId().longValue();
        } else throw new ClassCastException();
    }
    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the actualVal.
     */
    public Float getActualVal() {
        return actualVal;
    }
    /**
     * @param actualVal The actualVal to set.
     */
    public void setActualVal(Float actualVal) {
        this.actualVal = actualVal;
    }
    /**
     * @return Returns the actualValDate.
     */
    public String getActualValDate() {
        return actualValDate;
    }
    /**
     * @param actualValDate The actualValDate to set.
     */
    public void setActualValDate(String actualValDate) {
        this.actualValDate = actualValDate;
    }
    /**
     * @return Returns the defaultInd.
     */
    public boolean isDefaultInd() {
        return defaultInd;
    }
    /**
     * @param defaultInd The defaultInd to set.
     */
    public void setDefaultInd(boolean defaultInd) {
        this.defaultInd = defaultInd;
    }
    /**
     * @return Returns the priorValues.
     */
    public Collection getPriorValues() {
        return priorValues;
    }
    /**
     * @param priorValues The priorValues to set.
     */
    public void setPriorValues(Collection priorValues) {
        this.priorValues = priorValues;
    }
    /**
     * @return Returns the riskName.
     */
    public String getRiskName() {
        return riskName;
    }
    /**
     * @param riskName The riskName to set.
     */
    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }
    /**
     * @return Returns the progress.
     */
    public String getProgress() {
        return progress;
    }
    /**
     * @param progress The progress to set.
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }
    /**
     * @return Returns the revisedTargetVal.
     */
    public Float getRevisedTargetVal() {
        return revisedTargetVal;
    }
    /**
     * @param revisedTargetVal The revisedTargetVal to set.
     */
    public void setRevisedTargetVal(Float revisedTargetVal) {
        this.revisedTargetVal = revisedTargetVal;
    }
    /**
     * @return Returns the revisedTargetValDate.
     */
    public String getRevisedTargetValDate() {
        return revisedTargetValDate;
    }
    /**
     * @param revisedTargetValDate The revisedTargetValDate to set.
     */
    public void setRevisedTargetValDate(String revisedTargetValDate) {
        this.revisedTargetValDate = revisedTargetValDate;
    }
    /**
     * @return Returns the actualValComments.
     */
    public String getActualValComments() {
        return actualValComments;
    }
    /**
     * @param actualValComments The actualValComments to set.
     */
    public void setActualValComments(String actualValComments) {
        this.actualValComments = actualValComments;
    }
    /**
     * @return Returns the baseValComments.
     */
    public String getBaseValComments() {
        return baseValComments;
    }
    /**
     * @param baseValComments The baseValComments to set.
     */
    public void setBaseValComments(String baseValComments) {
        this.baseValComments = baseValComments;
    }
    /**
     * @return Returns the revisedTargetValComments.
     */
    public String getRevisedTargetValComments() {
        return revisedTargetValComments;
    }
    /**
     * @param revisedTargetValComments The revisedTargetValComments to set.
     */
    public void setRevisedTargetValComments(String revisedTargetValComments) {
        this.revisedTargetValComments = revisedTargetValComments;
    }
    /**
     * @return Returns the targetValComments.
     */
    public String getTargetValComments() {
        return targetValComments;
    }
    /**
     * @param targetValComments The targetValComments to set.
     */
    public void setTargetValComments(String targetValComments) {
        this.targetValComments = targetValComments;
    }
    /**
     * @return Returns the currentValComments.
     */
    public String getCurrentValComments() {
        return currentValComments;
    }
    /**
     * @param currentValComments The currentValComments to set.
     */
    public void setCurrentValComments(String currentValComments) {
        this.currentValComments = currentValComments;
    }
    /*public Long getLogframeValueId() {
        return logframeValueId;
    }
    public void setLogframeValueId(Long logframeValueId) {
        this.logframeValueId = logframeValueId;
    }
*/
    public AmpCategoryValue getIndicatorsCategory() {
        return indicatorsCategory;
    }
    public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }
    public String getAmpCategoryValue() {
        return ampCategoryValue;
    }
    public void setAmpCategoryValue(String ampCategoryValue) {
        this.ampCategoryValue = ampCategoryValue;
    }
    public Long getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
    
}
