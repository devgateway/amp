/*
 * RegionalFundingForm.java
 * Created : 06-Oct-2005
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class RegionalFundingForm extends MainProjectDetailsForm {
	private Collection regionalFundings;
	private Long regionId;
	private double totCommitments;
	private double totDisbursements;
	private double totUnDisbursed;
	private double totExpenditures;
	private double totUnExpended;

	public RegionalFundingForm() {
		totCommitments = 0;
		totDisbursements = 0;
		totUnDisbursed = 0;
		totExpenditures = 0;
		totUnExpended = 0;		
	}
	
	/**
	 * @return Returns the regionalFundings.
	 */
	public Collection getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings The regionalFundings to set.
	 */
	public void setRegionalFundings(Collection regionalFundings) {
		this.regionalFundings = regionalFundings;
	}

	/**
	 * @return Returns the regionId.
	 */
	public Long getRegionId() {
		return regionId;
	}

	/**
	 * @param regionId The regionId to set.
	 */
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return Returns the totCommitments.
	 */
	public double getTotCommitments() {
		return totCommitments;
	}

	/**
	 * @param totCommitments The totCommitments to set.
	 */
	public void setTotCommitments(double totCommitments) {
		this.totCommitments = totCommitments;
	}

	/**
	 * @return Returns the totDisbursements.
	 */
	public double getTotDisbursements() {
		return totDisbursements;
	}

	/**
	 * @param totDisbursements The totDisbursements to set.
	 */
	public void setTotDisbursements(double totDisbursements) {
		this.totDisbursements = totDisbursements;
	}

	/**
	 * @return Returns the totExpenditures.
	 */
	public double getTotExpenditures() {
		return totExpenditures;
	}

	/**
	 * @param totExpenditures The totExpenditures to set.
	 */
	public void setTotExpenditures(double totExpenditures) {
		this.totExpenditures = totExpenditures;
	}

	/**
	 * @return Returns the totUnDisbursed.
	 */
	public double getTotUnDisbursed() {
		return totUnDisbursed;
	}

	/**
	 * @param totUnDisbursed The totUnDisbursed to set.
	 */
	public void setTotUnDisbursed(double totUnDisbursed) {
		this.totUnDisbursed = totUnDisbursed;
	}

	/**
	 * @return Returns the totUnExpended.
	 */
	public double getTotUnExpended() {
		return totUnExpended;
	}

	/**
	 * @param totUnExpended The totUnExpended to set.
	 */
	public void setTotUnExpended(double totUnExpended) {
		this.totUnExpended = totUnExpended;
	}
}

