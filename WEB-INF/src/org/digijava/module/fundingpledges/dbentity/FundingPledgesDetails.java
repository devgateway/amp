package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
	private String currencycode;
	private Long pledgetypeid;
	private Long typeOfAssistanceid;
	private Long aidmodalityid;
	
	public Long getTypeOfAssistanceid() {
		return typeOfAssistanceid;
	}
	public void setTypeOfAssistanceid(Long typeOfAssistanceid) {
		this.typeOfAssistanceid = typeOfAssistanceid;
		this.typeOfAssistance = CategoryManagerUtil.getAmpCategoryValueFromDb(this.typeOfAssistanceid);
	}
	public Long getAidmodalityid() {
		return aidmodalityid;
	}
	public void setAidmodalityid(Long aidmodalityid) {
		this.aidmodalityid = aidmodalityid;
		this.aidmodality = CategoryManagerUtil.getAmpCategoryValueFromDb(this.aidmodalityid);
	}
	public Long getPledgetypeid() {
		return pledgetypeid;
	}
	public void setPledgetypeid(Long pledgetypeid) {
		this.pledgetypeid = pledgetypeid;
		this.pledgetype = CategoryManagerUtil.getAmpCategoryValueFromDb(this.pledgetypeid);
	}
	public String getCurrencycode() {
		return currencycode;
	}
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
		this.setCurrency(CurrencyUtil.getAmpcurrency(currencycode));
	}
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
