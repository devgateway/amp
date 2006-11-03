/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Persister class for Components
 * @author Priyajith
 */
public class AmpComponent implements Serializable {
	
	private Long ampComponentId;
	private String title;
	private String description;
	private Double amount;
	private Date reportingDate;	
	private AmpCurrency currency;
	private AmpActivity activity;
	private Set physicalProgress; 
	private Set componentFundings;
	private String code;
	private String type;
	
	private Set activities;

	public String toString() {
		return title!=null?title:"";
	}
	
	/**
	 * @return Returns the activity.
	 */
	public AmpActivity getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	/**
	 * @return Returns the ampComponentId.
	 */
	public Long getAmpComponentId() {
		return ampComponentId;
	}
	/**
	 * @param ampComponentId The ampComponentId to set.
	 */
	public void setAmpComponentId(Long ampComponentId) {
		this.ampComponentId = ampComponentId;
	}
	/**
	 * @return Returns the currency.
	 */
	public AmpCurrency getCurrency() {
		return currency;
	}
	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public Date getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the amount.
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the physicalProgress.
	 */
	public Set getPhysicalProgress() {
		return physicalProgress;
	}
	/**
	 * @param physicalProgress The physicalProgress to set.
	 */
	public void setPhysicalProgress(Set physicalProgress) {
		this.physicalProgress = physicalProgress;
	}

	public Set getComponentFundings() {
		return componentFundings;
	}
	public void setComponentFundings(Set componentFundings) {
		this.componentFundings = componentFundings;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the activities.
	 */
	public Set getActivities() {
		return activities;
	}

	/**
	 * @param activities The activities to set.
	 */
	public void setActivities(Set activities) {
		this.activities = activities;
	}	
}
