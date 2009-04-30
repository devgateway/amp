package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class YearlyDiscrepancyAllWorker	{
	
	private static Logger logger = Logger.getLogger(YearlyDiscrepancyAllWorker.class);
	private static ArrayList arrList;
	
	public static Collection getDiscrepancy(FilterParams filterParams)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() with ampFundingId : " + filterParams.getAmpFundingId() );	
		//donor perspective
		FilterParams fp = new FilterParams();
		fp.copy(filterParams);
		fp.setTransactionType(Constants.COMMITMENT);
		Collection c = YearlyDiscrepancyWorker.getYearlyDiscrepancy(fp);
		arrList = new ArrayList();
		Iterator iter = c.iterator();
		while ( iter.hasNext() )	{
			YearlyDiscrepancy discrepancy = (YearlyDiscrepancy)iter.next();
			YearlyDiscrepancyAll discrepancyAll = new YearlyDiscrepancyAll();
			discrepancyAll.setFiscalYear(discrepancy.getFiscalYear());
			discrepancyAll.setCommitmentDonorActual(discrepancy.getDonorActual());
			discrepancyAll.setCommitmentImplAgencyActual(discrepancy.getImplAgencyActual());
			discrepancyAll.setCommitmentMofedActual(discrepancy.getMofedActual());
			arrList.add(discrepancyAll);
		}
		fp.setTransactionType(Constants.DISBURSEMENT);
		c = YearlyDiscrepancyWorker.getYearlyDiscrepancy(fp);
		if ( c.size() > 0 )	
			merge(fp,c);
			
		fp.setTransactionType(Constants.EXPENDITURE);
		c = YearlyDiscrepancyWorker.getYearlyDiscrepancy(fp);
		if ( c.size() > 0 )	
			merge(fp,c);
		
		if ( arrList.size() != 0 )	{
			Collections.sort(arrList,new QuarterlyInfoComparator());
		}
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() returns collection of size : " + arrList.size() );
			
		return arrList;
	}
	
	public static void merge(FilterParams fp,Collection c)	{
		
		if ( c.size() > 0 )	{
			Iterator iter = c.iterator();
			boolean b = false;
			while ( iter.hasNext() )	{
				b = false;
				YearlyDiscrepancy discrepancy = (YearlyDiscrepancy)iter.next();
				
				for ( int i = 0 ; i < arrList.size() ; i++ )	{
					YearlyDiscrepancyAll discrepancyAll = (YearlyDiscrepancyAll)arrList.get(i);	
					if (  discrepancyAll.getFiscalYear()==discrepancy.getFiscalYear() )	{
							b = true;
							if ( fp.getTransactionType()== Constants.DISBURSEMENT )	{
								discrepancyAll.setDisbursementDonorActual(discrepancy.getDonorActual());
								discrepancyAll.setDisbursementImplAgencyActual(discrepancy.getImplAgencyActual());
								discrepancyAll.setDisbursementMofedActual(discrepancy.getMofedActual());
							}
							else if ( fp.getTransactionType()== Constants.EXPENDITURE)	{
								discrepancyAll.setExpenditureDonorActual(discrepancy.getDonorActual());
								discrepancyAll.setExpenditureImplAgencyActual(discrepancy.getImplAgencyActual());
								discrepancyAll.setExpenditureMofedActual(discrepancy.getMofedActual());
							}
							break;
					}
				}
				if ( !b )	{
					YearlyDiscrepancyAll discrepancyAll = new YearlyDiscrepancyAll();
					discrepancyAll.setFiscalYear(discrepancy.getFiscalYear());
					if ( fp.getTransactionType()== Constants.DISBURSEMENT )	{
						discrepancyAll.setDisbursementDonorActual(discrepancy.getDonorActual());
						discrepancyAll.setDisbursementImplAgencyActual(discrepancy.getImplAgencyActual());
						discrepancyAll.setDisbursementMofedActual(discrepancy.getMofedActual());
					}
					else if ( fp.getTransactionType()== Constants.EXPENDITURE)	{
						discrepancyAll.setExpenditureDonorActual(discrepancy.getDonorActual());
						discrepancyAll.setExpenditureImplAgencyActual(discrepancy.getImplAgencyActual());
						discrepancyAll.setExpenditureMofedActual(discrepancy.getMofedActual());
					}
					arrList.add(discrepancyAll);
				}	
			}
		}
	}
}	