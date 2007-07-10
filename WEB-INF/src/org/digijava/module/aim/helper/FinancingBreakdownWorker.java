package org.digijava.module.aim.helper ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;
 
public class FinancingBreakdownWorker
{
	private static Logger logger = Logger.getLogger(FinancingBreakdownWorker.class) ;
	
	public static String getDonor(AmpFunding ampFunding)
	{
			AmpOrganisation ampOrganisation = ampFunding.getAmpDonorOrgId();
			if ( logger.isDebugEnabled() )
			logger.debug("getDonor() of worker class returning organisation " 
			+ ampOrganisation.getName() ) ;
			return ampOrganisation.getName() ;
	}
	
	public static String getTotalDonorFund(FilterParams fp)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getTotalDonorFund() with FilterParams " +						 " transactionType=" + fp.getTransactionType() +						 " ampFunding=" + fp.getAmpFundingId() );
		double total = 0.0;
		Collection c = YearlyInfoWorker.getYearlyInfo(fp);
		if ( c.size() != 0 )	{
			Iterator iter = c.iterator();
		
			while ( iter.hasNext() )	{
				YearlyInfo yf = (YearlyInfo)iter.next();
				if ( yf.getActualAmount() != null && (!yf.getActualAmount().equals("NA")) )	{
					double temp = DecimalToText.getDouble(yf.getActualAmount());
					total += temp;
				}
			}
		}
		
		if ( logger.isDebugEnabled() )
			logger.debug("getTotalDonorFund() returning " + 
			DecimalToText.getString(total));
			
		return DecimalToText.getString(total);
	}
	
	public static String getTotalDonorDisbursement(Long ampFundingId,
												   double fromExchangeRate,
												   double toExchangeRate,
												   String perspective)
	{
		Integer trsType = new Integer(Constants.DISBURSEMENT) ;
		Integer adjType = new Integer(Constants.ACTUAL) ;
		double total = DbUtil.getTotalDonorFund(ampFundingId,trsType,adjType,perspective) ;
		String strTotal = CurrencyWorker.convert(total,fromExchangeRate,toExchangeRate) ;
		if ( logger.isDebugEnabled() )
		logger.debug("getTotalDonorDisbursement() strTotal " + strTotal ) ;
		return strTotal ;	
	}

	public static String getTotalDonorExpenditure(Long ampFundingId,
												  double fromExchangeRate,
	 											  double toExchangeRate,
	 											  String perspective)
	{
		Integer trsType = new Integer(Constants.EXPENDITURE) ;
		Integer adjType = new Integer(Constants.ACTUAL) ;
		double total = DbUtil.getTotalDonorFund(ampFundingId,trsType,adjType,perspective) ;
		String strTotal =  CurrencyWorker.convert(total,fromExchangeRate,toExchangeRate) ;
		return strTotal ; 
	}

	public static String getOverallTotal(Collection c,int type)
	{
		if ( logger.isDebugEnabled() )
		logger.debug("getOverallTotal(Collection c size=" + c.size()
		+", type= " + type ) ;
		Iterator iter = c.iterator() ;
		FinancingBreakdown financingBreakdown = null ;
		String strTotal = "" ;
		double total = 0.0 ;
		String s1 = "" ;
		String s2 = "" ;
		while ( iter.hasNext() )
		{
			financingBreakdown = (FinancingBreakdown)iter.next() ;
			if ( type == 0 )
			{		  
					s1 = financingBreakdown.getTotalCommitted() ;
					s2 = DecimalToText.removeCommas(s1) ;
					total += Double.parseDouble(s2) ;
			}
			else if ( type == 1 )
			{
				s1 = financingBreakdown.getTotalDisbursed() ;
				s2 = DecimalToText.removeCommas(s1) ;
				total += Double.parseDouble(s2) ;
			}
			else if ( type == 2 )
			{
				s1 = financingBreakdown.getTotalExpended() ;
				s2 = DecimalToText.removeCommas(s1) ;
				total += Double.parseDouble(s2) ;
			}
		}
		if ( logger.isDebugEnabled() )
		logger.debug("getOverallTotal(Collection c , type= " + type 
		+ ") returning total " + total ) ;
		strTotal = DecimalToText.ConvertDecimalToText(total) ;
		return strTotal ;	
	}
	
	public static Collection getFinancingBreakdownList(Long ampActivityId,
													   Collection ampFundings,
													   FilterParams fp)	{
		if ( logger.isDebugEnabled() )
			logger.debug("GETFINANCINGBREAKDOWNLIST() PASSED AMPACTIVITYID : " + ampActivityId);
			
		Iterator iter = null;
		String actualStartDate = "";
		String actualCompletionDate = "";
		ArrayList temp = new ArrayList();
		
		if ( ampFundings != null )	{
			iter = ampFundings.iterator();
 			while ( iter.hasNext() )	{
				FinancingBreakdown financingBreakdown = new FinancingBreakdown();
				AmpFunding ampFunding = (AmpFunding) iter.next();
				financingBreakdown.setAmpFundingId(ampFunding.getAmpFundingId().longValue());
				financingBreakdown.setFinancingId(ampFunding.getFinancingId());
				String donor = FinancingBreakdownWorker.getDonor(ampFunding);
				financingBreakdown.setDonor(donor);
				AmpOrganisation ampOrg = ampFunding.getAmpDonorOrgId();
				String goeId = DbUtil.getGoeId(ampActivityId);
				financingBreakdown.setGoeId(goeId);
				financingBreakdown.setOrganisation(ampOrg);
				
				financingBreakdown.setSignatureDate(DateConversion.
						ConvertDateToString(ampFunding.getSignatureDate()));
				
				fp.setAmpFundingId(ampFunding.getAmpFundingId());
				fp.setTransactionType(Constants.COMMITMENT);
				String totalDonorCommitment = getTotalDonorFund(fp) ;
				financingBreakdown.setTotalCommitted(totalDonorCommitment) ;
				
				fp.setTransactionType(Constants.DISBURSEMENT);
				String totalDonorDisbursement = getTotalDonorFund(fp) ;
				financingBreakdown.setTotalDisbursed(totalDonorDisbursement) ;
				
				String unDisbursed = DecimalToText.getDifference(totalDonorCommitment,totalDonorDisbursement) ;
				financingBreakdown.setUnDisbursed(unDisbursed);
				
				fp.setTransactionType(Constants.EXPENDITURE);
				String totalDonorExpenditure = getTotalDonorFund(fp);
				financingBreakdown.setTotalExpended(totalDonorExpenditure) ;
				
				String unExpended = DecimalToText.getDifference(totalDonorDisbursement,totalDonorExpenditure) ;
				financingBreakdown.setUnExpended(unExpended) ;
				if ( ampFunding.getActualStartDate() != null )	{
					actualStartDate = DateConversion.ConvertDateToString(ampFunding.getActualStartDate()) ;
				}
				financingBreakdown.setActualStartDate(actualStartDate) ;
				
				if ( ampFunding.getActualCompletionDate() != null )	{
					actualCompletionDate = DateConversion.ConvertDateToString(ampFunding.getActualCompletionDate()) ;
				}
				financingBreakdown.setActualCompletionDate(actualCompletionDate) ;
				temp.add(financingBreakdown) ;
				
			}
		}
		else
		{
		}
		if ( logger.isDebugEnabled() )
					logger.debug("GETFINANCINGBREAKDOWNLIST() RETURNING A COLLECTION OF SIZE : " + temp.size());
	 	return temp ;
	}
}
