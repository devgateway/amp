package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

@Deprecated
public class QuarterlyDiscrepancyWorker	{
	
	private static Logger logger = Logger.getLogger(QuarterlyDiscrepancyWorker.class);
	
	public static Collection getDiscrepancy(FilterParams filterParams)	{
		if ( logger.isDebugEnabled() )
			logger.debug("getDiscrepancy() with ampFundingId : " + filterParams.getAmpFundingId() );	

		return new ArrayList();
	}
	
	
}	