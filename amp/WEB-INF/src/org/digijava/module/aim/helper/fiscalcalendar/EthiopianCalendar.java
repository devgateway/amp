package org.digijava.module.aim.helper.fiscalcalendar ;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EthiopianCalendar 
{
    public int ethMonth ; 
    public int ethDay ; 
    public int ethYear ; 
    public int ethQtr;
    public int ethFiscalYear = 0 ;
    public int ethFiscalQrt = 0;
    public String ethMonthName="";
    
    public static Logger logger = Logger.getLogger(EthiopianCalendar.class) ;
    /**
     * This method takes as input a date object containing the date 
     * which you want to convert to Ethiopian Calendar and it returns a 
     * EthiopianCalendar object containing 
     * @param date
     * @return
     */
    static public EthiopianCalendar getEthiopianDate(Date date){
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(date);
        return getEthiopianDate(gc);
        
    }
    
    /**@author ronald
     * This method takes as input a GregorianCalendar object containing the date 
     * which you want to convert to Ethiopian Calendar and it returns a 
     * EthiopianCalendar object containing 
     * @param GregorianCalendar object
     * @return EthiopianCalendar
     */
    static public EthiopianCalendar getEthiopianDate(GregorianCalendar gc)
    {
        //if ( logger.isDebugEnabled() )
        //logger.debug("GregorianCalendar passed in day " + gc.get(Calendar.DAY_OF_MONTH) +
        //           " month " + gc.get(Calendar.MONTH) ) ;
        EthiopianCalendar  obj = new EthiopianCalendar();   
        
        String endingEthYrStart = "11-Sep";
        String startingEthYrStart = "11-Sep";
        boolean endingEthYrLeap = false ;
        boolean startingEthYrLeap = false;
        boolean dateInEndingEthYr = false ;
        boolean dateInStartingEthYr = false;
        int  gregYr , ethDayOfYear;
        String ethStart="";
            
        gregYr = gc.get(Calendar.YEAR); 
        int ty = gc.get(Calendar.YEAR);
        if( (gc.isLeapYear(ty + 1)) == true )
        {
            endingEthYrLeap = true;
            startingEthYrStart = "12-Sept";
        }
        else if( (gc.isLeapYear(ty + 2)) == true )
        {   
            startingEthYrLeap = true;
        }
        else if( (gc.isLeapYear(ty)) == true )
        {   
            endingEthYrStart = "12-Sept";
        }
        GregorianCalendar startDate= new GregorianCalendar();
        startDate = obj.Date(startingEthYrStart, gregYr);
        GregorianCalendar g = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE));
        GregorianCalendar g1 = new GregorianCalendar(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
    
        Date startDateEth = g.getTime();
        Date gregDate = g1.getTime();
        boolean ethLeap=false;
        EthiopianCalendar ecal = new EthiopianCalendar();
        int a = gregDate.compareTo(startDateEth);
        
        if((gregDate.compareTo(startDateEth)) < 0)
        {
                ecal.ethYear = gregYr - 8;
                dateInEndingEthYr = true;
        }
        else
        {
            ecal.ethYear = gregYr - 7;
            dateInStartingEthYr = true;
        }
        if(dateInEndingEthYr == true)
        {
            ethStart = endingEthYrStart;
            ethLeap = endingEthYrLeap;
            gregYr = gregYr - 1;
        }
        
        if(dateInStartingEthYr == true)
        {
            ethStart = startingEthYrStart;
            ethLeap = startingEthYrLeap;
        }
        
        GregorianCalendar tte;
        tte = obj.Date(ethStart, gregYr);
        Calendar ethStartDate;
        ethStartDate = (Calendar)tte;

        Calendar ethCal = Calendar.getInstance();
        Calendar gregCal = Calendar.getInstance();
          
        int ey, em, ed, gy, gm,gd;
        ey = ethStartDate.get(Calendar.YEAR);
        em = ethStartDate.get(Calendar.MONTH);
        ed= ethStartDate.get(Calendar.DATE);
         
        gy= gc.get(Calendar.YEAR);
        gm= gc.get(Calendar.MONTH);
        gd= gc.get(Calendar.DATE);
        
        ethCal.set(ey, em, ed);
        gregCal.set(gy,gm,gd );
        
        long dif = ((gregCal.getTime().getTime() - ethCal.getTime().getTime())/(1000*60*60*24));
        int difInDays = (int)((gregCal.getTime().getTime() - ethCal.getTime().getTime())/(1000*60*60*24));
        int  q, r;
        ethDayOfYear =  difInDays + 1;  
        q = (ethDayOfYear)/30;
        r = (ethDayOfYear)%30;
        if(r!=0)
        {
            ecal.ethMonth  = q + 1;
            ecal.ethDay = r;
        }
        else
        {
            ecal.ethMonth = q;
            ecal.ethDay =30;
        }
        if(ethDayOfYear >=0 && ethDayOfYear <=30 )
        {
            ecal.ethFiscalQrt = 1;
            ecal.ethFiscalYear = ecal.ethYear;
        }
        else if(ethDayOfYear >=31 && ethDayOfYear <=120 )
        {
            ecal.ethFiscalQrt = 2;
            ecal.ethFiscalYear = ecal.ethYear;
        }
        else if(ethDayOfYear >=121 && ethDayOfYear <=210 )
        {
            ecal.ethFiscalQrt = 3;
            ecal.ethFiscalYear = ecal.ethYear;
        }
        else if(ethDayOfYear >=211 && ethDayOfYear <=300 )
        {
            ecal.ethFiscalQrt = 4;
            ecal.ethFiscalYear = ecal.ethYear;
        }
        else if(ethDayOfYear >= 301 && ethDayOfYear <=366)
        {
            ecal.ethFiscalYear = ecal.ethYear + 1;
            ecal.ethFiscalQrt = 1;
        }
        
        ecal.ethMonthName = obj.ethMonths(ecal.ethMonth);
        
        if (ecal.ethMonth <= 3) {
            ecal.ethQtr = 1;
        } else if (ecal.ethMonth <= 6 && ecal.ethMonth > 3) {
            ecal.ethQtr = 2;
        } else if (ecal.ethMonth <= 9 && ecal.ethMonth > 6) {
            ecal.ethQtr = 3;
        } else if (ecal.ethMonth <= 13) {
            ecal.ethQtr = 4;
        }
        return (ecal);
    }

    boolean is_leap(int yy)
    {
        if ( (yy%4 == 0) || ((yy%100 != 0) && (yy%400 == 0)) )
        {
            return true;
        }
        else
        {
            return false;
        }   
    }

    GregorianCalendar Date(String str, int y)
    {
        String res [] = new String[2];
        String dd;
        int pos=0, count=0,j=0, d;
        count = str.length();
        str = str.concat("-");
        
        for(int i=0; i< count+1; i++)
            {   
             if(str.charAt(i)=='-' || str.charAt(i)=='/' || str.charAt(i)=='.')
             {
                  dd = str.substring(pos, i);
                res[j] = dd;
                  j++;
                  pos = i + 1;
             }
        }
        d = Integer.parseInt(res[0].trim());
        int m = Month(res[1]);
        GregorianCalendar gg = new GregorianCalendar(y, m, d);
        return gg;
    }

    int Month(String s)
    {
        if(s.equals("Jan"))
            return 0;
        else if(s.equals("Feb"))
            return 1;
        else if(s.equals("March"))
            return 2;
        else if(s.equals("April"))
            return 3;
        else if(s.equals("May"))
            return 4;
        else if(s.equals("June"))
            return 5;
        else if(s.equals("July"))
            return 6;
        else if(s.equals("Aug"))
            return 7;
        else if(s.equals("Sep") || s.equals("Sept") ||s.equals("September"))
            return 8;
        else if(s.equals("Oct"))
            return 9;
        else if(s.equals("Nov"))
            return 10;
        else
            return 11;
    }
    
    public String ethMonths(int y)
    {
        if(y==1)
            return "Meskerem";
        else if(y==2)
            return "Tikemet";
        else if(y==3)
            return "Hidar";
        else if(y==4)
            return "Tahesas";
        else if(y==5)
              return "Tir";
        else if(y==6)
                return "Yekatit";
        else if(y==7)
                return "Megabit";
        else if(y==8)
                return "Miyaza";
        else if(y==9)
                return "Ginbot";
        else if(y==10)
                return "Sene";
        else if(y==11)
                return "Hamle";
        else if(y==12)
                return "Nehase";
        else if(y==13)
                return "Pegume";
        else
            return "Chek it";
    }

    public GregorianCalendar[] getGregorianDatesForEthYr (int ethYr)
    {
        GregorianCalendar gregDates[] = new GregorianCalendar[2];  
        // gregDates[0] will hold gregStartDate for ethYr
        // gregDates[1] will hold gregEndDate for ethYr
        
        EthiopianCalendar obj = new EthiopianCalendar();
        GregorianCalendar gcYrStart;
        GregorianCalendar gcYrEnd; 

        String ethYrStart       = "11-Sep";
        String ethLeapYrStart   = "12-Sep";
        String ethYrEnd         = "10-Sep";
        String ethLeapYrEnd     = "11-Sep";

        gcYrStart = obj.Date(ethYrStart, ethYr + 7);
        gcYrEnd   = obj.Date(ethLeapYrEnd, ethYr + 8);
        EthiopianCalendar ecStart = obj.getEthiopianDate(gcYrStart);
        EthiopianCalendar ecEnd   = obj.getEthiopianDate(gcYrEnd);

        if ((ethYr - ecStart.ethYear) == 1)
        {   gcYrStart = obj.Date(ethLeapYrStart, ethYr+7);      }
        
        if ((ecEnd.ethYear - ethYr) == 1)
        {   gcYrEnd = obj.Date(ethYrEnd, ethYr+8);              }

        gregDates[0] = gcYrStart;
        gregDates[1] = gcYrEnd;

        return gregDates;
    }
    
    public GregorianCalendar[] getGregorianDatesForEthFiscalYr (int ethFiscalYr)
    {
        EthiopianCalendar ec = new EthiopianCalendar();
        GregorianCalendar gcDates[] = new GregorianCalendar[2];
        int gregStartYrForEthFiscalYr, gregEndYrForEthFiscalYr;
        
        gcDates = ec.getGregorianDatesForEthYr(ethFiscalYr);
        
        gregStartYrForEthFiscalYr = gcDates[1].get(Calendar.YEAR);
        gregEndYrForEthFiscalYr   = gregStartYrForEthFiscalYr + 1;
        
        int ethFiscalMonth = 7;  // Eth Fiscal Year starts from 8-jul , ends on 7-jul
        int ethFiscalStartDay = 8;
        int ethFiscalEndDay = 7;
                
        gcDates[0].set( gregStartYrForEthFiscalYr, ethFiscalMonth -1, ethFiscalStartDay );
        gcDates[1].set( gregEndYrForEthFiscalYr, ethFiscalMonth -1, ethFiscalEndDay );
        
        return gcDates;
    }
}
