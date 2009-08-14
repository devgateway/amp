package org.digijava.module.aim.helper ;

import org.apache.log4j.Logger;

public class QuarterlyInfoComparator implements java.util.Comparator
{
	private static Logger logger = Logger.getLogger(QuarterlyInfoComparator.class) ;

	public int compare(Object o1, Object o2)	
	{
		if ( logger.isDebugEnabled() )
			logger.debug("COMPARE() < ");
		int result = 0 ;
		if ( o1 instanceof QuarterlyInfo && o2 instanceof QuarterlyInfo )
		{
			QuarterlyInfo q1 = (QuarterlyInfo) o1 ;
			QuarterlyInfo q2 = (QuarterlyInfo) o2 ;
			result = comparisonResult(q1.getFiscalYear(),q2.getFiscalYear(),
			q1.getFiscalQuarter(),q2.getFiscalQuarter(),q1.getAggregate(),q2.getAggregate()) ;
		}
		else if ( o1 instanceof QuarterlyComparison && o2 instanceof QuarterlyComparison )
		{
			QuarterlyComparison q1 = (QuarterlyComparison) o1 ;
			QuarterlyComparison q2 = (QuarterlyComparison) o2 ;
			result = comparisonResult(q1.getFiscalYear(),q2.getFiscalYear(),
						q1.getFiscalQuarter(),q2.getFiscalQuarter()) ;
		}
		else if ( o1 instanceof QuarterlyDiscrepancyAll && o2 instanceof QuarterlyDiscrepancyAll )
		{
			QuarterlyDiscrepancyAll q1 = (QuarterlyDiscrepancyAll) o1 ;
			QuarterlyDiscrepancyAll q2 = (QuarterlyDiscrepancyAll) o2 ;
			result = comparisonResult(q1.getFiscalYear(),q2.getFiscalYear(),
						q1.getFiscalQuarter(),q2.getFiscalQuarter()) ;
		} 
		else if ( o1 instanceof YearlyDiscrepancyAll && o2 instanceof YearlyDiscrepancyAll )
		{
			YearlyDiscrepancyAll q1 = (YearlyDiscrepancyAll) o1 ;
			YearlyDiscrepancyAll q2 = (YearlyDiscrepancyAll) o2 ;
			result = comparisonResult(q1.getFiscalYear(),q2.getFiscalYear(),
						0,0) ;
		} 
		if ( logger.isDebugEnabled() )
			logger.debug("COMPARE() RETURNING RESULT : " + result + " >");
		return result ;
	}
	
	public int comparisonResult(int fy1,int fy2, int fq1,int fq2,int aggr1, int aggr2)
	{
		int result = 0 ;
		if ( fy1 < fy2 )
		{
			result = -1 ;
		}
		else if ( fy1 == fy2 )
		{
			if ( fq1 < fq2 )
			{
				result = -1 ;
			}
			else if (fq1 == fq2)
			{
				if ( aggr1 < aggr2 )
					result = -1;
				else if ( aggr1 > aggr2 )
					result = 1;
			}
			else if ( fq1 > fq2 )
			{
				result = 1 ;
			}
		}
		else if ( fy1 > fy2  )
		{
			result = 1 ;
		}
		return result ;
	}
	
	public int comparisonResult(int fy1,int fy2, int fq1,int fq2)
	{
		
		int result = 0 ;
		if ( fy1 < fy2 )
		{
			result = -1 ;
		}
		else if ( fy1 == fy2 )
		{
			if ( fq1 < fq2 )
			{
				result = -1 ;
			}
			else if (fq1 == fq2)
			{
				result = 0 ;
			}
			else if ( fq1 > fq2 )
			{
				result = 1 ;
			}
		}
		else if ( fy1 > fy2  )
		{
			result = 1 ;
		}
		return result ;
	}
}