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
			QuarterlyInfo qf = null;
			YearlyInfo yf = null;
			String pa = "";
			String aa = "";

			int i = 0;
			qf =  qo.get(i);

			while ( i < qo.size() ) {

				yf = new YearlyInfo();
				yf.setFiscalYear(qf.getFiscalYear());
				if ( qf.getAggregate() == 0 )	{
					if (qf.getPlannedAmount() != null &&  (!qf.getPlannedAmount().equals("0"))) {
						pa = DecimalToText.removeCommas(qf.getPlannedAmount());
						tempPlanned += Double.parseDouble(pa);
					}
					if (qf.getActualAmount() != null && (!qf.getActualAmount().equals("0"))) {
						aa = DecimalToText.removeCommas(qf.getActualAmount());
                        //if(StringUtils.isNumeric(aa)){
                            tempActual += Double.parseDouble(aa);
                        //}
					}
				}
				i++;
				if ( i < qo.size() )	{
					qf = (QuarterlyInfo) qo.get(i);
				}
				else {
					yf.setPlannedAmount( DecimalToText.mf.format(tempPlanned) );
					yf.setActualAmount( DecimalToText.mf.format(tempActual) );
					yo.add(yf);
					break;
				}

				while (qf.getFiscalYear()==yf.getFiscalYear()  ) {
					if ( qf.getAggregate()==0)	{
						if (qf.getPlannedAmount() != null &&  (!qf.getPlannedAmount().equals("0"))) {
							pa = DecimalToText.removeCommas(qf.getPlannedAmount());
							tempPlanned += Double.parseDouble(pa);
						}

						if (qf.getActualAmount() != null && (!qf.getActualAmount().equals("0"))) {
							aa = DecimalToText.removeCommas(qf.getActualAmount());
							tempActual += Double.parseDouble(aa);
						}
					}
					i++;
					if ( i < qo.size() )
						qf = (QuarterlyInfo) qo.get(i);
					else
						break;
				}
				yf.setPlannedAmount( DecimalToText.mf.format(tempPlanned) );
				yf.setActualAmount( DecimalToText.mf.format(tempActual) );
				yo.add(yf);
				tempPlanned = 0.0;
				tempActual = 0.0;
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
		String s2 = "" ;

		while ( iter.hasNext() )	{
			y =  iter.next();
			if ( type == Constants.PLANNED )	{
				s1 = y.getPlannedAmount();
				s2 = DecimalToText.removeCommas(s1);
				total += Double.parseDouble(s2);
			}
			else if ( type == Constants.ACTUAL )	{
				s1 = y.getActualAmount();
				s2 = DecimalToText.removeCommas(s1);
				total += Double.parseDouble(s2);
			}
		}
		String strTotal = CurrencyWorker.mf.format(total);
		logger.debug("getTotalYearly() returns total=" + strTotal );
		return strTotal;
	}

}
