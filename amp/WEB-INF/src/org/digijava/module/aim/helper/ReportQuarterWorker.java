package org.digijava.module.aim.helper ;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
@Deprecated
public class ReportQuarterWorker
{
	private static Logger logger = Logger.getLogger(ReportQuarterWorker.class) ;
	
	public static double getAmount(int yr, int qtr, Collection fdoColl)
	{
		double amt =0.0;
		Iterator iterfdoColl = fdoColl.iterator();
		while(iterfdoColl.hasNext()){
			FiscalDO fd1 = (FiscalDO) iterfdoColl.next();
			if(fd1.getFiscalYear() == yr && fd1.getFiscalQuarter() == qtr){
				amt = 5000.0;
			}//
		}//while
		return amt;
	}
	
	
	public static double getAmount1(int yr, int qtr, Long ampFundingId,
	Integer transactionType,
	Integer adjustmentType)
	{
		Collection ampFundings = DbUtil.getDonorFund1(ampFundingId, transactionType, adjustmentType);
		double amt = 0.0;
		if(ampFundings.size() != 0){
			Iterator iterfunding = ampFundings.iterator();
			int i=0;
			while(iterfunding.hasNext()){
				AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterfunding.next();
				//logger.debug("Date is for i " + i +  " :" +ampFundingDetail.getTransactionDate());
				FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(ampFundingDetail.getTransactionDate(),new Long(4));
				//logger.debug("Year is " + fdo.getFiscalYear());
				//logger.debug("Quarter is " + fdo.getFiscalQuarter());
				//logger.debug("Planned Commitment Amount " + ampFundingDetail.getTransactionAmount());
				//logger.debug("Curreny " +ampFundingDetail.getAmpCurrencyId().getCurrencyCode());
				i++;
				if(fdo.getFiscalYear() == yr && fdo.getFiscalQuarter() == qtr){
					if(ampFundingDetail.getAmpCurrencyId().getCurrencyCode().equals("USD")){
						amt = amt + ampFundingDetail.getTransactionAmount().doubleValue();
					}
					else{
						double fromCurrency = CurrencyUtil.getExchangeRate(ampFundingDetail.getAmpCurrencyId().getCurrencyCode());
						double toCurrency = CurrencyUtil.getExchangeRate("USD");
						amt = amt + CurrencyWorker.convert1(ampFundingDetail.getTransactionAmount().doubleValue(),fromCurrency,toCurrency);
					}
					
				}
				else
				{
					logger.debug(" amount null");
				}
					
				logger.debug("next i ");
			}//while
		}//if ampFundings
		return amt;
	}
	public static double getAmountbyYr(int yr, Long ampFundingId,
		Integer transactionType,
		Integer adjustmentType )
	{
	//	logger.info("getAmount1() with year = "+yr ) ;
		double amt = 0.0;
		Collection ampFundings = DbUtil.getDonorFund1(ampFundingId, transactionType, adjustmentType);
	//	logger.info("Funding Size: " + ampFundings.size());
		if(ampFundings.size() != 0){
	//		logger.info("inside if");
			Iterator iterfunding = ampFundings.iterator();
			int i=0;
			while(iterfunding.hasNext()){
				AmpFundingDetail ampFundingDetail = (AmpFundingDetail) iterfunding.next();
			//	logger.info("Date is for i " + i +  " :" +ampFundingDetail.getTransactionDate());
				FiscalDO fdo = FiscalCalendarWorker.getFiscalYrQtr(ampFundingDetail.getTransactionDate(),new Long(4));
			//	logger.info("Year is " + fdo.getFiscalYear());
			//	logger.info("Quarter is " + fdo.getFiscalQuarter());
			//	logger.info("Planned Commitment Amount " + ampFundingDetail.getTransactionAmount());
			//	logger.info("Curreny " +ampFundingDetail.getAmpCurrencyId().getCurrencyCode());
				i++;
				if(fdo.getFiscalYear() == yr ){
					if(ampFundingDetail.getAmpCurrencyId().getCurrencyCode().equals("USD")){
						amt = amt + ampFundingDetail.getTransactionAmount().doubleValue();
					}
					else{
						double fromCurrency = CurrencyUtil.getExchangeRate(ampFundingDetail.getAmpCurrencyId().getCurrencyCode());
						double toCurrency = CurrencyUtil.getExchangeRate("USD");
						amt = amt + CurrencyWorker.convert1(ampFundingDetail.getTransactionAmount().doubleValue(),fromCurrency,toCurrency);
					}
				
				}
				else
				{
	//				logger.info(" amount null");
				}
				
				//logger.debug("next i ");
			}//while
		}//if ampFundings
		return amt;
	}
}
