package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

public class QuarterlyDiscrepancyWorker	{
	
	private static Logger logger = Logger.getLogger(QuarterlyDiscrepancyWorker.class);
	private static ArrayList arrList;
	
	public static Collection getDiscrepancy(FilterParams filterParams)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() with ampFundingId : " + filterParams.getAmpFundingId() );	
		//donor perspective
		FilterParams fp = new FilterParams();
		fp.copy(filterParams);
		fp.setPerspective("DN");
		Collection c = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		arrList = new ArrayList();
		Iterator iter = c.iterator();
		while ( iter.hasNext() )	{
			QuarterlyInfo qi = (QuarterlyInfo)iter.next();
			QuarterlyDiscrepancy discrepancy = new QuarterlyDiscrepancy();
			discrepancy.setFiscalYear(qi.getFiscalYear());
			discrepancy.setFiscalQuarter(qi.getFiscalQuarter());
			if ( qi.getAggregate() == 1 )
				discrepancy.setTransactionDate(qi.getDateDisbursed());
			discrepancy.setDonorPlanned(qi.getPlannedAmount());
			discrepancy.setDonorActual(qi.getActualAmount());
			discrepancy.setAggregate(qi.getAggregate());
			arrList.add(discrepancy);
		}
		//MOFED perspective
		fp.setPerspective("MA");
		c = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		if ( c.size() > 0 )
			merge(fp,c);
		fp.setPerspective("IA");
		c = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		if ( c.size() > 0 )
			merge(fp,c);
		
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() returns collection of size : " + arrList.size() );
		return arrList;
	}
	
	public static void merge(FilterParams fp, Collection c)	{
		if ( c.size() > 0 )	{
			Iterator iter = c.iterator();
			boolean b = false;
			while ( iter.hasNext() )	{
				b = false;
				QuarterlyInfo qi = (QuarterlyInfo)iter.next();
				
				for ( int i = 0 ; i < arrList.size() ; i++ )	{
					QuarterlyDiscrepancy disc = (QuarterlyDiscrepancy)arrList.get(i);	
					if ( qi.getAggregate() == 0 )	{
						if ( ( disc.getFiscalYear()==qi.getFiscalYear() )
							&& ( disc.getFiscalQuarter()==qi.getFiscalQuarter() ) )	{
								b = true;
								if ( fp.getPerspective() == "IA")	{
									disc.setImplAgencyPlanned(qi.getPlannedAmount());
									disc.setImplAgencyActual(qi.getActualAmount());
								}
								else if ( fp.getPerspective() == "MA" )	{
									disc.setMofedPlanned(qi.getPlannedAmount());
									disc.setMofedActual(qi.getActualAmount());
								}
								break;
						}
					}
					else if ( qi.getAggregate() == 1 )	{
						if ( disc.getFiscalYear()==qi.getFiscalYear() )	{
							String strDate1 = disc.getTransactionDate();
							String strDate2 = qi.getDateDisbursed();
							Date d1 = null;
							Date d2 = null;
							if ( strDate1 != null & strDate2 != null )	{
								d1 = DateConversion.getDate(strDate1);	
								d2 = DateConversion.getDate(strDate2);	
								if ( d1.equals(d2 ))	{
									b = true;
									if ( fp.getPerspective() == "IA")	{
										disc.setImplAgencyPlanned(qi.getPlannedAmount());
										disc.setImplAgencyActual(qi.getActualAmount());
									}
									else if ( fp.getPerspective() == "MA" )	{
										disc.setMofedPlanned(qi.getPlannedAmount());
										disc.setMofedActual(qi.getActualAmount());
									}
									break;					
								}
							}
						}
					}	
				}		
				if ( !b )	{
					QuarterlyDiscrepancy discrepancy = new QuarterlyDiscrepancy();
					discrepancy.setFiscalYear(qi.getFiscalYear());
					discrepancy.setFiscalQuarter(qi.getFiscalQuarter());
					discrepancy.setAggregate(qi.getAggregate());
					if ( fp.getPerspective() == "IA")	{
						discrepancy.setImplAgencyPlanned(qi.getPlannedAmount());
						discrepancy.setImplAgencyActual(qi.getActualAmount());
					}
					else if ( fp.getPerspective() == "MA" )	{
						discrepancy.setMofedPlanned(qi.getPlannedAmount());
						discrepancy.setMofedActual(qi.getActualAmount());
					}
					arrList.add(discrepancy);
				}
			}
		}
	}
}	