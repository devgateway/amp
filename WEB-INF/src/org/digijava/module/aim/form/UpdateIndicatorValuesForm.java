/*
 * UpdateIndicatorValuesForm.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class UpdateIndicatorValuesForm extends ActionForm {
	
	private Long indicatorId;
	private Long indicatorValId;
	private Long activityId;
	private float baseVal;
	private float targetVal;
	private float revisedTargetVal;
	private String baseValDate;
	private String targetValDate;
	private String revisedTargetValDate;
	private String baseValComments;
	private String targetValComments;
	private String revisedTargetValComments;	
	
	private Collection indicators;
	private Long expIndicatorId;
    private Long indicatorConId;

    public Long getIndicatorConId() {
        return indicatorConId;
    }

    public void setIndicatorConId(Long indicatorConId) {
        this.indicatorConId = indicatorConId;
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
	 * @return Returns the expIndicatorId.
	 */
	public Long getExpIndicatorId() {
		return expIndicatorId;
	}
	/**
	 * @param expIndicatorId The expIndicatorId to set.
	 */
	public void setExpIndicatorId(Long expIndicatorId) {
		this.expIndicatorId = expIndicatorId;
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
	 * @return Returns the indicators.
	 */
	public Collection getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
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

