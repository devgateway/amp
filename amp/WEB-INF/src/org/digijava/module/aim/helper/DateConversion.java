package org.digijava.module.aim.helper ;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.onepager.models.MTEFYearsModel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;


public class DateConversion
{
    private static Logger logger = Logger.getLogger(DateConversion.class) ;

    public static Comparator dtComp = new Comparator() {
        public int compare(Object e1,Object e2) {
            if (e1 instanceof String &&
                    e2 instanceof String) {
                String tdt1 = (String) e1;
                String tdt2 = (String) e2;
                Date dt1=null;
                Date dt2=null;
                if (tdt1 == null || tdt1.trim().length() < 1) {
                    return -1;
                } else if (tdt2 == null || tdt2.trim().length() < 1) {
                    return 1;
                }
                try{
                     dt1 = DateConversion.getLocalizedDate(tdt1);
                     dt2 = DateConversion.getLocalizedDate(tdt2);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                return dt2.compareTo(dt1);
            } else throw new ClassCastException();
        }
    };

    public static String convertDateToFiscalYearString(Date inputDate ) {
        String textDate ="";
        if (inputDate != null) {
        textDate = MTEFYearsModel.convert(inputDate, AmpARFilter.getDefaultCalendar().getIsFiscal()).value;
        }
        return textDate;
    }
    
    
    public static String convertDateToString(Date date) {
        return date == null ? "" : DateTimeUtil.formatDate(date);
    }
    
    public static String convertDateToLocalizedString(Date date) {
        return date == null ? "" : DateTimeUtil.formatDateLocalized(date);
    }
    
    public static Date getDate(String strDate, String format)
    {
            SimpleDateFormat formater=new SimpleDateFormat(format);
            try {
                return formater.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
    }   

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    /**
     * parses a localized date following the format in GlobalSettings
     * @param strDate
     * @return
     */
    public static Date getLocalizedDate(String strDate) {
        try {
            if (isEmpty(strDate))
                return null;
            else {
                LocalDate ld = AmpDateFormatterFactory.getLocalizedFormatter().parseDate(strDate);
                return java.sql.Date.valueOf(ld);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    /**
     * parses a date following the format in GlobalSettings
     * @param strDate
     * @return
     */
    public static Date getDate(String strDate) {
        try {
            return isEmpty(strDate) ? null : FormatHelper.parseDate(strDate).getTime();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static Date getDateForIndicator(String strDate){
        if (strDate == null)
            return null;
        try{
            String pattern=FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
            if (pattern == null || pattern.equals("")) {
                pattern = "MMM/dd/yyyy";
            }
            pattern = pattern.replaceAll("m", "M");
            Date date = new SimpleDateFormat(pattern).parse(strDate);
            return date;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**@author jose
     * This method given a String like dd/mm/yyyy it will parse the year out of it and
     * return the year
     * @param String dd/mm/yyyy or d/mm/yyyy or dd/m/yyyy or d/m/yyyy
     * @return int year
     */
    public static int getYear(String s)
    {
        //if ( logger.isDebugEnabled() )
            //logger.debug("getYear passed String" + s ) ;
        int yr = 0 ;
        if ( s != null && s.length() != 0 )     {
            String strYr = "" ;
            char[] arr = s.toCharArray() ;
            int i = s.length() - 1 ;
            while( arr[i] != '/' )
            {
                strYr += arr[i] ;
                i-- ;
            }
            StringBuffer sb = new StringBuffer(strYr) ;
            sb.reverse() ;
            yr= Integer.parseInt(sb.toString()) ;
        }
        //if ( logger.isDebugEnabled() )
            //logger.debug("getYear returning year="+yr) ;
        return yr ;
    }
    
    public static String getFormattedPeriod(Date from, Date to) {
        return getFormattedPeriod(getPeriod(from, to));
    }
    
    public static Period getPeriod(Date from, Date to) {
        if (from == null || to == null)
            return null;
        
        LocalDateTime fromLDT = LocalDateTime.fromDateFields(from);
        LocalDateTime toLDT = LocalDateTime.fromDateFields(to);
        return new Period(fromLDT, toLDT, PeriodType.yearMonthDayTime());
    }
    
    public static String getFormattedPeriod(Period period) {
        if (period == null)
            return null;
        PeriodFormatter formatter = getPeriodFormatter();
        return formatter.print(period);
    }
    
    public static PeriodFormatter getPeriodFormatter() {
        String yearTrn = " " + TranslatorWorker.translateText("year");
        String yearsTrn = " " + TranslatorWorker.translateText("years");
        String monthTrn = " " + TranslatorWorker.translateText("month");
        String monthsTrn = " " + TranslatorWorker.translateText("months");
        String dayTrn = " " + TranslatorWorker.translateText("day");
        String daysTrn = " " + TranslatorWorker.translateText("days");
        return new PeriodFormatterBuilder()
                .printZeroRarelyFirst().appendYears().appendSuffix(yearTrn, yearsTrn).appendSeparator(" ")
                .printZeroRarelyFirst().appendMonths().appendSuffix(monthTrn, monthsTrn).appendSeparator(" ")
                .printZeroAlways().appendDays().appendSuffix(dayTrn, daysTrn).toFormatter();
    }

}
