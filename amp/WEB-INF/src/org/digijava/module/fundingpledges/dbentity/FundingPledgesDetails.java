package org.digijava.module.fundingpledges.dbentity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	private java.sql.Timestamp funding_date;
	private String fundingDate;
	
	private AmpCategoryValue pledgetype;
	private AmpCategoryValue typeOfAssistance;
	private AmpCategoryValue aidmodality;	
	private Double amount;
	private AmpCurrency currency;
	private String currencycode;
	private Long pledgetypeid;
	private Long typeOfAssistanceid;
	private Long aidmodalityid;
	private String fundingYear;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public java.sql.Timestamp getFunding_date() {
        Timestamp retVal = Timestamp.valueOf(new StringBuffer(getFundingYear()).append("-01-01 00:00:00").toString());
        return retVal;
	}
	
	@Deprecated
	public void setFunding_date(java.sql.Timestamp fundingDate) {
		this.funding_date = fundingDate;
		if (fundingDate!=null) {
			java.util.Date date1 = new java.util.Date(fundingDate.getTime());
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        String fundingDateStr = formatter.format(date1);
	        this.fundingDate = fundingDateStr;
		} 
	}
	
	public AmpCategoryValue getPledgetype() {
		return pledgetype;
	}
	public void setPledgetype(AmpCategoryValue pledgetype) {
		this.pledgetype = pledgetype;
		if (pledgetype==null) {
			this.pledgetypeid = -1l;
		} else {
			this.pledgetypeid = pledgetype.getId();
		}
	}
	public AmpCategoryValue getTypeOfAssistance() {
		return typeOfAssistance;
	}
	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
		if (typeOfAssistance == null) {
			this.typeOfAssistanceid = -1l;
		} else {
			this.typeOfAssistanceid = typeOfAssistance.getId();
		}
	}
	public AmpCategoryValue getAidmodality() {
		return aidmodality;
	}
	public void setAidmodality(AmpCategoryValue aidmodality) {
		this.aidmodality = aidmodality;
		if (aidmodality == null) {
			this.aidmodalityid = -1l;
		} else {
			this.aidmodalityid = aidmodality.getId();
		}
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
		this.currencycode = currency.getCurrencyCode();
	}
	/**
	 * @return the fundingDate
	 */
	@Deprecated
	public String getFundingDate() {
		return fundingDate;
	}
	/**
	 * @param fundingDate the fundingDate to set
	 */
	@Deprecated
	public void setFundingDate(String fundingDate) {
		this.fundingDate = fundingDate;
		if (fundingDate!=null && fundingDate.length()>0) {
			try {
				DateFormat formatter ;
				java.util.Date date ;
				formatter = new SimpleDateFormat("dd/MM/yyyy");
				date = formatter.parse(fundingDate);
				this.funding_date = new Timestamp(date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public String getFundingYear() {
		return fundingYear;
	}
	public void setFundingYear(String fundingYear) {
		this.fundingYear = fundingYear;
	}
}
