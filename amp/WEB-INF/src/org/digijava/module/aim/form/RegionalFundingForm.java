/*
 * RegionalFundingForm.java
 * Created : 06-Oct-2005
 */
package org.digijava.module.aim.form;

import java.util.Collection;

public class RegionalFundingForm extends MainProjectDetailsForm {
	private Collection regionalFundings;
	private Long regionId;
	private double totCommitments;
	private double totDisbursements;
	private double totUnDisbursed;
	private double totExpenditures;
	private double totUnExpended;
	
	private boolean currFilter;
	private boolean calFilter;
	private boolean goButton;
	
	private String currFilterValue;
	private long calFilterValue;
	private Collection fiscalCalendars;
	

	public RegionalFundingForm() {
		totCommitments = 0;
		totDisbursements = 0;
		totUnDisbursed = 0;
		totExpenditures = 0;
		totUnExpended = 0;
		currFilter = false;
		calFilter = false;
		goButton = false;
		currFilterValue = "";
		calFilterValue = -1;
		fiscalCalendars = null;
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

	/**
	 * @return Returns the calFilter.
	 */
	public boolean isCalFilter() {
		return calFilter;
	}

	/**
	 * @param calFilter The calFilter to set.
	 */
	public void setCalFilter(boolean calFilter) {
		this.calFilter = calFilter;
	}

	/**
	 * @return Returns the calFilterValue.
	 */
	public long getCalFilterValue() {
		return calFilterValue;
	}

	/**
	 * @param calFilterValue The calFilterValue to set.
	 */
	public void setCalFilterValue(long calFilterValue) {
		this.calFilterValue = calFilterValue;
	}

	/**
	 * @return Returns the currFilter.
	 */
	public boolean isCurrFilter() {
		return currFilter;
	}

	/**
	 * @param currFilter The currFilter to set.
	 */
	public void setCurrFilter(boolean currFilter) {
		this.currFilter = currFilter;
	}

	/**
	 * @return Returns the currFilterValue.
	 */
	public String getCurrFilterValue() {
		return currFilterValue;
	}

	/**
	 * @param currFilterValue The currFilterValue to set.
	 */
	public void setCurrFilterValue(String currFilterValue) {
		this.currFilterValue = currFilterValue;
	}

	/**
	 * @return Returns the fiscalCalendars.
	 */
	public Collection getFiscalCalendars() {
		return fiscalCalendars;
	}

	/**
	 * @param fiscalCalendars The fiscalCalendars to set.
	 */
	public void setFiscalCalendars(Collection fiscalCalendars) {
		this.fiscalCalendars = fiscalCalendars;
	}

	/**
	 * @return Returns the goButton.
	 */
	public boolean isGoButton() {
		return goButton;
	}

	/**
	 * @param goButton The goButton to set.
	 */
	public void setGoButton(boolean goButton) {
		this.goButton = goButton;
	}

	
}

