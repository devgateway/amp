package org.digijava.module.fundingpledges.dbentity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

@Getter @Setter
public class FundingPledgesDetails {
	private long id;
	private FundingPledges pledgeid;
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
	
	public void setTypeOfAssistanceid(Long typeOfAssistanceid) {
		this.typeOfAssistanceid = typeOfAssistanceid;
		this.typeOfAssistance = CategoryManagerUtil.getAmpCategoryValueFromDb(this.typeOfAssistanceid);
	}

	public void setAidmodalityid(Long aidmodalityid) {
		this.aidmodalityid = aidmodalityid;
		this.aidmodality = CategoryManagerUtil.getAmpCategoryValueFromDb(this.aidmodalityid);
	}
	
	public void setPledgetypeid(Long pledgetypeid) {
		this.pledgetypeid = pledgetypeid;
		this.pledgetype = CategoryManagerUtil.getAmpCategoryValueFromDb(this.pledgetypeid);
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
		this.setCurrency(CurrencyUtil.getAmpcurrency(currencycode));
	}
	
	public java.sql.Timestamp getFunding_date() {
        Timestamp retVal = Timestamp.valueOf(new StringBuffer(getFundingYear()).append("-01-01 00:00:00").toString());
        return retVal;
	}
	
	public void setPledgetype(AmpCategoryValue pledgetype) {
		this.pledgetype = pledgetype;
		if (pledgetype==null) {
			this.pledgetypeid = -1l;
		} else {
			this.pledgetypeid = pledgetype.getId();
		}
	}
	
	public void setTypeOfAssistance(AmpCategoryValue typeOfAssistance) {
		this.typeOfAssistance = typeOfAssistance;
		if (typeOfAssistance == null) {
			this.typeOfAssistanceid = -1l;
		} else {
			this.typeOfAssistanceid = typeOfAssistance.getId();
		}
	}

	public void setAidmodality(AmpCategoryValue aidmodality) {
		this.aidmodality = aidmodality;
		if (aidmodality == null) {
			this.aidmodalityid = -1l;
		} else {
			this.aidmodalityid = aidmodality.getId();
		}
	}
	
	public void setCurrency(AmpCurrency currency) {
		this.currency = currency;
		this.currencycode = currency.getCurrencyCode();
	}
}
