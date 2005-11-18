/*
 * RegionalFundingsHelper.java
 * Created: 17-Nov-2005
 */

package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.util.DbUtil;

/**
 * Helper class for RegionalFundings manipulation
 
 * @author priyajith
 */
public class RegionalFundingsHelper {
	
	private static Logger logger = Logger.getLogger(RegionalFundingsHelper.class); 
	
	/**
	 * Generates a collection of RegionalFundings objects
	 * @param ampRegFunds The collection containing AmpRegionalFundingObjects
	 * @param currCode The currency code to which the currency of the fundings 
	 * need to be converted
	 * @param calCode The calendar in which the calendar field of RegionalFunding 
	 * objects need to be converted   
	 * @return
	 */
	public static ArrayList getRegionalFundings(Collection ampRegFunds,
			String currCode,long calCode) {
		
		Iterator itr = ampRegFunds.iterator();
		
		ArrayList temp = new ArrayList();
		while (itr.hasNext()) {
			AmpRegionalFunding regFund = (AmpRegionalFunding) itr.next();
			RegionalFunding rf = new RegionalFunding();
			rf.setRegionId(regFund.getRegion().getAmpRegionId());
			rf.setRegionName(regFund.getRegion().getName());
			int index = -1;
			if (temp.contains(rf) == true) {
				index = temp.indexOf(rf);
				rf = (RegionalFunding) temp.get(index);
			}
			FundingDetail fd = new FundingDetail();
			
			fd.setCurrencyName(regFund.getCurrency().getCurrencyName());
			fd.setCurrencyCode(regFund.getCurrency().getCurrencyCode());
			fd.setPerspectiveCode(regFund.getPerspective().getCode());
			fd.setPerspectiveName(regFund.getPerspective().getName());
			fd.setTransactionAmount(DecimalToText.getString(regFund.getTransactionAmount().doubleValue()));
			
			String tsDate = DateConversion.ConvertDateToString(regFund.getTransactionDate());
			if (calCode == Constants.ETH_CAL.longValue() || 
					calCode == Constants.ETH_FY.longValue()) {
				// Ethiopian Calendar or Ethiopian Fiscal Year
				fd.setTransactionDate(EthDateWorker.getEthDate(tsDate));
			} else {
				// Gregorian calendar
				fd.setTransactionDate(tsDate);
			}
			
			fd.setTransactionType(regFund.getTransactionType().intValue());

			Date dt = regFund.getTransactionDate();
			double frmExRt = DbUtil.getExchangeRate(fd.getCurrencyCode(),1,dt);
			double toExRt = DbUtil.getExchangeRate(currCode,1,dt);
			double amt = CurrencyWorker.convert1(regFund.getTransactionAmount().doubleValue(),frmExRt,toExRt);
			fd.setTransactionAmount(DecimalToText.ConvertDecimalToText(amt));
			fd.setCurrencyCode(currCode);
			fd.setAdjustmentType(regFund.getAdjustmentType().intValue());
			if (fd.getAdjustmentType() == Constants.PLANNED) {
				fd.setAdjustmentTypeName("Planned");
				amt = 0;
			} else if (fd.getAdjustmentType() == Constants.ACTUAL) {
				fd.setAdjustmentTypeName("Actual");
			}
			
			if (fd.getTransactionType() == Constants.COMMITMENT) {
				if (rf.getCommitments() == null) {
					rf.setCommitments(new ArrayList());
				}
				rf.getCommitments().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotCommitments();
					rf.setTotCommitments(amt);						
				}
			} else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
				if (rf.getDisbursements() == null) {
					rf.setDisbursements(new ArrayList());
				}
				rf.getDisbursements().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotDisbursements();
					rf.setTotDisbursements(amt);						
				}					
			} else if (fd.getTransactionType() == Constants.EXPENDITURE) {
				if (rf.getExpenditures() == null) {
					rf.setExpenditures(new ArrayList());
				}
				rf.getExpenditures().add(fd);
				if (fd.getAdjustmentType() == Constants.ACTUAL) {
					amt += rf.getTotExpenditures();
					rf.setTotExpenditures(amt);						
				}					
			}
			if (index > -1) {
				temp.set(index,rf);
			} else {
				temp.add(rf);
			}
		}		
		
		return temp;
	}
}