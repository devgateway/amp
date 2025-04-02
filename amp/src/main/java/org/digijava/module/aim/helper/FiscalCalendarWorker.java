package org.digijava.module.aim.helper ;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.FiscalCalendarUtil;
@Deprecated
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
    public static FiscalDO getFiscalYrQtr(Date d, Long fiscalCalId)     {
        FiscalDO fdo  = null ;
        AmpFiscalCalendar fc = null;
        
        fc = FiscalCalendarUtil.getAmpFiscalCalendar(fiscalCalId);
        if ( d != null )    {
                
            EthiopianCalendar ec = new EthiopianCalendar() ;
            EthiopianCalendar ec1 = null ;
            
            fdo  = new FiscalDO() ;
            GregorianCalendar gc = new GregorianCalendar() ;
            gc.setTime(d) ;
        
             if ((fc.getBaseCal() != null) && ( fc.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))) {
                ec1 = ec.getEthiopianDate(gc);
                fdo.setFiscalYear(ec1.ethYear);
                fdo.setFiscalQuarter(ec1.ethQtr);               
                
            }
            else
            {
                if ( fc.getStartDayNum().intValue()!= -1 && 
                        fc.getStartMonthNum().intValue() != -1 )    {
                    //Zero index month
                    int fiscalYr = 0;
                    
                    int year = gc.get(Calendar.YEAR);
                    //
                    DecimalFormat dfDay=new DecimalFormat("00");
                    String stDay = dfDay.format(fc.getStartDayNum().intValue());
                    DecimalFormat dfMonth=new DecimalFormat("00");
                    String  stMnt = dfMonth.format(fc.getStartMonthNum().intValue());
                    String bsDate = stMnt + "/" +  stDay + "/" + year;
                    Date baseDate = DateConversion.getDate(bsDate,"MM/dd/yyyy"); 

                    if (d.after(baseDate) || d.equals(baseDate)) {
                        fiscalYr = year;    
                    } else {
                        fiscalYr = year-1;
                    }
                    int fiscalQtr = 0;
                    bsDate = stMnt + "/" +  stDay + "/" + year;
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
                
                    fdo.setFiscalYear(fiscalYr) ;
                    
                }
                else    {
                    fdo.setFiscalYear(-1) ;
                    fdo.setFiscalQuarter(-1) ;
                }
            }
        }
        else    {
            fdo  = new FiscalDO();
            fdo.setFiscalYear(0);
            fdo.setFiscalQuarter(0);
        }
        return fdo ;
    }
}
