package org.digijava.module.aim.helper ;

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.GregorianCalendar;
import org.digijava.module.aim.helper.EthiopianCalendar;


public class EthDateWorker
{
	private static Logger logger = Logger.getLogger(FiscalCalendarWorker.class) ;
	
	public static String getEthDate(String strDate)
	{
		Date date = DateConversion.getDate(strDate) ;
		EthiopianCalendar ec = new EthiopianCalendar() ;
		EthiopianCalendar ec1 = null ;
		GregorianCalendar gc = new GregorianCalendar() ;
		gc.setTime(date) ;
		ec1 = ec.getEthiopianDate(gc) ;
		logger.debug("Ethiopian date returned day "+ec1.ethDay
									+ " month " + ec1.ethMonth
									+ " year " + ec1.ethYear ) ;
		String ethDate = ec1.ethDay+"/"+ec1.ethMonth+"/"+ec1.ethYear ;
						
		return ethDate;
	}
	
}
