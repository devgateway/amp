package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class YearlyDiscrepancyWorker	{
	
	private static Logger logger = Logger.getLogger(YearlyDiscrepancyWorker.class);
	
	public static Collection getYearlyDiscrepancy(FilterParams filterParams)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getYearlyDiscrepancy() with ampFundingId : " 
			+ filterParams.getAmpFundingId() );	
			
		FilterParams fp = new FilterParams();
		fp.copy(filterParams);	
		Collection c = QuarterlyDiscrepancyWorker.getDiscrepancy(fp);	
		Collection yearlyDiscrepancies = getYearlySum(c);
		
		if ( logger.isDebugEnabled() )
			logger.debug("getYearlyDiscrepancy() returning a collection of size : " + yearlyDiscrepancies.size() );	
		
		return yearlyDiscrepancies;
	}
	
	public static ArrayList getYearlySum(Collection c)	{
			
		if ( logger.isDebugEnabled() )
			logger.debug("getYearlySum() passed a collection of size : " + c.size() );	
				
		ArrayList yearlyDiscrepancies = new ArrayList();
		ArrayList quarterlyDiscrepancies = new ArrayList(c);
		Iterator iter = quarterlyDiscrepancies.iterator();
		int fiscalYr = 0;
		double donorPlanned = 0.0;
		double implAgencyPlanned = 0.0;
		double mofedPlanned = 0.0 ;
		double donorActual = 0.0 ;
		double implAgencyActual = 0.0;
		double mofedActual = 0.0;
		int index = 0;
		while ( index < quarterlyDiscrepancies.size() )	{
			QuarterlyDiscrepancy quarterlyDiscrepancy = (QuarterlyDiscrepancy)quarterlyDiscrepancies.get(index);
			if ( quarterlyDiscrepancy.getAggregate() == 0 )	{
				fiscalYr = quarterlyDiscrepancy.getFiscalYear();
				donorPlanned = DecimalToText.getDouble(quarterlyDiscrepancy.getDonorPlanned());
				implAgencyPlanned = DecimalToText.getDouble(quarterlyDiscrepancy.getImplAgencyPlanned());
				mofedPlanned = DecimalToText.getDouble(quarterlyDiscrepancy.getMofedPlanned());
				donorActual = DecimalToText.getDouble(quarterlyDiscrepancy.getDonorActual());
				implAgencyActual = DecimalToText.getDouble(quarterlyDiscrepancy.getImplAgencyActual());
				mofedActual = DecimalToText.getDouble(quarterlyDiscrepancy.getMofedActual());
			}
			index++;
			if ( index < quarterlyDiscrepancies.size() )	{
				quarterlyDiscrepancy = (QuarterlyDiscrepancy)quarterlyDiscrepancies.get(index);
			}
				
			while ( index < quarterlyDiscrepancies.size() &&  
			   quarterlyDiscrepancy.getFiscalYear() == fiscalYr )	{
			   	if ( quarterlyDiscrepancy.getAggregate() == 0 ){
					donorPlanned += DecimalToText.getDouble(quarterlyDiscrepancy.getDonorPlanned());
					implAgencyPlanned += DecimalToText.getDouble(quarterlyDiscrepancy.getImplAgencyPlanned());
					mofedPlanned += DecimalToText.getDouble(quarterlyDiscrepancy.getMofedPlanned());
					donorActual += DecimalToText.getDouble(quarterlyDiscrepancy.getDonorActual());
					implAgencyActual += DecimalToText.getDouble(quarterlyDiscrepancy.getImplAgencyActual());
					mofedActual += DecimalToText.getDouble(quarterlyDiscrepancy.getMofedActual());
			   	}
				index++;
				if ( index < quarterlyDiscrepancies.size() )	{
					quarterlyDiscrepancy = (QuarterlyDiscrepancy)quarterlyDiscrepancies.get(index);
				}
			}
			YearlyDiscrepancy yearlyDiscrepancy = new YearlyDiscrepancy();
			yearlyDiscrepancy.setFiscalYear(fiscalYr);
			yearlyDiscrepancy.setDonorPlanned(DecimalToText.ConvertDecimalToText(donorPlanned));
			yearlyDiscrepancy.setImplAgencyPlanned(DecimalToText.ConvertDecimalToText(implAgencyPlanned));
			yearlyDiscrepancy.setMofedPlanned(DecimalToText.ConvertDecimalToText(mofedPlanned));
			yearlyDiscrepancy.setDonorActual(DecimalToText.ConvertDecimalToText(donorActual));
			yearlyDiscrepancy.setImplAgencyActual(DecimalToText.ConvertDecimalToText(implAgencyActual));
			yearlyDiscrepancy.setMofedActual(DecimalToText.ConvertDecimalToText(mofedActual));
			yearlyDiscrepancies.add(yearlyDiscrepancy);
		}
		if ( logger.isDebugEnabled() )
					logger.debug("getYearlySum() returning an array list of size : " + yearlyDiscrepancies.size() );	
						
		return yearlyDiscrepancies;
	}
}	