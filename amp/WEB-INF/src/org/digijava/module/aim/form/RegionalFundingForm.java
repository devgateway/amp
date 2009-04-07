/*
 * RegionalFundingForm.java
 * Created : 06-Oct-2005
 */
package org.digijava.module.aim.form;

import java.math.BigDecimal;
import java.util.Collection;

public class RegionalFundingForm extends MainProjectDetailsForm {
	private Collection regionalFundings;
	private Long regionId;
	private BigDecimal totCommitments;
	private BigDecimal totDisbursements;
	private BigDecimal totUnDisbursed;
	private BigDecimal totExpenditures;
	private BigDecimal totUnExpended;
	
	private boolean currFilter;
	private boolean calFilter;
	private boolean goButton;
	
	private String currFilterValue;
	private long calFilterValue;
	private Collection fiscalCalendars;
	

	public RegionalFundingForm() {
		totCommitments = new BigDecimal(0);
		totDisbursements = new BigDecimal(0);
		totUnDisbursed = new BigDecimal(0);
		totExpenditures = new BigDecimal(0);
		totUnExpended = new BigDecimal(0);
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
	public BigDecimal getTotCommitments() {
		return totCommitments;
	}

	/**
	 * @param totCommitments The totCommitments to set.
	 */
	public void setTotCommitments(BigDecimal totCommitments) {
		this.totCommitments = totCommitments;
	}

	/**
	 * @return Returns the totDisbursements.
	 */
	public BigDecimal getTotDisbursements() {
		return totDisbursements;
	}

	/**
	 * @param totDisbursements The totDisbursements to set.
	 */
	public void setTotDisbursements(BigDecimal totDisbursements) {
		this.totDisbursements = totDisbursements;
	}

	/**
	 * @return Returns the totExpenditures.
	 */
	public BigDecimal getTotExpenditures() {
		return totExpenditures;
	}

	/**
	 * @param totExpenditures The totExpenditures to set.
	 */
	public void setTotExpenditures(BigDecimal totExpenditures) {
		this.totExpenditures = totExpenditures;
	}

	/**
	 * @return Returns the totUnDisbursed.
	 */
	public BigDecimal getTotUnDisbursed() {
		return totUnDisbursed;
	}

	/**
	 * @param totUnDisbursed The totUnDisbursed to set.
	 */
	public void setTotUnDisbursed(BigDecimal totUnDisbursed) {
		this.totUnDisbursed = totUnDisbursed;
	}

	/**
	 * @return Returns the totUnExpended.
	 */
	public BigDecimal getTotUnExpended() {
		return totUnExpended;
	}

	/**
	 * @param totUnExpended The totUnExpended to set.
	 */
	public void setTotUnExpended(BigDecimal totUnExpended) {
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

