/*
 * RegionalFunding.java
 * Created : 01-Sep-2005
 */

package org.digijava.module.aim.helper;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * The class represents a regional funding object which contains 
 * data about a region and the funding allocated to that region
 * 
 * @author Priyajith
 */
public class RegionalFunding {

	/** The database primary key of the region */
	private Long regionId;
	
	/** The region name */
	private String regionName;
	
	/**
	 * The collection of funding details object which is of type
	 * org.digijava.module.aim.helper.FundingDetail
	 */
	private Collection commitments;
	private Collection disbursements;
	private Collection expenditures;
	private BigDecimal totCommitments;
	private BigDecimal totDisbursements;
	private BigDecimal totExpenditures;
	private BigDecimal totUnDisbursed;
	private BigDecimal totUnExpended;
	
	public RegionalFunding() {
		totCommitments = new BigDecimal(0);
		totDisbursements =  new BigDecimal(0);
		totExpenditures =  new BigDecimal(0);
		totUnDisbursed =  new BigDecimal(0);
		totUnExpended =  new BigDecimal(0);
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
	 * @return Returns the regionName.
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionName The regionName to set.
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg) {
		if (arg instanceof RegionalFunding) {
			RegionalFunding regFund = (RegionalFunding) arg;
			return regFund.regionId.equals(regionId);
		} else {
			throw new ClassCastException();			
		}
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
	
	

}