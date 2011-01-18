package org.digijava.module.aim.helper ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.logic.Logic;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;

public class FinancingBreakdownWorker
{
	private static Logger logger = Logger.getLogger(FinancingBreakdownWorker.class) ;
	public static String getDonor(AmpFunding ampFunding)
	{
			AmpOrganisation ampOrganisation = ampFunding.getAmpDonorOrgId();
			if ( logger.isDebugEnabled() )
			logger.debug("getDonor() of worker class returning organisation "
			+ ampOrganisation.getName() ) ;
			if (ampOrganisation!=null)
				return ampOrganisation.getName() ;
			return"";
	}

	public static String getTotalProjections(FilterParams fp) {
		Collection amounts	= QuarterlyInfoWorker.getQuarterlyForProjections(fp, true);
		double totals		= 0;
		Iterator iter		= amounts.iterator();
		while (iter.hasNext()) {
			double temp	= (Double)iter.next();
			totals			+= temp;
		}

		return FormatHelper.formatNumber(totals);
	}

	public static String getTotalDonorFund(FilterParams fp, boolean isDebug)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getTotalDonorFund() with FilterParams " +
						 " transactionType=" + fp.getTransactionType() +
						 " ampFunding=" + fp.getAmpFundingId() );
		DecimalWraper total = new DecimalWraper();
		Collection<YearlyInfo> c = YearlyInfoWorker.getYearlyInfo(fp);
		if ( c.size() != 0 )	{
				total = Logic.getInstance().getTotalDonorFundingCalculator().getTotalDonorFunding(c, fp);
			}
		if (total.getValue()!=null){
			if(isDebug){
				return total.toString()+ "=" +  total.getCalculations();
			}
			else{
				logger.debug("getTotalDonorFund() returning " + FormatHelper.formatNumber(total.getValue().doubleValue()));
				return FormatHelper.formatNumber(total.getValue().doubleValue());
			}
		}
		return "";
	}

	public static String getTotalDonorDisbursement(Long ampFundingId,
												   double fromExchangeRate,
												   double toExchangeRate)
	{
		Integer trsType = new Integer(Constants.DISBURSEMENT) ;
		Integer adjType = new Integer(Constants.ACTUAL) ;
		double total = DbUtil.getTotalDonorFund(ampFundingId,trsType,adjType) ;
		String strTotal = CurrencyWorker.convert(total,fromExchangeRate,toExchangeRate) ;
		if ( logger.isDebugEnabled() )
		logger.debug("getTotalDonorDisbursement() strTotal " + strTotal ) ;
		return strTotal ;
	}

	public static String getTotalDonorExpenditure(Long ampFundingId,
												  double fromExchangeRate,
	 											  double toExchangeRate)
	{
		Integer trsType = new Integer(Constants.EXPENDITURE) ;
		Integer adjType = new Integer(Constants.ACTUAL) ;
		double total = DbUtil.getTotalDonorFund(ampFundingId,trsType,adjType) ;
		String strTotal =  CurrencyWorker.convert(total,fromExchangeRate,toExchangeRate) ;
		return strTotal ;
	}

	public static String getOverallTotal(Collection c,int type,boolean isDebug)
	{
		if ( logger.isDebugEnabled() )
			logger.debug("getOverallTotal(Collection c size=" + c.size()
					   + ", type= " + type ) ;
		Iterator iter = c.iterator() ;
		FinancingBreakdown financingBreakdown = null ;
		String strTotal = "" ;
		double total = 0.0 ;
		String s1 = "" ;
		String s2 = "" ;
		while ( iter.hasNext() )
		{
			financingBreakdown = (FinancingBreakdown)iter.next() ;
			if ( type == Constants.COMMITMENT ){
				if (!isDebug){
					s1 = financingBreakdown.getTotalCommitted() ;
					total += FormatHelper.parseDouble(s1) ;
				}
				else{
					if ("".equalsIgnoreCase(s1)){
						s1 = financingBreakdown.getTotalCommitted();
					}
					else{
						s1 = s1 + "+" + financingBreakdown.getTotalCommitted();
					}
				}
			}
			else if ( type == Constants.DISBURSEMENT ){
				if(!isDebug){
					s1 = financingBreakdown.getTotalDisbursed() ;
					total += FormatHelper.parseDouble(s1) ;
				}
				else{
					if ("".equalsIgnoreCase(s1)){
						s1 = financingBreakdown.getTotalDisbursed();
					}
					else{
						s1 = s1 + "+" + financingBreakdown.getTotalDisbursed();
					}
					
				}
			}
			else if ( type == Constants.EXPENDITURE ){
				if(!isDebug){
					s1 = financingBreakdown.getTotalExpended() ;
					total += FormatHelper.parseDouble(s1) ;
				}
				else{
					if("".equalsIgnoreCase(s1)){
						s1 =  financingBreakdown.getTotalExpended();
					}
					else{
						s1 = s1 + "+" + financingBreakdown.getTotalExpended();
					}
				}
			}
			else if ( type == Constants.DISBURSEMENT_ORDER ){
				if(!isDebug){
					s1 = financingBreakdown.getTotalDisbOrdered() ;
					total += FormatHelper.parseDouble(s1) ;
				}
				else{
					if("".equalsIgnoreCase(s1)){
						s1 = s1 + "+" + financingBreakdown.getTotalDisbOrdered();
					}
					else{
						s1 =  financingBreakdown.getTotalDisbOrdered();
					}
				}
			}
			else if ( type == Constants.MTEFPROJECTION ) {
				if(!isDebug){
					s1 = financingBreakdown.getTotalProjection() ;
					total += FormatHelper.parseDouble(s1) ;
				}
				else{
					if("".equalsIgnoreCase(s1)){
						s1 =  financingBreakdown.getTotalProjection();
					}
					else{
						s1 = s1 + "+" + financingBreakdown.getTotalProjection();
					}
				}
			}
		}
		if ( logger.isDebugEnabled() )
		logger.debug("getOverallTotal(Collection c , type= " + type + ") returning total " + total ) ;
		if (isDebug){
			return s1;
		}
		strTotal= FormatHelper.formatNumber(total);
		return strTotal ;
	}

	public static Collection<FinancingBreakdown> getFinancingBreakdownList(Long ampActivityId,
													   Collection<AmpFunding> ampFundings,
													   FilterParams fp,boolean isDebug)	{
		if ( logger.isDebugEnabled() )
			logger.debug("GETFINANCINGBREAKDOWNLIST() PASSED AMPACTIVITYID : " + ampActivityId);

		String actualStartDate = "";
		String actualCompletionDate = "";
		List<FinancingBreakdown> temp = new ArrayList<FinancingBreakdown>();

		if ( ampFundings != null )	{
			Iterator<AmpFunding> iter = ampFundings.iterator();
 			while ( iter.hasNext() )	{


				AmpFunding ampFunding = (AmpFunding) iter.next();

				FinancingBreakdown financingBreakdown = new FinancingBreakdown();
				financingBreakdown.setAmpFundingId(ampFunding.getAmpFundingId().longValue());
				financingBreakdown.setFinancingId(ampFunding.getFinancingId());

				String donor = FinancingBreakdownWorker.getDonor(ampFunding);
				financingBreakdown.setDonor(donor);

				AmpOrganisation ampOrg = ampFunding.getAmpDonorOrgId();
//				String goeId = DbUtil.getGoeId(ampActivityId);
//				financingBreakdown.setGoeId(goeId);
				financingBreakdown.setOrganisation(ampOrg);

				financingBreakdown.setSignatureDate(DateConversion.
						ConvertDateToString(ampFunding.getSignatureDate()));

				fp.setAmpFundingId(ampFunding.getAmpFundingId());
				fp.setTransactionType(Constants.COMMITMENT);
				String totalDonorCommitment = getTotalDonorFund(fp,isDebug) ;
				financingBreakdown.setTotalCommitted(totalDonorCommitment) ;

				fp.setTransactionType(Constants.DISBURSEMENT);
				String totalDonorDisbursement = getTotalDonorFund(fp,isDebug) ;
				financingBreakdown.setTotalDisbursed(totalDonorDisbursement) ;
				String unDisbursed ="";
				if (isDebug){
					unDisbursed = "(" + totalDonorCommitment + ")" + "-" + "("
							+ totalDonorDisbursement + ")";
				}
				else{
					unDisbursed = FormatHelper.getDifference(totalDonorCommitment,totalDonorDisbursement) ;
				}	
				financingBreakdown.setUnDisbursed(unDisbursed);

				fp.setTransactionType(Constants.EXPENDITURE);
				String totalDonorExpenditure = getTotalDonorFund(fp,isDebug);
				financingBreakdown.setTotalExpended(totalDonorExpenditure) ;

                fp.setTransactionType(Constants.DISBURSEMENT_ORDER);
				String totalDisbOrdered = getTotalDonorFund(fp, isDebug);
				financingBreakdown.setTotalDisbOrdered(totalDisbOrdered);
				financingBreakdown.setTotalProjection(getTotalProjections(fp));
				String unExpended = "";
				if (isDebug){
					unExpended = "(" + totalDonorDisbursement + ")" + "-" + "("
							+ totalDonorExpenditure + ")";
				}
				else{
					unExpended = FormatHelper.getDifference(totalDonorDisbursement,totalDonorExpenditure) ;
				}
				
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		FinancingBreakdownWorker.logger = logger;
	}
}
