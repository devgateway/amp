/*
 * Components.java
 */

package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Collection;

public class Components implements Comparable , Serializable{
	
	private Long componentId;
	private String title;
	private String description;
	private String amount;
	private Collection commitments;
	private Collection disbursements;
	private Collection expenditures;
	private String reportingDate;	
	private String currencyCode;
	private Collection phyProgress;
	
	public Components() {}
	
	public Components(Long id) {
		componentId = id;
	}
	
	/**
	 * @return Returns the amount.
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the componentId.
	 */
	public Long getComponentId() {
		return componentId;
	}
	/**
	 * @param componentId The componentId to set.
	 */
	public void setComponentId(Long componentId) {
		this.componentId = componentId;
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
	 * @return Returns the phyProgress.
	 */
	public Collection getPhyProgress() {
		return phyProgress;
	}
	/**
	 * @param phyProgress The phyProgress to set.
	 */
	public void setPhyProgress(Collection phyProgress) {
		this.phyProgress = phyProgress;
	}
	/**
	 * @return Returns the reportingDate.
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate The reportingDate to set.
	 */
	public void setReportingDate(String reportingDate) {
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
	
	public int compareTo(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof Components)) throw new ClassCastException();
		
		Components comp = (Components) obj;
		return comp.getComponentId().compareTo(this.componentId);
	}
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

		/**
	 * @return Returns the commitments.
	 */
	public Collection getCommitments() {
		return commitments;
	}

	/**
	 * @param commitments The commitments to set.
	 */
	public void setCommitments(Collection commitments) {
		this.commitments = commitments;
	}

		/**
	 * @return Returns the disbursements.
	 */
	public Collection getDisbursements() {
		return disbursements;
	}

	/**
	 * @param disbursements The disbursements to set.
	 */
	public void setDisbursements(Collection disbursements) {
		this.disbursements = disbursements;
	}

		/**
	 * @return Returns the expenditures.
	 */
	public Collection getExpenditures() {
		return expenditures;
	}

	/**
	 * @param expenditures The expenditures to set.
	 */
	public void setExpenditures(Collection expenditures) {
		this.expenditures = expenditures;
	}

	public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof Components)) throw new ClassCastException();
		
		Components temp = (Components) obj;
		if (this.componentId == null) return false;
		if (temp.componentId == null) return false;
		return this.componentId.equals(temp.componentId);
	}
}
