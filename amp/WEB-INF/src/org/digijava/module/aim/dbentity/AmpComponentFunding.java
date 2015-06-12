/*
 * AmpComponentFunding.java
 * Created : 30-Aug-2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Output;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpComponentFunding implements Versionable, Cloneable, Serializable {
	@Interchangeable(fieldTitle="ID")
	private Long ampComponentFundingId;
//	@Interchangeable(fieldTitle="Activity")
	private AmpActivityVersion activity;
	@Interchangeable(fieldTitle="Transaction Type")
	private Integer transactionType;
	@Interchangeable(fieldTitle="Adjustment Type")
	private AmpCategoryValue adjustmentType ;
	@Interchangeable(fieldTitle="Transaction Date")
	private Date transactionDate;
	@Interchangeable(fieldTitle="Reporting Date")
	private Date reportingDate;
	@Interchangeable(fieldTitle="Transaction Amount")
	private Double transactionAmount;
    //reusing field to store the organisation related to the current component
	@Interchangeable(fieldTitle="Reporting Organization")
	private AmpOrganisation reportingOrganization;
	@Interchangeable(fieldTitle="Currency")
	private AmpCurrency currency;
	@Interchangeable(fieldTitle="Description")
	private String description;
	@Interchangeable(fieldTitle="Component")
	private AmpComponent component;
	@Interchangeable(fieldTitle="Exchange Rate")
	private Float exchangeRate;
	
	/**
	 * @return Returns the activity.
	 */
	public AmpActivityVersion getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}
	/**
	 * @return Returns the adjustmentType.
	 */
	public AmpCategoryValue getAdjustmentType() {
		return adjustmentType;
	}
	/**
	 * @param adjustmentType The adjustmentType to set.
	 */
	public void setAdjustmentType(AmpCategoryValue adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	/**
	 * @return Returns the ampComponentFundingId.
	 */
	public Long getAmpComponentFundingId() {
		return ampComponentFundingId;
	}
	/**
	 * @param ampComponentFundingId The ampComponentFundingId to set.
	 */
	public void setAmpComponentFundingId(Long ampComponentFundingId) {
		this.ampComponentFundingId = ampComponentFundingId;
	}
	/**
	 * @return Returns the component.
	 */
	public AmpComponent getComponent() {
		return component;
	}
	/**
	 * @param component The component to set.
	 */
	public void setComponent(AmpComponent component) {
		this.component = component;
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
	 * If overriding is really needed please fully comment on the reason!
	 *
	@Override
	public int hashCode() {
		if (this.ampComponentFundingId == null)
			return 0;
		return ampComponentFundingId.intValue();
	}
	*/
	
	/*public boolean equals(Object obj) {
		if (obj == null) throw new NullPointerException();
		if (!(obj instanceof AmpComponentFunding)) throw new ClassCastException();
		if(this.ampComponentFundingId == null) 
			return super.equals(obj);
		
		AmpComponentFunding comp = (AmpComponentFunding) obj;
		return this.ampComponentFundingId.equals(comp.ampComponentFundingId);
		
	}*/
	public void setExchangeRate(Float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Float getExchangeRate() {
		return exchangeRate;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpComponentFunding aux = (AmpComponentFunding) obj;
		String original = " " + this.currency + this.transactionType + this.transactionAmount + this.transactionDate
				+ this.adjustmentType.getValue() + this.reportingDate + this.reportingOrganization + this.description
				+ this.component.getTitle() + this.exchangeRate;
		String copy = " " + aux.currency + aux.transactionType + aux.transactionAmount + aux.transactionDate
				+ aux.adjustmentType.getValue() + aux.reportingDate + aux.reportingOrganization + aux.description
				+ aux.component.getTitle() + aux.exchangeRate;
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		String transactionType = "";
		switch (this.transactionType.intValue()) {
		case 0:
			transactionType = "Commitments";
			break;
		case 1:
			transactionType = "Disbursements";
			break;
		case 2:
			transactionType = "Expenditures";
			break;
		case 3:
			transactionType = "Disbursement Orders";
			break;
		case 4:
			transactionType = "MTEF Projection";
			break;
		}
		out.getOutputs().add(new Output(null, new String[] { "Trn" }, new Object[] { transactionType }));
		out.getOutputs().add(
				new Output(null, new String[] { "Value" }, new Object[] {
						 " " + this.adjustmentType.getValue() + " - " , this.transactionAmount,
						" ", this.currency, " - ", this.transactionDate }));
		return out;
	}

	@Override
	public Object getValue() {
		return " " + this.currency + this.transactionType + (this.transactionAmount==null?"null":this.transactionAmount.longValue()) + this.transactionDate
				+ this.adjustmentType.getValue() + this.reportingDate + this.description
				+ this.component.getTitle() + (this.exchangeRate==null?"null":this.exchangeRate.floatValue());
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpComponentFunding aux = (AmpComponentFunding) clone();
		aux.activity = newActivity;
		aux.ampComponentFundingId = null;
		return aux;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}