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
	private String fundingYear;
	
	public java.sql.Timestamp getFunding_date() {
        Timestamp retVal = Timestamp.valueOf(new StringBuffer(getFundingYear()).append("-01-01 00:00:00").toString());
        return retVal;
	}
}
