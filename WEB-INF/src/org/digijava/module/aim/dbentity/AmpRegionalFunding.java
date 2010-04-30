/*
 * AmpRegionalFunding.java
 * Created : 30-Aug-2005 
 */

package org.digijava.module.aim.dbentity;

import java.util.Date;

import org.digijava.module.aim.util.FeaturesUtil;

public class AmpRegionalFunding {
	
	private Long ampRegionalFundingId;
	private AmpActivity activity;
	private Integer transactionType;
	private Integer adjustmentType;
	private Date transactionDate;
	private Date reportingDate;
	private Double transactionAmount;
	private AmpOrganisation reportingOrganization;
	private AmpCurrency currency;
	private String expenditureCategory;
	@Deprecated
	private AmpRegion region;
	
	private AmpCategoryValueLocations regionLocation;
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
	 * @return Returns the adjustmentType.
	 */
	public Integer getAdjustmentType() {
		return adjustmentType;
	}
	/**
	 * @param adjustmentType The adjustmentType to set.
	 */
	public void setAdjustmentType(Integer adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	/**
	 * @return Returns the ampRegionalFundingId.
	 */
	public Long getAmpRegionalFundingId() {
		return ampRegionalFundingId;
	}
	/**
	 * @param ampRegionalFundingId The ampRegionalFundingId to set.
	 */
	public void setAmpRegionalFundingId(Long ampRegionalFundingId) {
		this.ampRegionalFundingId = ampRegionalFundingId;
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
	 * @return Returns the expenditureCategory.
	 */
	public String getExpenditureCategory() {
		return expenditureCategory;
	}
	/**
	 * @param expenditureCategory The expenditureCategory to set.
	 */
	public void setExpenditureCategory(String expenditureCategory) {
		this.expenditureCategory = expenditureCategory;
	}
	
	/**
	 * @return the regionLocation
	 */
	public AmpCategoryValueLocations getRegionLocation() {
		return regionLocation;
	}
	/**
	 * @param regionLocation the regionLocation to set
	 */
	public void setRegionLocation(AmpCategoryValueLocations regionLocation) {
		this.regionLocation = regionLocation;
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
	 * @return Returns the reportingOrganization.
	 */
	public AmpOrganisation getReportingOrganization() {
		return reportingOrganization;
	}
	/**
	 * @param reportingOrganization The reportingOrganization to set.
	 */
	public void setReportingOrganization(AmpOrganisation reportingOrganization) {
		this.reportingOrganization = reportingOrganization;
	}
	/**
	 * @return Returns the transactionAmount.
	 */
	public Double getTransactionAmount() {
		return FeaturesUtil.applyThousandsForVisibility(transactionAmount);
	}
	/**
	 * @param transactionAmount The transactionAmount to set.
	 */
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = FeaturesUtil.applyThousandsForEntry(transactionAmount);
	}
	/**
	 * @return Returns the transactionDate.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate The transactionDate to set.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return Returns the transactionType.
	 */
	public Integer getTransactionType() {
		return transactionType;
	}
	/**
	 * @param transactionType The transactionType to set.
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	/**
	 * @deprecated use regionLocation instead
	 * @return the region
	 */
	@Deprecated
	public AmpRegion getRegion() {
		return region;
	}
	/**
	 * @deprecated use regionLocation instead
	 * @param region the region to set
	 */
	@Deprecated
	public void setRegion(AmpRegion region) {
		this.region = region;
	}
	
	public boolean equals(Object arg) {
		if (arg instanceof AmpRegionalFunding) {
			AmpRegionalFunding regFund = (AmpRegionalFunding) arg;
			return ampRegionalFundingId.equals(regFund.getAmpRegionalFundingId());	
		}
		throw new ClassCastException();
	}
}