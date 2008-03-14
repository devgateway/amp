package org.digijava.module.aim.helper ;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;

public class FiscalCalendarWorker
{
	//private static Logger logger = Logger.getLogger(FiscalCalendarWorker.class) ;
	
	/**@author jose 
	 * This method will return fiscal year and quarter this date 
	 * belongs to in the given fiscal calendar specified by fiscalCalId
	 * @param d The transaction date 
	 * @param fiscalCalId The fiscal calendar
	 * @return FiscalDO object containing fiscal year and quarter
	 */
	public static FiscalDO getFiscalYrQtr(Date d, Long fiscalCalId) 	{
		FiscalDO fdo  = null ;
		AmpFiscalCalendar fc = null;
		
		//logger.info("fiscal cal id = " + fiscalCalId);
		//AMP-2212
		fc = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalId);
		if ( d != null )	{
			//If not Ethiopian calendar
			//if ( !fCalendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN_FISCAl.getValue()))
		    	//	fc = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalId);
			
			
			EthiopianCalendar ec = new EthiopianCalendar() ;
			EthiopianCalendar ec1 = null ;
			
			fdo  = new FiscalDO() ;
			GregorianCalendar gc = new GregorianCalendar() ;
			gc.setTime(d) ;
			//target date
			//Ethiopian calendar //AMP-2212
			if ( fc.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN_FISCAl.getValue()))
			{
				//logger.info("Setting year and quarter for Ethiopian Fiscal Year");				
				ec1 = ec.getEthiopianDate(gc) ;
				fdo.setFiscalYear(ec1.ethFiscalYear) ;
				fdo.setFiscalQuarter(ec1.ethFiscalQrt) ;
				//logger.info("Year :" + fdo.getFiscalYear() + ",Qtr : " + fdo.getFiscalQuarter());
			} else if (fc.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue())) {
				//logger.info("Setting year and quarter for Ethiopian Calendar");
				ec1 = ec.getEthiopianDate(gc) ;
				fdo.setFiscalYear(ec1.ethYear) ;
				fdo.setFiscalQuarter(ec1.ethQtr) ;				
				//logger.info("Year :" + fdo.getFiscalYear() + ",Qtr : " + fdo.getFiscalQuarter());
			}
			else
			{
				if ( fc.getStartDayNum().intValue()!= -1 && 
						fc.getStartMonthNum().intValue() != -1 )	{
					//Zero index month
					int fiscalYr = 0;
					
					int year = gc.get(Calendar.YEAR);
					//
					DecimalFormat dfDay=new DecimalFormat("00");
					String stDay = dfDay.format(fc.getStartDayNum().intValue());
					DecimalFormat dfMonth=new DecimalFormat("00");
					String  stMnt = dfMonth.format(fc.getStartMonthNum().intValue());
					//
					//logger.info("Stday = " + stDay + ", stMnt = " + stMnt);
					String bsDate = stMnt + "/" +  stDay + "/" + year;
					Date baseDate = DateConversion.getDate(bsDate,"MM/dd/yyyy"); 

					if (d.after(baseDate) || d.equals(baseDate)) {
						fiscalYr = year;	
					} else {
						fiscalYr = year-1;
					}
					//logger.info("Fiscal Yr = " + fiscalYr);
					int fiscalQtr = 0;
					bsDate = stMnt + "/" +  stDay + "/" + year;
					///logger.info("Base date got = " + bsDate);
					baseDate = DateConversion.getDate(bsDate,"MM/dd/yyyy"); 
					GregorianCalendar gc1 = new GregorianCalendar();
					gc1.setTime(baseDate);
					boolean found = false;
					
					while (found == false && fiscalQtr < 4) {
						if (d.after(gc1.getTime()) || d.equals(gc1.getTime())) {
							fiscalQtr ++;
						} else {
							found = true;
							break;							
						}
						if (!found) {
							int prevMnth = gc1.get(Calendar.MONTH); 
							int month = (prevMnth + 3) % 12; 
							gc1.set(Calendar.MONTH,month);			
							if (month < prevMnth) {
								gc1.set(Calendar.YEAR,(gc1.get(Calendar.YEAR) + 1));
							}
						}
					}
					//logger.info("Setting fiscal year as " + fiscalYr);
					fdo.setFiscalYear(fiscalYr) ;
					//logger.info("Setting fiscal qtr as " + fiscalQtr);
					fdo.setFiscalQuarter(fiscalQtr) ;					
					
					
					/*
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
				*/
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
		return fdo ;
	}
}