package org.digijava.module.aim.helper ;

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.digijava.module.aim.helper.FiscalCalendar;
import org.digijava.module.aim.helper.FiscalDO;
import org.digijava.module.aim.helper.EthiopianCalendar;
import org.digijava.module.aim.util.DbUtil;

public class FiscalCalendarWorker
{
	private static Logger logger = Logger.getLogger(FiscalCalendarWorker.class) ;
	
	/**@author jose 
	 * This method will return fiscal year and quarter this date 
	 * belongs to in the given fiscal calendar specified by fiscalCalId
	 * @param d The transaction date 
	 * @param fiscalCalId The fiscal calendar
	 * @return FiscalDO object containing fiscal year and quarter
	 */
	public static FiscalDO getFiscalYrQtr(Date d, Long fiscalCalId)
	{
		if ( logger.isDebugEnabled() )
			logger.debug("getFiscalYrQtr() with date = "+d+", fiscalCalId = "+fiscalCalId) ;
		int fiscalYear = 0 ;
		int qtr = 0 ;
		FiscalDO fdo  = null ;
		FiscalCalendar fc = null ;
		
		if ( d != null )	{
			//If not Ethiopian calendar
			if ( fiscalCalId.longValue() != Constants.ETH_FY.longValue())
				fc = DbUtil.getFiscalCalendar(fiscalCalId) ;
			
			EthiopianCalendar ec = new EthiopianCalendar() ;
			EthiopianCalendar ec1 = null ;
			
			fdo  = new FiscalDO() ;
			GregorianCalendar gc = new GregorianCalendar() ;
			gc.setTime(d) ;
			//target date
			//Ethiopian calendar
			if ( fiscalCalId.longValue() == Constants.ETH_FY.longValue() )
			{
				logger.info("Setting year and quarter for Ethiopian Fiscal Year");				
				ec1 = ec.getEthiopianDate(gc) ;
				fdo.setFiscalYear(ec1.ethFiscalYear) ;
				fdo.setFiscalQuarter(ec1.ethFiscalQrt) ;
				logger.info("Year :" + fdo.getFiscalYear() + ",Qtr : " + fdo.getFiscalQuarter());
			} else if (fiscalCalId.longValue() == Constants.ETH_CAL.longValue()) {
				logger.info("Setting year and quarter for Ethiopian Calendar");
				ec1 = ec.getEthiopianDate(gc) ;
				fdo.setFiscalYear(ec1.ethYear) ;
				fdo.setFiscalQuarter(ec1.ethQtr) ;				
				logger.info("Year :" + fdo.getFiscalYear() + ",Qtr : " + fdo.getFiscalQuarter());
			}
			else
			{
				if ( fc.getStartDay() != -1 && fc.getStartMonth() != -1 )	{
					//Zero index month
					  int month = gc.get(Calendar.MONTH) ;
					  if ( month < (fc.startMonth-1) )
					  {
						  fiscalYear = gc.get(Calendar.YEAR) - 1 ;
					  }
					  else
					  {
						  fiscalYear = gc.get(Calendar.YEAR) ;
					  }
					  //Fiscal Calendar start date say Indian fiscal year Apr 1,2004
					  GregorianCalendar gc1 = new GregorianCalendar(fiscalYear,(fc.startMonth-1),fc.startDay) ;
					  GregorianCalendar gc2 = (GregorianCalendar)gc1.clone() ;
					  int i = 1 ;
					  while ( i <= 4)
					  {
						  gc2.add(Calendar.DATE,-1) ;
						  gc1.add(Calendar.MONTH,3) ;
						  if ( gc.after(gc2) && gc.before(gc1) )
						  {
							  qtr = i ;
							  break ;
						  }
						  gc2 = (GregorianCalendar)gc1.clone() ;
						  i++ ;
					  }
					  fdo.setFiscalYear(fiscalYear) ;
					  fdo.setFiscalQuarter(qtr) ;
				}
				else	{
					fdo.setFiscalYear(-1) ;
					fdo.setFiscalQuarter(-1) ;
				}
			}
		}
		else	{
			fdo  = new FiscalDO();
			fdo.setFiscalYear(0);
			fdo.setFiscalQuarter(0);
		}
		if ( logger.isDebugEnabled() )
			logger.debug("Fiscal year = "+fdo.getFiscalYear()+"  quarter= "+fdo.getFiscalQuarter()) ;
		return fdo ;
	}
}