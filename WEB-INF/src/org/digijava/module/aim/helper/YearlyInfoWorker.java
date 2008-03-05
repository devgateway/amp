package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class YearlyInfoWorker {

	private static Logger logger = Logger.getLogger(YearlyInfoWorker.class);

	public static Collection<YearlyInfo> getYearlyInfo(FilterParams fp) {

		Collection<YearlyInfo> yo = new ArrayList<YearlyInfo>();
		Collection<QuarterlyInfo> qo = QuarterlyInfoWorker.getQuarterlyInfo(fp);
		if ( qo.size() != 0 )
			yo = getYearlySum(qo);

		if ( logger.isDebugEnabled() )
			logger.debug("GETYEARLYINFO() RETURNING A COLLECTION OF SIZE : " + yo.size());

		return yo;
	}

	public static Collection<YearlyInfo> getYearlySum(Collection<QuarterlyInfo> qi) {

		logger.debug("GETYEARLYSUM() PASSED A COLLECTION OF SIZE : " + qi.size());

		ArrayList<YearlyInfo> yo = new ArrayList<YearlyInfo>();
		if ( qi.size() != 0 )	{

			ArrayList<QuarterlyInfo> qo = new ArrayList<QuarterlyInfo>(qi);

			double tempPlanned = 0.0;
			double tempActual = 0.0;
			String tmpActualWraped="";
			String tmpPlanedWraped="";
			QuarterlyInfo qf = null;
			YearlyInfo yf = null;
			//String pa = "";
			//String aa = "";

			int i = 0;
			qf =  qo.get(i);

			while ( i < qo.size() ) {

				yf = new YearlyInfo();
				yf.setFiscalYear(qf.getFiscalYear());
				if ( qf.getAggregate() == 0 )	{
						tempPlanned += qf.getPlannedAmount();
						tempActual += qf.getActualAmount();
						tmpActualWraped = qf.getWrapedActual();
						tmpPlanedWraped = qf.getWrapedPlanned();
				}
				i++;
				if ( i < qo.size() )	{
					qf = (QuarterlyInfo) qo.get(i);
				}
				else {
					yf.setPlannedAmount( tempPlanned );
					yf.setActualAmount( tempActual );
					yf.setWrapedActual(tmpActualWraped);
					yf.setWrapedPlanned(tmpPlanedWraped);
					yo.add(yf);
					break;
				}

				while (qf.getFiscalYear()==yf.getFiscalYear()  ) {
					if ( qf.getAggregate()==0)	{
						tempPlanned += qf.getPlannedAmount();
						tempActual += qf.getActualAmount();
						if (qf.getWrapedActual() != null && tmpActualWraped != null) {
							tmpActualWraped = tmpActualWraped + " + " + qf.getWrapedActual();
						} else if (qf.getWrapedActual()!=null) {
							tmpActualWraped = qf.getWrapedActual();
						}
						if (qf.getWrapedPlanned() != null && tmpPlanedWraped != null) {
							tmpPlanedWraped = tmpPlanedWraped + " + " + qf.getWrapedPlanned();
						} else if (qf.getWrapedPlanned()!=null) {
							tmpPlanedWraped = qf.getWrapedPlanned();
						}
					}
					i++;
					if ( i < qo.size() )
						qf = (QuarterlyInfo) qo.get(i);
					else
						break;
				}
				yf.setPlannedAmount( tempPlanned );
				yf.setActualAmount( tempActual );
				yf.setWrapedActual(tmpActualWraped);
				yf.setWrapedPlanned(tmpPlanedWraped);
				yo.add(yf);
				tempPlanned = 0d;
				tempActual = 0d;
			}
		}
		logger.debug("GETYEARLYSUM() RETURNING A COLLECTION OF SIZE : " + yo.size());
		return yo;
	}

	public static String getTotalYearly(Collection<YearlyInfo> yearlyInfo, int type)	{
		logger.debug("getTotalYearly() passed a collection of size" + yearlyInfo.size() );
		Iterator<YearlyInfo> iter = yearlyInfo.iterator() ;
		YearlyInfo y = null ;
		double total = 0.0 ;
		String s1 = "" ;
		
		while ( iter.hasNext() )	{
			y =  iter.next();
			if ( type == Constants.PLANNED )	{
				total += y.getPlannedAmount();
			}
			else if ( type == Constants.ACTUAL )	{
				total += y.getActualAmount();
			}
		}
		String strTotal = FormatHelper.formatNumber(total);
		logger.debug("getTotalYearly() returns total=" + strTotal );
		return strTotal;
	}

}
