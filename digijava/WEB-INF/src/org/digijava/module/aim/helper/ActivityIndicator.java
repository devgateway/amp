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
	private float revTargetVal;
	private String baseValDate;
	private String targetValDate;
	private String revTargetValDate;
	
	private String indicatorName;
	private String indicatorCode;
	private float currentVal;
	private String comments;
	private Long risk;
	
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
	 * @return Returns the revTargetVal.
	 */
	public float getRevTargetVal() {
		return revTargetVal;
	}
	/**
	 * @param revTargetVal The revTargetVal to set.
	 */
	public void setRevTargetVal(float revTargetVal) {
		this.revTargetVal = revTargetVal;
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
	 * @return Returns the revTargetValDate.
	 */
	public String getRevTargetValDate() {
		return revTargetValDate;
	}
	/**
	 * @param revTargetValDate The revTargetValDate to set.
	 */
	public void setRevTargetValDate(String revTargetValDate) {
		this.revTargetValDate = revTargetValDate;
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
}