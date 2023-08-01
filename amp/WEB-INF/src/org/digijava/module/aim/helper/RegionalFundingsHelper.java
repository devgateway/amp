/*
 * RegionalFundingsHelper.java
 * Created: 17-Nov-2005
 */

package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.*;

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
            rf.setRegionId( regFund.getRegionLocation().getId() );
            rf.setRegionName( regFund.getRegionLocation().getName());
            int index = -1;
            if (temp.contains(rf) == true) {
                index = temp.indexOf(rf);
                rf = (RegionalFunding) temp.get(index);
            }
            FundingDetail fd = new FundingDetail();
            
            fd.setCurrencyName(regFund.getCurrency().getCurrencyName());
            fd.setCurrencyCode(regFund.getCurrency().getCurrencyCode());
            fd.setTransactionAmount(FormatHelper.formatNumber(regFund.getTransactionAmountWithFormatConversion()));
            
            String tsDate = DateConversion.convertDateToLocalizedString(regFund.getTransactionDate());
            fd.setTransactionDate(tsDate);
            fd.setFiscalYear(DateConversion.convertDateToFiscalYearString(regFund.getTransactionDate()));

            
            fd.setTransactionType(regFund.getTransactionType().intValue());

            Date dt = regFund.getTransactionDate();
            double frmExRt = Util.getExchange(fd.getCurrencyCode(),new java.sql.Date(dt.getTime()));
            double toExRt = Util.getExchange(currCode,new java.sql.Date(dt.getTime()));
            double amt = CurrencyWorker.convert1(regFund.getTransactionAmountWithFormatConversion(), frmExRt, toExRt);
            fd.setTransactionAmount(FormatHelper.formatNumber(amt));
            fd.setCurrencyCode(currCode);
            fd.setAdjustmentTypeName(regFund.getAdjustmentType());
            //fd.setAdjustmentType(regFund.getAdjustmentType().intValue());
            if (fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()) ) {               
                amt = 0;
            } 
            
            if (fd.getTransactionType() == Constants.COMMITMENT) {
                if (rf.getCommitments() == null) {
                    rf.setCommitments(new ArrayList());
                }
                rf.getCommitments().add(fd);
                if (fd.getAdjustmentTypeName().getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    amt += rf.getTotCommitments();
                    rf.setTotCommitments(amt);                      
                }
            } else if (fd.getTransactionType() == Constants.DISBURSEMENT) {
                if (rf.getDisbursements() == null) {
                    rf.setDisbursements(new ArrayList());
                }
                rf.getDisbursements().add(fd);
                if (fd.getAdjustmentTypeName().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    amt += rf.getTotDisbursements();
                    rf.setTotDisbursements(amt);                        
                }                   
            } else if (fd.getTransactionType() == Constants.EXPENDITURE) {
                if (rf.getExpenditures() == null) {
                    rf.setExpenditures(new ArrayList());
                }
                rf.getExpenditures().add(fd);
                if (fd.getAdjustmentTypeName().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    amt += rf.getTotExpenditures();
                    rf.setTotExpenditures(amt);                     
                }                   
            }
            
            List list = null;
            if (rf.getCommitments() != null) {
                list = new ArrayList(rf.getCommitments());
                Collections.sort(list,FundingValidator.dateComp);
            }
            rf.setCommitments(list);
            list = null;
            if (rf.getDisbursements() != null) {
                list = new ArrayList(rf.getDisbursements());
                Collections.sort(list,FundingValidator.dateComp);
            }
            rf.setDisbursements(list);
            list = null;
            if (rf.getExpenditures() != null) {
                list = new ArrayList(rf.getExpenditures());
                Collections.sort(list,FundingValidator.dateComp);
            }
            rf.setExpenditures(list);
            
            if (index > -1) {
                temp.set(index,rf);
            } else {
                temp.add(rf);
            }
        }       
        
        Comparator<RegionalFunding> c= new RegionalFunding.RegionalFundingComparator();
        Collections.sort(temp, c);
        return temp;
    }
}
