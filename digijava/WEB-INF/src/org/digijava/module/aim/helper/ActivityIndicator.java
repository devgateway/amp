/*
 * ActivityIndicator.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.helper;

public class ActivityIndicator {
	
	private Long indicatorId;
	private Long indicatorValId;
	private float baseVal;
	private float targetVal;
	private float actualVal;
	private String baseValDate;
	private String targetValDate;
	private String actualValDate;
	
	private String indicatorName;
	private String indicatorCode;
	private float currentVal;
	private String currentValDate;
	private String comments;
	private Long risk;
	
	private Long activityId;
	private boolean defaultInd;
	
	/**
	 * @return Returns the baseVal.
	 */
	public float getBaseVal() {
		return baseVal;
	}
	/**
	 * @param baseVal The baseVal to set.
	 */
	public void setBaseVal(float baseVal) {
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
	public float getTargetVal() {
		return targetVal;
	}
	/**
	 * @param targetVal The targetVal to set.
	 */
	public void setTargetVal(float targetVal) {
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
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the currentVal.
	 */
	public float getCurrentVal() {
		return currentVal;
	}
	/**
	 * @param currentVal The currentVal to set.
	 */
	public void setCurrentVal(float currentVal) {
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
	public float getActualVal() {
		return actualVal;
	}
	/**
	 * @param actualVal The actualVal to set.
	 */
	public void setActualVal(float actualVal) {
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
}