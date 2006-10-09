package org.digijava.module.aim.dbentity;

import java.util.Date;

public class AmpMEIndicatorValue {
	private Long ampMeIndValId;
	private AmpActivity activityId;
	private AmpMEIndicators meIndicatorId;
	private float baseVal;  // BASE
	private Date baseValDate;
	private String baseValComments;
	private float actualVal;  // ACTUAL
	private Date actualValDate;
	private String actualValComments;
	private float targetVal; // TARGET
	private Date targetValDate;
	private String targetValComments;
	private float revisedTargetVal; // Revised TARGET
	private String revisedTargetValComments;
	private Date revisedTargetValDate;
	private String comments;
	
	private AmpIndicatorRiskRatings risk;
	/**
	 * @return Returns the activityId.
	 */
	public AmpActivity getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(AmpActivity activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the ampMeIndValId.
	 */
	public Long getAmpMeIndValId() {
		return ampMeIndValId;
	}
	/**
	 * @param ampMeIndValId The ampMeIndValId to set.
	 */
	public void setAmpMeIndValId(Long ampMeIndValId) {
		this.ampMeIndValId = ampMeIndValId;
	}
	/**
	 * @return Returns the baseValDate.
	 */
	public Date getBaseValDate() {
		return baseValDate;
	}
	/**
	 * @param baseValDate The baseValDate to set.
	 */
	public void setBaseValDate(Date baseValDate) {
		this.baseValDate = baseValDate;
	}
	/**
	 * @return Returns the meIndicatorId.
	 */
	public AmpMEIndicators getMeIndicatorId() {
		return meIndicatorId;
	}
	/**
	 * @param meIndicatorId The meIndicatorId to set.
	 */
	public void setMeIndicatorId(AmpMEIndicators meIndicatorId) {
		this.meIndicatorId = meIndicatorId;
	}
	/**
	 * @return Returns the risk.
	 */
	public AmpIndicatorRiskRatings getRisk() {
		return risk;
	}
	/**
	 * @param risk The risk to set.
	 */
	public void setRisk(AmpIndicatorRiskRatings risk) {
		this.risk = risk;
	}
	/**
	 * @return Returns the targetValDate.
	 */
	public Date getTargetValDate() {
		return targetValDate;
	}
	/**
	 * @param targetValDate The targetValDate to set.
	 */
	public void setTargetValDate(Date targetValDate) {
		this.targetValDate = targetValDate;
	}
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public Date getActualValDate() {
		return actualValDate;
	}
	/**
	 * @param actualValDate The actualValDate to set.
	 */
	public void setActualValDate(Date actualValDate) {
		this.actualValDate = actualValDate;
	}
	/**
	 * @return Returns the revisedTargetVal.
	 */
	public float getRevisedTargetVal() {
		return revisedTargetVal;
	}
	/**
	 * @param revisedTargetVal The revisedTargetVal to set.
	 */
	public void setRevisedTargetVal(float revisedTargetVal) {
		this.revisedTargetVal = revisedTargetVal;
	}
	/**
	 * @return Returns the revisedTargetValDate.
	 */
	public Date getRevisedTargetValDate() {
		return revisedTargetValDate;
	}
	/**
	 * @param revisedTargetValDate The revisedTargetValDate to set.
	 */
	public void setRevisedTargetValDate(Date revisedTargetValDate) {
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
}