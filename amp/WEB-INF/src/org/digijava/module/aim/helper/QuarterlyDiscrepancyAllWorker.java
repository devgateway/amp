package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class QuarterlyDiscrepancyAllWorker	{
	
	private static Logger logger = Logger.getLogger(QuarterlyDiscrepancyAllWorker.class);
	private static ArrayList arrList;
	
	public static Collection getDiscrepancy(FilterParams filterParams)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() with ampFundingId : " + filterParams.getAmpFundingId() );	
		//donor perspective
		FilterParams fp = new FilterParams();
		fp.copy(filterParams);
		fp.setTransactionType(Constants.COMMITMENT);
		Collection c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);
		arrList = new ArrayList();
		Iterator iter = c.iterator();
		while ( iter.hasNext() )	{
			QuarterlyDiscrepancy discrepancy = (QuarterlyDiscrepancy)iter.next();
			if ( discrepancy.getAggregate() == 0 )	{
				QuarterlyDiscrepancyAll discrepancyAll = new QuarterlyDiscrepancyAll();
				discrepancyAll.setFiscalYear(discrepancy.getFiscalYear());
				discrepancyAll.setFiscalQuarter(discrepancy.getFiscalQuarter());
				discrepancyAll.setCommitmentDonorActual(discrepancy.getDonorActual());
				discrepancyAll.setCommitmentImplAgencyActual(discrepancy.getImplAgencyActual());
				discrepancyAll.setCommitmentMofedActual(discrepancy.getMofedActual());
				arrList.add(discrepancyAll);
			}
		}
		fp.setTransactionType(Constants.DISBURSEMENT);
		c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);
		if ( c.size() > 0 )	
			merge(fp,c);
			
		fp.setTransactionType(Constants.EXPENDITURE);
		c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);
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
				QuarterlyDiscrepancy discrepancy = (QuarterlyDiscrepancy)iter.next();
				
				for ( int i = 0 ; i < arrList.size() ; i++ )	{
					QuarterlyDiscrepancyAll discrepancyAll = (QuarterlyDiscrepancyAll)arrList.get(i);	
					if ( discrepancy.getAggregate() == 0 )	{
						if ( ( discrepancyAll.getFiscalYear()==discrepancy.getFiscalYear() )
							&& ( discrepancyAll.getFiscalQuarter()==discrepancy.getFiscalQuarter() ) )	{
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
				}		
				if ( !b )	{
					if ( discrepancy.getAggregate() == 0 )	{
						QuarterlyDiscrepancyAll discrepancyAll = new QuarterlyDiscrepancyAll();
						discrepancyAll.setFiscalYear(discrepancy.getFiscalYear());
						discrepancyAll.setFiscalQuarter(discrepancy.getFiscalQuarter());
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
}	