package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class FundingPledgesDetails {
	private long id;
	private FundingPledges pledgeid;
	public FundingPledges getPledgeid() {
		return pledgeid;
	}
	public void setPledgeid(FundingPledges pledgeid) {
		this.pledgeid = pledgeid;
	}
	private java.sql.Time funding_date;
	private AmpCategoryValue pledgetype;
	private AmpCategoryValue typeOfAssistance;
	private AmpCategoryValue aidmodality;	
	private Double amount;
	private AmpCurrency currency;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public java.sql.Time getFunding_date() {
		return funding_date;
	}
	public void setFunding_date(java.sql.Time fundingDate) {
		funding_date = fundingDate;
	}
	
	public AmpCategoryValue getPledgetype() {
		return pledgetype;
	}
	public void setPledgetype(AmpCategoryValue pledgetype) {
		this.pledgetype = pledgetype;
	}
	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}
	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
	}
	public AmpCategoryValue getAidmodality() {
		return aidmodality;
	}
	public void setAidmodality(AmpCategoryValue aidmodality) {
		this.aidmodality = aidmodality;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public AmpCurrency getCurrency() {
		return currency;
	}
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
	}
	
}
