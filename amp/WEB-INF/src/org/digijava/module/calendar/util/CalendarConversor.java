package org.digijava.module.calendar.util;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class CalendarConversor {
   
   /* Linked list is used to organize the month and year of the calendar */
   private LinkedList[] calendarYear = new LinkedList[13] ;   

   /* class variables and constant variables */
   private static final int bce   = 5500 ;     // before common era (BCE)
   private int ce         = 0 ;                // commen era (CE)
      
   private String yearSaintName = "" ;
   private String day = "" ;
   private int date  = 0 ;
   private int month = 0 ;
   private int year  = 0 ;     
      
   /* defines array of week days in Ethiopic */
   private static String[] weekDays = {"\u121B\u12AD\u1230\u129E", "\u1218\u1261\u12D5", 
                                       "\u1210\u1219\u1235", "\u12A0\u122D\u1265", 
                                       "\u1245\u12F3\121C", "\u12A6\u1201\u12F5", "\u1230\u129E"} ;
   
   /* defines array of week days in Gregorian */
   private static String[] gregorianWeekDays = {"Tuesday", "Wednesday", "Thursday", "Friday", 
                                                "Saturday", "Sunday", "Monday"} ;

   /** 
    * Constructor: A linked-list for months is constructed.    *
    *
    * @param year the year to which a calendar will be built 
    */
   public CalendarConversor(int year) { 
      /* if the requested year is out of the allowable range, just year is set to 1 */
      if (year < 1) 
         this.ce = 1 ;
      else
         this.ce = year ;
      
      /* initialize calendarYear with 13 linked list objects for months */
      for (int i=0; i < calendarYear.length; i++)
         calendarYear[i] = new LinkedList() ;      
   }

   /** 
    * Returns the metqee for the given year
    *
    * @param ce  a year for which metqee is generated
    * @return    metiqee is equal to 30 - abekitee
    * @since     2.0
    */  
   public int getMetqee(int ce ) {
      int era = bce + ce ;
      era -= 1 ;
      int wember = era % 19 ;

      int abikete = (wember * 11) % 30 ;
      int metqee  = 30 - abikete ;      
      return metqee ;
   }
   
   /** 
    * Returns the Ethiopian new year day which will be used as starting day for
    * the given year
    *
    * @param ce  A year for which metqee is generated
    * @return    The name of the first day in String form
    * @since     2.0
    */  
   public String getNewYearDay(int ce) {
      /* tintyon is used to determine what the day is on new year */
      String[] tintyon = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;
            
      /* number of years until the give year  */
      int era = bce + ce ;        
      era -= 1 ; 

      /* determines the number of leap year until */
      int tmp = 0 ;
      for (int i=0; i <= era; i++) {
          if (i % 4 == 3) tmp++ ;
      }         
      /* calculates the days upto the given year */
      int daysSince = (era * 365) + tmp ;    
      /* returns the day of ethiopian new year */
      return tintyon[ daysSince % 7 ] ;      
   }

   
   /** 
    * Returns the day of the given date. It starts counting from Tuesday
    * of BCE era 1st day to arrive to the right one.
    *
    * @param month   Ethiopian month
    * @param date    Ethiopian date
    * @param ce      Ethiopian Common Era year  
    * @return        The day of the given date in String form
    * @since         2.0
    */  
   public static String getDay(int month, int date, int  ce ) {
      /* tintyon is used to determine what the day is on new year */      
      String[] tintyon = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;
      int era = bce + ce ;
      era -= 1 ;

      int tmp = 0 ;
      for (int i=0; i <= era; i++)
          if (i % 4 == 3) tmp++ ;

      int days = (era * 365) + tmp + ( (month - 1) * 30) + (date - 1) ;

      return tintyon[ days % 7 ] ;
   }
 
   /**
    * It prepares monthly holidays with integer array. It fetches each
    * Date instance and adds the monthly holidays to its list. 
    * These holidays will be mapped to proper names by the XMLer class during 
    * transformation. For that purpose, the XMLer class utilizes a properties
    * file.
    * <p> 
    * The properties file is "ealet_holiday_properties.xml" and has the following
    * form.
    * <p>
    * The key and values ares
    * <pre>
    *     1         abo
    *     ^          ^
    *    key       value
    *     ^          ^
    *     |          |
    *     |           ----- the name of the holiday
    *     | 
    *      ----- the date of the month to the holiday belongs
    * </pre>
    * @since      2.0
    */
   public void insertMonthlyHoliday() {
      /* these are some of the monthly holidays of Eth Orthodox religion and 
       * they are mapped to their proper names using "ealet_holiday_properties.xml"
       * by the XMLer class */
      int[] monthlyHoliday = {1, 5, 7, 12, 16, 19, 21, 23, 24, 27, 29 } ;
      try {         
         /* insers the new holiday into calendarYear */
         for (int i=0; i < 13 /* calendarYear.length */; i++) {
            LinkedList month  = calendarYear[i] ;  // gets month
            Day date = null ;                       // owner of the holiday                  
            LinkedList holiday = null ;            // holiday list
            Day newHoliday = null ;                // new holiday to be inserted
            for (int j=0; j < monthlyHoliday.length 
                          && monthlyHoliday[j] <= month.size(); j++) {
               /* since the list starts from 0, index should decremented by 1 when the 
                * list is  accessed */
               date = (Day) month.get(monthlyHoliday[j] - 1) ;      
               holiday = date.getHoliday() ;        // gets holiday list
               newHoliday = new Day() ;         
               newHoliday.setHolidayName("" + monthlyHoliday[j]) ;
               newHoliday.setHolidayType("monthly") ;
               holiday.add(newHoliday) ;           // inserts the holiday
               // //System.out.println("monthly holiday : " + monthlyHoliday[j]) ;               
            }
            // //System.out.println("month = " + i) ;            
         }         
      } catch (Exception e) {
         //System.out.println(e) ;
      }      
   }

   /**
    * The method prepares annual holidays with integer array. It fetches each
    * Day instance from the calendar and inserts these holidays.
    * The month and date where each annual holiday
    * occurs is used in combination as a key in the holiday properties 
    * file and used by the XMLer class during transformation to retrieve
    * the holiday names.
    * <p> 
    * The properties file is "ealet_holiday_properties.xml" and has the following
    * field and value content.
    * <p>
    * The key and values ares
    * <pre>
    *    1-1     Enkutatash
    *     ^          ^
    *    key       value
    *     ^          ^
    *     |          |
    *     |           ----- the name of the holiday
    *     | 
    *      ----- the 1st is month while the 2nd is date
    * </pre>
    * @since      2.0
    */   
   public void insertAnnualHoliday() {
      int[][] annualHoliday = { {1,1},  {1,10}, {1,17}, {3,15},  
                                {5,6},  {6,11}, {6,12}, {7,8},   
                                {8,10}, {11,5}, {12,13},{12,16} } ;

      try {         
         /* insers the new holiday into calendarYear */
         Day date = null ;                     // owner of the holiday                  
         LinkedList holiday = null ;            // holiday list
         Day newHoliday = null ;               // new holiday to be inserted
         
         for (int i=0; i < annualHoliday.length /* calendarYear.length */; i++) {
            LinkedList month  = calendarYear[annualHoliday[i][0] - 1] ;  // gets month
            if( annualHoliday[i][1] <= month.size()) {
               /* since the list starts from 0, index should decremented by 1 when the 
                * list is  accessed */
               date = (Day) month.get(annualHoliday[i][1] - 1) ;      
               holiday = date.getHoliday() ;        // gets holiday list
               newHoliday = new Day() ;         
               newHoliday.setHolidayName("" + annualHoliday[i][0] + "-" + annualHoliday[i][1]) ;
               newHoliday.setHolidayType("annual") ;
               holiday.add(newHoliday) ;           // inserts the holiday
            }
            // //System.out.println(newHoliday.getHolidayName()) ;            
         }         
      } catch (Exception e) {
         //System.out.println(e) ;
      }      
      
   }

   /** 
    * Fasting holidays are variables since they are calcualted based on moon's
    * epect. The 1st fasting holiday is Nenewei and it is calculated using
    * <b>metiqee</b> and <b>tewissak</b>. The remaining fasting holidays can be
    * determined based on Nenewei since their ranges are known. 
    * <p> 
    * As each fasting holiday is calculated, an instance of the Day class is
    * created and inserted into the calendar under each proper month and date. 
    * <b>This method must be called after the main calendar is built; that is.
    * after the <tt>calendarYear</tt> linked list is completely built</b>. 
    * Failing to do so will result fatal error.
    * @since      2.0
    */     
   public void insertMovingHolidays() {
      /* Ealetawi tewissak is used partly to determine "Nenewie tsome" and */
      /* I am not clear on its rationale, but it does work                 */      
      String[] tewissak  = {"", "",  "Fri", "Thu", "Wed", "Tue", "Mon", 
                            "Sun", "Sat"} ;

      /* Days counting starts from "Tuesday" */
      String[] tintyon   = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;

      /* Gaps among fasting holidays in Ethiopian Orthodox religion */
      int[] fastingHolidayTewissak = {0, 14, 41, 62, 67, 69, 93, 108, 118, 
                                      119, 121} ;

      /* Moving fasting holidays in Ethiopian Orthodox religion. */
      /* I was forced to hard-code them to maintain their occuring order */
      String[] fastingHoliday = {"neneway", "hudadei", "debre-zeyit", 
                                 "hossaena", "siklet",  "tinsae", 
                                 "rekibe-kahinat", "eriget", "piraqilitoss", 
                                 "tsome-hawariat", "tsome-dihinet"} ;
      
      /* Obtain the metqee for  this Ethiopian calendar year */
      int metqee = getMetqee(ce) ;

      /* Determine if metqee occurs in September or October */
      int tempMonth  = (metqee >= 15 ? 1 : 2) ;
      
      /* Determine the day of the metqee */ 
      String dayName = getDay(tempMonth, metqee,  ce ) ;

      /* Create an object for each fasting holiday */
      Day newHoliday = new Day() ;

      /* Adjust the tewissak for the Nenewie */
      int   t = 0 ; 
      for (t=0; !(tewissak[t].equals(dayName)) ; t++) ;
      int tmp = metqee + t ;

      /* determines Nenewei's date and month */
      tempMonth = ( (tmp > 30) || (metqee < 15) ? 6 : 5) ;
      int tempDate  = tmp % 30 + (tmp % 30 == 0 ? 30 : 0) ;
      
      int neneweiDate = tempDate ;  
      int neneweiMonth = tempMonth ;
      
      for(int i=0; i < fastingHolidayTewissak.length; i++) {
         /* (i==0) ? it is Nenewei, its day is calculated before this loop */
         if (i != 0) {
            /* determines the month of this holiday by modulating the range
             * between nenewei dates and the current holiday.  */
            if ( (neneweiDate + fastingHolidayTewissak[i])%30 == 0 ) { // does  the tseom on 30th?
               tempMonth = neneweiMonth + (neneweiDate + fastingHolidayTewissak[i]) / 30 ;
               tempMonth-- ; // since the 30th date is inclusive, tempMonth shouldn be decremented
            } else {
               tempMonth = neneweiMonth + (neneweiDate + fastingHolidayTewissak[i]) / 30 ;
            }
            /* determine the day this holiday occurs */
            tempDate  = (neneweiDate + fastingHolidayTewissak[i]) % 30 + 
                        ((neneweiDate + fastingHolidayTewissak[i]) % 30 == 0 ? 30 : 0) ;
         }
         
         /* assigns each holidays */
         newHoliday.setHolidayType("moving") ;
         newHoliday.setYear(ce) ;             
         newHoliday.setMonth(tempMonth) ;
         newHoliday.setDate(tempDate) ;
         newHoliday.setHolidayName(fastingHoliday[i]) ;
         newHoliday.setDay(getEthiopianDayIndex(newHoliday.getMonth(), newHoliday.getDate(), newHoliday.getYear()));

         /* insers the new holiday into calendarYear */
         LinkedList cal     = calendarYear[tempMonth-1] ;       // gets the calendar
         Day holidayOwner  = (Day) cal.get(tempDate-1) ;     // gets the Day instance
         LinkedList current = holidayOwner.getHoliday() ;       // gets access to Day instance
         current.add(newHoliday) ;                              // inserts holiday         
         newHoliday = new Day() ;                              // creates a new instance
      }
   }      


   /** 
    * Returns the day of a given Gregorian date
    *
    * @param month   Gregorian calendar month
    * @param date    Gregorian calendar date
    * @param ce    Gregorian calendar year
    * @return        The name of the day on the given Gregorian date
    * @since         2.0
    */     
   public String getGregorianDay(int  ce , int month, int date) {
      String[] days    = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"} ;
      int[] monthDates = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;

      if ( ( ce % 4 == 0 && ce % 100 != 0) || ce % 400 == 0 ) monthDates[1] = 29 ;
      
      int tmp = 0 ;
      for (int i=0; i <  ce ; i++)
         if ( (i % 4 == 0 && i % 100 != 0) || i % 400 == 0 ) tmp++ ;

      int within = 0 ;
      for (int i=0; i < (month - 1) ; i++)
         within += monthDates[i] ;

      ce -= 1 ;
      int dates = ( ce * 365) + tmp + within + (date - 1) ;

      return days[ dates % 7 ] ;
   }
      
   
   /**
    * The Ethiopian calendar doesn't follow the modification that was established 1582 by
    * Pop Gregory, because of that the Ethiopian calendar adds three extra days for every 
    * four hundred years than needed.
    * 
    * It is assumed that the first Ethiopian new year in CE occured on August 29, 07 of the
    * Western calendar. Until 1582 of Western calendar when an Ethiopian year successeds 
    * an Ethiopian leap year, a new year starts on August 30th; otherwise on August 29th.
    * 
    * This changed when Pope Gregory modified the Western calendar by shifting October 5, 1582
    * to October 15, 1582 instantly and introducing a new leap year calculation method. This 
    * removed the substantial defects in the calendar; unfortunately, the Ethiopian calendar
    * never adapted the reform, thus its new year continues to shift forward. 
    * 
    * Note: If the year preceeding any given year is a leap year, in the preceeding year, the
    * month of Paguemain has 6 days instead of 5. In this case, the new year begins one
    * day late than the other years. What this method does is the following
    * 
    *    a. It calculates the number of leap years until the desired year for Gregorian
    *    b. It calculates the number of leap years until the desired year for Ethiopian
    *    c. It determines the difference and adds that to August 29 to find out when the 
    *       new year occurs. 
    * 
    * @param eYear    Ethiopian year
    * @return        The date of September when Ethiopian new year occurs
    * @since         2.1
    */   
   public static int whenIsEthNewYearInGregMonth(int eYear) {
      // determines the number of Ethiopian leap years until now
      int eLeapPears = eYear / 4 ;      
      // calculates the equivalent Gregorian year
      int gYear = eYear + 7 ;
      // determines the number of Gregorian leap years until now
      int gLeapYears = ( (gYear/4) - (gYear/100) + (gYear/400) ) ;     
 
      // It is assumed that the first Ethiopian new year in CE occurred on August 29, 07
      // of the Gregorian date
      int firstEthNewYearOccuredInGregorianAugustCE = 29 ;
      // determines the number of days Ethiopian New Year advanced compared to Gregorian.
      int newYearDate = (firstEthNewYearOccuredInGregorianAugustCE + (eLeapPears - gLeapYears)) % 31  ;
      return newYearDate ;
   }

   
   /**
    * The method performs conversion from Ethiopian date to Gregorian. Breifely,
    * <ul>
    *    <li>Determines when Ethiopian new years occurs in Greg. September</li>
    *    <li>Initially, converts Eth year to Greg using the 7 years diff</li>
    *    <li>If Eth date falls after Dec, the Greg year must be up by 1 year</li>
    *    <li>Since the Eth new year is on September, its overlaps with Gregorian; 
    *        thus to calculate the Greg month an <b>order</b> is introduced</li>
    *    <li>Once the Greg date is found, the day is easily obtained</li>      
    * </ul>
    * 
    * @param month   Ethiopian calendar month
    * @param date    Ethiopian calendar date
    * @param year    Ethiopian calendar year
    * @return        Ethiopian day
    */
   public static Day toGregorian(int month, int date, int year) {
      /* creates an instance of Day */
      Day dayObj = new Day("", 0, month, date, year) ;
     
      /* if malformed date, then terminate while returning null */
      if (month == 0 || date == 0 || year == 0)
         return null ;
   
      /* determines when Eth new years occurs in Geg September */
      int newYearDay = whenIsEthNewYearInGregMonth(year) ;

      
      /* On Ethiopian September the difference between Greg. and Eth year is 7 */
      int gregorianYear = year + 7 ;
   
      /* Gregorian months sequenced with respect to Ethiopian; begins Sept */
      /* Note: index 0 is used as position holder */
      int[] gregorianMonths  = {0, 30, 31, 30, 31, 31, 28, 31, 30, 31, 30, 31, 31, 30} ;
   
      /* if (gregorianYear + 1) is leap year, then february has 29 days */
      int nextYear = gregorianYear + 1 ;
      if ( ((nextYear % 4 == 0) && (nextYear % 100 != 0)) || (nextYear % 400 == 0) ) 
         gregorianMonths[6] = 29 ;
         
      /* determine the number of days upto Ethiopian month/day/year */
      int until = ( (month-1) * 30) + date ;
      if (until <= 37 && year <= 1575) {   // I don't remember what i did here
         until += 28 ;
         //System.out.println("until = " + until) ;
         //System.out.println("until = " + until) ;
         //System.out.println("until = " + until) ;         
         gregorianMonths[0] = 31 ;
      } else {
         until += (newYearDay - 1) ;
      } 
   
      /* if "year" is ethiopian pre-leap-year, paguemain has six days */
      until = ( ( ( (year-1) % 4 == 3) && (month == 13) ) ? ++until : until) ;
   
      /* determine the exact matching month, date, and year of gregorian */
      int m = 0 ;
      for (int i=0; i < gregorianMonths.length; i++) {
         if (until <= gregorianMonths[i]) { 
            m = i ;
            dayObj.setGregorianDate(until) ;
            break ;
         } else {
            m = i ;
            until -= gregorianMonths[i]  ;
         }
      }
   
      /* Makes the gregorian months order with respect to the Ethiopian */   
      int order[] = {8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9} ;      
      /* if (m > 4) is true, gregorianYear must advance by 1. for m, 
       * the lower bound is 0; that is counting starts at 0 */
      dayObj.setGregorianYear((m > 4 ? ++gregorianYear : gregorianYear)) ;
      dayObj.setGregorianMonth(order[m]) ;
      dayObj.setYearName("") ;      
      /* it is ok to get the day using ethiopian date */
      dayObj.setDay(getEthiopianDayIndex(month, date, year)) ;
   
      return dayObj ;
   } 

   
   /**
    * The method performs conversion from Gregorian date to Ethiopian. Breifely,
    * <ul>
    *    <li>Determines when Ethiopian new years occurs in Greg. September</li>
    *    <li>Initially, converts Greg year to Eth using the 8 years diff</li>
    *    <li>If the Greg date falls after Eth new year, the Eth year must be up by 1
    *        year</li>
    *    <li>Since the Eth new year is on August||September||?, it overlaps with Gregorian; 
    *        thus to calculate the Greg month an <b>order</b> is introduced</li>
    *    <li>Then the Eth month and date are determined </li>      
    * </ul> 
    * @param month   Gregorian calendar month
    * @param date    Gregorian calendar date
    * @param year    Gregorian calendar year
    * @return        Gregorian day
    */
   public static Day toEthiopian(int month, int date, int year) {
      /* Saint name assigned to Ethiopian calendar */
      String[] saintName = {"Yohaniss", "Mathiwoss", "Markos", "Lukus"} ;
      
      /* create a day object */
      Day dayObj = new Day() ;
   
      /* if malformed date, then terminate while returning null */
      if (month == 0 || date == 0 || year == 0)
         return null ;
   
      /* if m/d/y falls in between 10/5/1583--10/14/1583, it is invalid */
      if ( (month == 10) && (date >= 5) && (date <= 14) && (year == 1582))
         return null ;
      
      /* declares array of standard Gregorian months in a year */
      /* Note: index 0 is used a position holder */
      int[] gregorianMonths  = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;
   
      /* declares array of Ethiopian months sequenced with respect to Gregorian, so it begins 
         from Tahissas (Jan in Greg). The reason why it starts from Tahissas is because the
         Eth and Greg months overlap one to another. */
      /* Note: index 0 is used a position holder */
      int[] ethiopianMonths  = {0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5, 30, 30, 30, 30} ;
   
      /* if it is gregorian leap year, then february has 29 days */
      if ( (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ) 
         gregorianMonths[2] = 29 ;
   
      /* In Aug||Sept||? there is 8 years deifference between Greg and Eth */
      int ethiopianYear = year - 8 ;
   
      /* if it is ethiopian leap year, then pagumain has 6 days */
      ethiopianMonths[10] = (ethiopianYear % 4 == 3 ? 6 : 5) ; 
   
      /* determines when Eth new years occurs in Geg September */
      int newYearDay = whenIsEthNewYearInGregMonth(ethiopianYear) ;
   
      /* determines the number of days upto month/day/year */
      int until = 0 ;
      for (int i=1; i < month; i++) 
         until += gregorianMonths[i] ;
      until += date ;
   
      /* updates tahissas to match january 1st and */ 
      int tahissas = 25 + (ethiopianYear % 4 == 0 ? 1 : 0) ;
   
      /* Note the change on oct, 1582 */      
      if (year < 1582) {
         ethiopianMonths[1] = 0 ;
         ethiopianMonths[2] = tahissas ; 
      } else if ( (until <= 277) && (year == 1582) ) {
         ethiopianMonths[1] = 0 ;
         ethiopianMonths[2] = tahissas ; 
      } else {
         tahissas = newYearDay - 3 ; 
         ethiopianMonths[1] = tahissas ; 
      }
   
      /* determine the actual equivalent ethiopian date */
      int m = 0 ;
      for (m=1; m < ethiopianMonths.length; m++) {
         // //System.out.println(m + " ==>\t " + until + "  " + "[" + ethiopianMonths[m] + "]") ;
         if (until <= ethiopianMonths[m]) {
            dayObj.setDate((m == 1 || ethiopianMonths[m] == 0 ? until + (30 - tahissas) : until)) ;
            break ;
         } else {
            until -= ethiopianMonths[m]  ;
         }
      }
   
      /* reorder to the proper ethiopian calendar and return result */
      int order[] = {0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2, 3, 4} ;
      /* if  (m > 10) is true, ethiopian year must be up by 1 */
      dayObj.setYear((m > 10 ? ++ethiopianYear : ethiopianYear)) ;
      dayObj.setMonth(order[m]) ;
      dayObj.setDay(getEthiopianDayIndex(dayObj.getMonth(), dayObj.getDate(), dayObj.getYear())) ;
      dayObj.setYearName(saintName[dayObj.getYear() % 4]) ;
      return dayObj ;
   } 

   /**
    * Performs conversion and validation on the given date performing the followings 
    * <ul>
    *    <li>It breaks into to tokens the incoming date (String) and converts them to int/li>
    *    <li>Checks if the mm, dd, and year are within allowed ranges</li>
    *    <li>If no error, a Day instance is created and returned to the caller</li>
    * </ul>
    *
    * @param dateSrc   Gregorian calendar month
    * @return          Day instance
    * @since           2.1
    */   
   static Day validateGregorianDate(String dateSrc) {  
      /* if date is null, terminate this flow and return null */
      if (dateSrc == null || "".equals(dateSrc)) return null ;
      
      int[] gregorianMonths  = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;
      
      /* try to tokenize date into mm, dd, and yyyy and convert it to int type */
      Day dayDst = new Day() ;
      
      try {
         int begin = 0, end   = 0 ;
         /* gets the month token and converts it to int */
         end = dateSrc.indexOf('/') ;
         int month = Integer.parseInt(dateSrc.substring(begin,end)) ;
         /* gets the date token and converts it to int */
         begin = ++end  ;
         end = dateSrc.indexOf('/', begin) ;
         int date = Integer.parseInt( dateSrc.substring(begin,end)) ;
         /* gets the year token and converts it to int */
         begin = ++end ;
         end = dateSrc.indexOf('/', begin ) ;
         int year = Integer.parseInt(dateSrc.substring(begin,dateSrc.length())) ;

         /* if it is gregorian leap year, then february has 29 days */
         if ( ( ((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0) ) 
            gregorianMonths[1] = 29 ;
         
         /* check if date, month, and year are valid */
         if (date > 0 && date <= gregorianMonths[month-1] && year >= 9) {            
            dayDst.setMonth(month) ;
            dayDst.setDate(date) ;
            dayDst.setYear(year) ;
         } else {
            return null ;
         }
      } catch (Exception e) {
         /* there seems to be a conversion problem, thus returning null is better */
         return null ;
      }
      return dayDst ;
   }

   /**
    * Performs conversion and validation on the given date performing the followings 
    * <ul>
    *    <li>It breaks into to tokens the incoming date (String) and converts them to int/li>
    *    <li>Checks if the mm, dd, and year are within allowed ranges</li>
    *    <li>If no error, a Day instance is created and returned to the caller</li>
    * </ul>
    *
    * @param dateSrc   Gregorian calendar month
    * @return          Day instance
    * @since           2.1
    */      
   static Day validateEthiopianDate(String dateSrc) {  
      /* if date is null, terminate this flow and return null */
      if (dateSrc == null || "".equals(dateSrc)) return null ;
      /* the last month (13th) has 6 days if year is leap year */
      int[] ethiopianMonths  = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5} ;            
      
      /* try to tokenize date into mm, dd, and yyyy and convert it to int type */
      Day dayDst = new Day() ;
      int begin = 0 ;
      int end   = 0 ;
      
      try {
         /* gets the month token and converts to int */
         end = dateSrc.indexOf('/') ;
         int month = Integer.parseInt(dateSrc.substring(begin,end)) ;
         /* gets the date token and converts to int */
         begin = ++end  ;
         end = dateSrc.indexOf('/', begin) ;
         int date = Integer.parseInt( dateSrc.substring(begin,end)) ;
         /* gets the year token and converts to int */
         begin = ++end ;
         end = dateSrc.indexOf('/', begin ) ;
         int year = Integer.parseInt(dateSrc.substring(begin,dateSrc.length())) ;

         /* if it is ethiopian leap year, then paguemain has 6 days */
         ethiopianMonths[12] = ( (year % 4) == 3 ? 6 : 5) ; 
         
         /* check if date, month, and year are valid */
         if (date > 0 && date <= ethiopianMonths[month-1] && year >= 1) {            
            dayDst.setMonth(month) ;
            dayDst.setDate(date) ;
            dayDst.setYear(year) ;
         } else {
            return null ;
         }
      } catch (Exception e) {
         /* there seems to be a conversion problem, thus returning null is better */
         return null ;
      }
      return dayDst ;
   }   
      
   /**
    * Determines what the day is for a given Ethiopian date. It starts counting
    * from 5500, the number of years in BCE, continuing the BC years. The 1st 
    * day starts with "Tuesday".
    * 
    * @param month   Ethiopian month
    * @param date    Ethiopian date
    * @param year    Ethiopian year
    * @return        Ethiopian day
    */
   public static int getEthiopianDayIndex(int month, int date, int year) {
      String[] tintyon = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;
      int era = bce + year ;
      era -= 1 ;

      /* Count the number of Ethiopian leap years */
      int tmp = 0 ;
      for (int i=0; i <= era; i++) {
          if (i % 4 == 3) tmp++ ;
      }
      /* calculates number of days since the beginning upto ehtYear */
      int days = (era * 365) + tmp + ( (month - 1) * 30) + (date - 1) ;

      return (days % 7) ;
   }
   
   /**
    * The method performs conversion from Gregorian date to Ethiopian. Breifely,
    * <ul>
    *    <li>Determines when Ethiopian new years occurs in Greg.</li>
    *    <li>Initially, converts Greg year to Eth using the 8 years diff</li>
    *    <li>If the Greg date falls after Eth new year, the Eth year must be up by 1
    *        year</li>
    *    <li>Since the Eth new year is on August||September||?, it overlaps with Gregorian; 
    *        thus to calculate the Greg month an <b>order</b> is introduced</li>
    *    <li>Then the Eth month and date are determined </li>      
    * </ul> 
    * 
    * The following assumptions are made
    * <ul>
    *    <li>In CE, the first Tahissas matches December 25.</li>
    *    <li>Conversion is allowed for Gregorian dates starting from 1/1/9.</li>
    *    <li>Dates from 10/05/1582 to 10/14/1582 are ignored.</li>
    *    <li>Julian method is used for leap years calcualtion until 10/04/1582, then Gregorian method.</li>
    *    <li>Ethiopina calendar adds a leap year to a year when it is module 4 with result 3.</li>
    * </ul> 
    *
    * @param gMonth   Gregorian calendar month
    * @param gDate    Gregorian calendar date
    * @param gYear    Gregorian calendar year
    * @return         Ethiopian day
    * @since          2.1
    */
   public static Day toEthiopianDate(int gMonth, int gDate, int gYear) {
      //System.out.println("From Gregorian Date to Ethiopian Date ---------------------") ;            
      /* Saint name assigned to Ethiopian calendar */
      String[] saintName = {"Yohaniss", "Mathiwoss", "Markos", "Lukus"} ;

      /* declares array of standard Gregorian months in a year */
      /* Note: index 0 is used a position holder */
      int[] gregorianMonths  = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;
      
      /* Comparing dates in integer form is an interesting one; it appears simple, but not */
      /* Note: calculating a leap year for this purpose isn't necessary */
      int lastDateJulianCalendarInGregorian = (1582 * 365) + 4;  
      for (int i=0; i < 10; i++) lastDateJulianCalendarInGregorian += gregorianMonths[i] ;

      /* This is 01/01/09 in Gregorian */
      int startingGregorianCalendarDate = (9*365) + 1 ;
      for (int i=0; i < gMonth; i++) startingGregorianCalendarDate += gregorianMonths[i] ;      
      
      /* This is gMonth/gDate/gYear in Gregorian */
      int thisDate = (gYear*365)  + gDate ;
      for (int i=0; i < gMonth; i++) thisDate += gregorianMonths[i] ;
      
      
      /* if malformed date, then terminate while returning null */
      if ( thisDate <  startingGregorianCalendarDate )
         return null ;
   
      /** If the given date occurs between 10/5/1582 and 10/14/1582, it should be
       *  invalid. These dates are dropped by Pope Gregory to correct the extra
       *  days produced by Julian calendar since the "birth of Jessus". 
       *  Unfortunately,  the Ethiopian calednar doesn't make a similar correction; 
       *  as a result, it introduces 3 extra days every 400 years. 
       */
      if ( (gMonth == 10) && (gDate >= 5) && (gDate <= 14) && (gYear == 1582))
         return null ;
   
      /* if it is gregorian leap year, then february has 29 days */
      if ( ( ((gYear % 4) == 0) && ((gYear % 100) != 0)) || ((gYear % 400) == 0) ) 
         gregorianMonths[2] = 29 ;
      
      /* determines the number of days upto gMonth/gDay/gYear */
      int gUntil = 0 ;
      for (int i=0; i < gMonth; i++) 
         gUntil += gregorianMonths[i] ;
      gUntil += gDate ;
            
      /* From January to Aug||Sept||, the Ethiopian year falls 8 years behind the Gregorian year */
      int eYear = gYear - 8 ;
      
      /* Declares array of Ethiopian months. There are 13 months in the calendar */
      int[] ethiopianMonths  = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5} ;
      
      /* if it is ethiopian leap year, then pagumain has 6 days */
      ethiopianMonths[12] = ( (eYear % 4) == 3 ? 6 : 5) ; 
            
      /* determines the number of Ethiopian leap years until now */
      /** Note: The reason why leap year is calculated out of (eYear-1) is the current year is tested and 
       *  proper modification is done above. If the current year is leap year, then Pagumain is updated to 6 
       */ 
      int eLeapYears = 0 ;      
      if ( thisDate <=  lastDateJulianCalendarInGregorian ) {
         eLeapYears = ( ((eYear-1) % 4) == 3 ? 1 : 0) ;   // determines if this is an Eth leap year
      } else {
         for (int i=1; i < eYear; i++) {
            if ( (i%4) == 3 )
               eLeapYears++ ;
         }
      }
      
      /** Determines the number of Gregorian leap years until now. If gYear occurs 
       *  before 1582; that is, before the introduction of Pope Gregory modification,
       *  then the leap year occurs on every year that is divisible by 4. The current year is not
       *  included in the calculation because that has been done above by modifying the array 
       *  gregorainMonths if it is a leap year. 
       */
      int gLeapYears = 0 ;
      if ( thisDate <=  lastDateJulianCalendarInGregorian )
         gLeapYears = (gYear-1)/4  ; 
      else
         gLeapYears = ( ((gYear-1)/4) - ((gYear-1)/100) + ((gYear-1)/400) ) ; 
          
      /** In Ethiopian calendar, a new year occurs in the first day of Meskerem and 
       *  the number of days between Meskerem 1, 01 and January 1, 07 was 126. Note
       *  that the Ethiopian year is 7/8 years behind than that of Gregorian year. 
       */
      int betweenMeskeremOneandJanuaryOne = 125 ;      
      
      /** Remember that the Ethiopian calendar fails to drop 1 day for every century 
       *  except on a centruy that is divisible by 400. In other words, the clanedar 
       *  shifts to the right from the Gregorian calendar and because of that the number 
       *  of days between Meskerem 1 and January 1 decreases. For correct conversion, 
       *  the shift must be accounted.
       */
      if ( thisDate <=  lastDateJulianCalendarInGregorian )      
         betweenMeskeremOneandJanuaryOne -= eLeapYears ;
      else 
         betweenMeskeremOneandJanuaryOne -= (eLeapYears - gLeapYears) ;
      
      //System.out.println("eLeapYears = " + eLeapYears + ", gLeapYears = " + gLeapYears) ;

      /* calculates the number of days between Meskerem and gMonth/gDay/gYear */
      int eUntil = betweenMeskeremOneandJanuaryOne + gUntil ;
      
      /** spreads eUntil, which is the number of days between Meskerem 1 and "?/?/?", 
       *  along the Ethiopian calendar year until the desired Ethiopian date is found.
       */
      int eMonth = 0 ;
      int eDate  = eUntil ;
      int numberOfMonths = 0 ;
//      //System.out.println("eUntil ===> " + eUntil) ;
//      //System.out.println("gUntil ===> " + gUntil) ;      
//      //System.out.println("betweenMeskeremOneandJanuaryOne ===> " + betweenMeskeremOneandJanuaryOne) ;    
      while (eUntil > ethiopianMonths[eMonth])  {
         /* keeps track of months and it is mainly used to reflect the Eth New Year that may 
            occur depending on the source date */
         numberOfMonths++ ;         
         
         /* if we are passed the last month, then we are in the new year, so increment eYear by 1 */
         if (numberOfMonths == 13) eYear++ ;   
         
         /* uses to count in which Ethiopian month we are in */
         /* counts days by spreading; that is, by deducting from eUntil days for current month */
         eUntil -= ethiopianMonths[eMonth++] ;
                  
         /* keeps track of possible dates */
         eDate = eUntil ;

         /* allows rollover within the array of ethiopianMonths */
         eMonth %= 13 ;                  
      } 

      /* current eMonth should up by 1, because the array of EthiopianMonths starts from 0 */
      eMonth++ ;
      
       /* creates an instance of Ethiopian day */
      Day eDay = new Day() ;
      
      /* set its Gregorian date ... first */
      eDay.setGregorianMonth(gMonth) ;
      eDay.setGregorianDate(gDate) ;
      eDay.setGregorianYear(gYear) ;
      eDay.setGregorianDay( gregorianWeekDays[getEthiopianDayIndex(eMonth, eDate, eYear)] ) ;            
      
      /* set its Ethiopian date ... next */      
      eDay.setMonth(eMonth) ;
      eDay.setDate(eDate) ;
      eDay.setYear(eYear) ;
      eDay.setDay(getEthiopianDayIndex(eDay.getMonth(), eDay.getDate(), eDay.getYear())) ;
      eDay.setDayName(weekDays[ getEthiopianDayIndex(eDay.getMonth(), eDay.getDate(), eDay.getYear()) ] ) ;
      eDay.setYearName(saintName[eDay.getYear() % 4]) ;
            
 //     //System.out.println(getEthiopianDayIndex(eDay.getMonth(), eDay.getDate(), eDay.getYear()) + "; " + gMonth + "/" + gDate + "/" + gYear + "\t" + eMonth + "/" + eDate + "/" + eYear) ;
      //System.out.println() ;
      return eDay ;
   } 

   /**
    * The method performs conversion from Ethiopian date to Gregorian. Breifely,
    * <ul>
    *    <li>Determines when Eth new years occurs in Gregorianr</li>
    *    <li>Initially, converts Eth year to Greg using the 7 years diff</li>
    *    <li>If the Eth date falls after Greg new year, the difference between Eth and Greg is 8 years</li>
    *    <li>The Eth new year occurs in August||September||? and it is determined by calculating the
    *        leap years of both Eth and Greg, finding the difference, and adding the result to
    *        the number of dates between the original Jan 1 and Meskerem 1.</li>
    *    <li>Then the Greg month and date are determined </li>      
    * </ul> 
    * 
    * The following assumptions are made
    * <ul>
    *    <li>In CE, the first Tahissas matches December 25.</li>
    *    <li>Conversion is allowed for Gregorian dates starting from 1/1/9.</li>
    *    <li>Dates from 10/05/1582 to 10/14/1582 in Greg are ignored.</li>
    *    <li>Julian method is used for leap years calcualtion until 10/04/1582, then Gregorian method.</li>
    *    <li>Ethiopina calendar adds a leap year to a year when it is module 4 with result 3.</li>
    * </ul> 
    *
    * @param eMonth   Gregorian calendar month
    * @param eDate    Gregorian calendar date
    * @param eYear    Gregorian calendar year
    * @return         Ethiopian day
    * @since          2.1
    */
   public static Day toGregorianDate(int eMonth, int eDate, int eYear) {
      //System.out.println("From Ethiopian Date to Gregorian Date *****************************************") ;
      /* Saint name assigned to Ethiopian calendar */
      String[] saintName = {"Yohaniss", "Mathiwoss", "Markos", "Lukus"} ;

      /* declares array of standard Ethiopian months in a year */
      /* Note: index 0 is used a position holder */
      int[] ethiopianMonths  = {0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5} ;
      
      /* Comparing dates in integer form is an interesting one; it appears simple, but not */
      /* The last Ethiopian date before Gregorian modification is 2/7/1575 */
      /* Note: calculating a leap year for this purpose isn't necessary */       

      int lastEthiopianDateBeforeGregorianModification = (1575 * 365) + 7;  
      for (int i=0; i < 2; i++) lastEthiopianDateBeforeGregorianModification += ethiopianMonths[i] ;

      /* This first Ethiopian date in CE is 01/01/01 */
      int startingEthiopianCalendarDate = (1 * 365) + 1  ;   // cheatting ????????????????????

      /* This is eMonth/eDate/eYear in Ethiopian */
      int thisDate = (eYear*365) + eDate ;
      for (int i=0; i < eMonth; i++) thisDate += ethiopianMonths[i] ;
            
      /* if malformed date, then terminate while returning null */
      if ( thisDate <  startingEthiopianCalendarDate )
         return null ;

      /* if it is ethiopian leap year, then paguemain has 6 days */
      ethiopianMonths[13] = ( (eYear % 4) == 3 ? 6 : 5) ; 
            
      /* determines the number of days upto gMonth/gDay/gYear */
      int eUntil = 0 ;
      for (int i=0; i < eMonth; i++) 
         eUntil += ethiopianMonths[i] ;
      eUntil += eDate ;
            
      /* From January to Aug||Sept||, the Gregorian year is ahead by 8 years; otherwise 7 years */
      int gYear = eYear + 7 ;
      
      /* Declares array of Ethiopian months. There are 13 months in the calendar */
      int[] gregorianMonths  = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;
      
      /* if it is gregorian leap year, then february has 29 days */
      if ( ( ((gYear % 4) == 0) && ((gYear % 100) != 0)) || ((gYear % 400) == 0) ) {
         gregorianMonths[1] = 29 ;
      }
      /* determines the number of Ethiopian leap years until now */
      /** Note: The reason why leap year is calculated out of (eYear-1) is the current year is tested and 
       *  proper modification is done above. 
       */ 
      int eLeapYears = 0 ;      
      if ( thisDate <=  lastEthiopianDateBeforeGregorianModification ) {
         eLeapYears = ( ((eYear-1) % 4) == 3 ? 1 : 0) ;   // determines if this is an Eth leap year
      } else {
         for (int i=1; i < eYear; i++) {
            if ( (i%4) == 3 )
               eLeapYears++ ;
         }
      }
      
      /** Determines the number of Gregorian leap years until now. If gYear occurs 
       *  before 1582; that is, before the introduction of Pope Gregory modification,
       *  then the leap year occurs on every year that is divisible by 4. The current year is not
       *  included in the calculation because that has been done above by modifying the array 
       *  gregorainMonths if it is a leap year. 
       */
      int gLeapYears = 0 ;
      if ( thisDate <=  lastEthiopianDateBeforeGregorianModification )
         gLeapYears = ( ( ((gYear % 4) == 0) && ((gYear % 100) != 0)) || ((gYear % 400) == 0) ) ? 1 : 0 ;
      else
         gLeapYears = ( ((gYear-1)/4) - ((gYear-1)/100) + ((gYear-1)/400) ) ; 
          
      /** In Ethiopian calendar, a new year occurs in the first day of Meskerem and 
       *  the number of days between January 1, 09 and Meskerem 1, 01 was 241. Note
       *  that the Ethiopian year fals 7/8 years behind than that of  gregorian year. 
       */
      int betweenJanuaryOneAndMeskeremOne = 240 ;      

      /** Remember that the Ethiopian calendar fails to drop 1 day for every century 
       *  except on a centruy that is divisible by 400. In other words, the clanedar 
       *  shifts to the right from the Gregorian calendar and because of that the number 
       *  of days between Meskerem 1 and January 1 decreases. For correct conversion, 
       *  the shift must be accounted.
       */
      if ( thisDate <=  lastEthiopianDateBeforeGregorianModification )      
         betweenJanuaryOneAndMeskeremOne += (eLeapYears + gLeapYears) ;
      else 
         betweenJanuaryOneAndMeskeremOne += (eLeapYears - gLeapYears) ;
      
      //System.out.println("eLeapYears = " + eLeapYears + ", gLeapYears = " + gLeapYears) ;

      /* calculates the number of days between Meskerem and gMonth/gDay/gYear */
      int gUntil = betweenJanuaryOneAndMeskeremOne + eUntil ;
      
      /** spreads eUntil, which is the number of days between Meskerem 1 and "?/?/?", 
       *  along the Ethiopian calendar year until the desired Ethiopian date is found.
       */
      int gMonth = 0 ;
      int gDate  = eUntil ;
      int numberOfMonths = 0 ;
//      //System.out.println("gUntil ===> " + gUntil) ;
//      //System.out.println("eUntil ===> " + eUntil) ;      
//      //System.out.println("betweenJanuaryOneAndMeskeremOne ===> " + betweenJanuaryOneAndMeskeremOne) ;    
      while (gUntil > gregorianMonths[gMonth])  {
         /* keeps track of months and it is mainly used to reflect the Eth New Year that may 
            occur depending on the source date */
         numberOfMonths++ ;         
         
         /**
          * if we are passed the last month, then we are in the new year, so increment gYear by 1.
          * Doing this causes one problem; if the previous year was a leap year, the statements 
          * way above would have made a modification on gregorianMonths array by making february 29,
          * which would be wrong if gYear is increased by 1 now, so another modification must be done.
          */
         if (numberOfMonths == 12) {
            gYear++ ;
            if ( ( ((gYear % 4) == 0) && ((gYear % 100) != 0)) || ((gYear % 400) == 0) ) {
               gregorianMonths[1] = 29 ;
            } else {
               gregorianMonths[1] = 28 ;
            }
            
         }   
         
         /* uses to count in which Ethiopian month we are in */
         /* counts days by spreading; that is, by deducting from eUntil days for current month */
         gUntil -= gregorianMonths[gMonth++] ;
                  
         /* keeps track of possible dates */
         gDate = gUntil ;

         /* allows rollover within the array of ethiopianMonths */
         gMonth %= 12 ;                  
      } 

      /* current eMonth should up by 1, because the array of EthiopianMonths starts from 0 */
      gMonth++ ;
     
       /* creates an instance of Gregorian day */
      Day gDay = new Day() ;

      /* set Ethiopian date ... first */
      gDay.setMonth(eMonth) ;
      gDay.setDate(eDate) ;
      gDay.setYear(eYear) ;
      gDay.setDay( getEthiopianDayIndex(eMonth, eDate, eYear) ) ;
      gDay.setDayName( weekDays[getEthiopianDayIndex(eMonth, eDate, eYear)] ) ;
      gDay.setYearName(saintName[gDay.getYear() % 4]) ;
      
      /* set its equivalent Gregorian */
      gDay.setGregorianMonth(gMonth) ;
      gDay.setGregorianDate(gDate) ;
      gDay.setGregorianYear(gYear) ;
      gDay.setGregorianDay( gregorianWeekDays[getEthiopianDayIndex(eMonth, eDate, eYear)] ) ;
            
  //    //System.out.println(getEthiopianDayIndex(eMonth, eDate, eYear) + "; " + eMonth + "/" + eDate + "/" + eYear + "\t" + gMonth + "/" + gDate + "/" + gYear) ;
      //System.out.println() ;
      
      return gDay ;
   } 

  /**
    * This method calls the toEthiopianDat(..) method and converts the result into UTF8 format
    * and return the result. 
    *
    * @param gMonth   Gregorian calendar month
    * @param gDate    Gregorian calendar date
    * @param gYear    Gregorian calendar year
    * @return         Ethiopian day
    * @since          2.1
    */
   public static String toEthiopianDateUTF8(int gMonth, int gDate, int gYear) {
      Day eDay = toEthiopianDate(gMonth, gDate, gYear) ;  
      if ( eDay != null ) {        
         String date = "" + eDay.getGregorianDay() + " " + eDay.getMonth() + "/" + eDay.getDate() + "/" + eDay.getYear() ;
         String dateUTF ;
         try {
            dateUTF = new String(date.getBytes("UTF-8"), "UTF-8") ;
         } catch (UnsupportedEncodingException uee) {
            return null ;
         }
         return dateUTF ;
      } else
         return null ;
   }  

   /**
    * This method calls the toGregorianDat(..) method and converts the result into UTF8 format
    * and return the result. 
    *
    * @param dateSrc  Ethiopian date in String type
    * @return         Ethiopian day in String type
    * @since          2.1
    */
   public static String toGregorianDateUTF8(String dateSrc) {
      /* validates the incoming date first before processing conversion */      
      Day day = validateEthiopianDate(dateSrc) ;
      if ( day != null ) {  
         day = toGregorianDate(day.getMonth(), day.getDate(), day.getYear()) ; 
         String date = "" + day.getGregorianDay() + " " + day.getGregorianMonth() + "/" + day.getGregorianDate() + "/" + day.getGregorianYear() ;
         String dateUTF ;
         try {
            dateUTF = new String(date.getBytes("UTF-8"), "UTF-8") ;
         } catch (UnsupportedEncodingException uee) {
            return null ;
         }
         return dateUTF ;
      } else
         return null ;
   }

   /**
    * This method calls the toEthiopianDat(..) method and converts the result into UTF8 format
    * and return the result. 
    *
    * @param dateSrc  Gregorian date in String type
    * @return         Ethiopian day in String type
    * @since          2.1
    */
   public static String toEthiopianDateUTF8(String dateSrc) {
      /* validates the incoming date first before processing conversion */
      Day day = validateEthiopianDate(dateSrc) ;
      if ( day != null ) {  
         day = toEthiopianDate(day.getMonth(), day.getDate(), day.getYear()) ; 
         String date = "" + day.getGregorianDay() + " " + day.getMonth() + "/" + day.getDate() + "/" + day.getYear() ;
         String dateUTF ;
         try {
            dateUTF = new String(date.getBytes("UTF-8"), "UTF-8") ;
         } catch (UnsupportedEncodingException uee) {
            return null ;
         }
         return dateUTF ;
      } else
         return null ;
   }
   
   /**
    * This method calls the toEthiopianDat(..) method and converts the result into UTF8 format
    * and return the result. 
    *
    * @param gMonth   Gregorian calendar month
    * @param gDate    Gregorian calendar date
    * @param gYear    Gregorian calendar year
    * @return         Ethiopian day
    * @since          2.1
    */
   public static String toGregorianDateUTF8(int gMonth, int gDate, int gYear) {
      Day gDay = toGregorianDate(gMonth, gDate, gYear) ;  
      if ( gDay != null ) {        
         String date = "" + gDay.getGregorianDay() + " " + gDay.getGregorianMonth() + "/" + gDay.getGregorianDate() + "/" + gDay.getGregorianYear() ;
         String dateUTF ;
         try {
            dateUTF = new String(date.getBytes("UTF-8"), "UTF-8") ;
         } catch (UnsupportedEncodingException uee) {
            return null ;
         }
         return dateUTF ;
      } else
         return null ;
   }

   /**
    * This method generates the calendar for the requested year. It involves
    * a serious of steps.
    * <ul>
    *    <li>Determins the 1st day of both the Eth and Greg year</li>
    *    <li>Generates the equivalent Greg year for Eth</li>
    *    <li>If Greg leap year, March will have 29 days</li>
    *    <li>Using linked list for months and days, calendar is built</li>
    *    <li>Updates the calendar by adding holidays, and fasting holidays</li>
    * </ul>
    * Finally, it returns an array of a linked list consisting of the calendar.
    *
    * @since      2.0
    */
   public LinkedList[] getEthiopianGregorianCalendar() {
      /* if ce is bad, terminate method */
      if (ce <= 0) return null;
      
      /* Saint name assigned to Ethiopian calendar */
      String[] saintName = {"Yohaniss", "Mathiwoss", "Markos", "Lukus"} ;

      /* the 1st day, in counting, begins with Tuesday, thus Tintyon */     
      String[] tintyon = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;      

      /* Gregorian days */
      String[] gDays = {"Tuesday", "Wednesday", "Thursday", "Friday", 
                        "Saturday", "Sunday", "Monday"} ;
            
      /* get the index of ethiopian day for (1,1,am) */
      /** The reason why I am using an integer to specify a day is because the Ethiopic day names written in
          Unicode were requiring that they are included within the code using the form \\u+xxxx, which is
          difficult to read, so I am using property files for all Ethiopic names and phrases used by this
          work.
       */
      int dayIndex = getEthiopianDayIndex(1, 1, ce) ;

      /* create a Date obj with a value of greg. converted from ethiopian */
      Day gDay = toGregorianDate(1, 1, ce) ;
      
      /* initialize the starting day for gregorian calendar */
      int gDate  = gDay.getGregorianDate() ;
      int gMonth = gDay.getGregorianMonth() ;
      int gYear  = gDay.getGregorianYear() ;

      /* convert the ethiopian year to gregorian */
      int gregorianYear = ce + 7 ;
            
      /* numbers of days in every month of gregorian year modified to start from August/september */   
      int[] gregorianMonths  = {0, 30, 31, 30, 31, 31, 28, 31, 30, 31, 30, 31, 31, 30} ;

      /* if (gregorianYear + 1) is leap year, then february has 29 days */
      int nextGregorianYear = gregorianYear + 1 ;
      if ( ((nextGregorianYear % 4 == 0) && (nextGregorianYear % 100 != 0)) || (nextGregorianYear % 400 == 0) ) 
         gregorianMonths[6] = 29 ;
      
      /* If this is Ethiopian leap year, the 13th month will have 6 days */
      boolean isEthiopianLeapYear = false ;      
      isEthiopianLeapYear = (ce % 4 == 3? true : false) ;
      
      /* determine the number of days upto month/day/year */
      int until = ( (month-1) * 30) + date ;
      /* if date is less than October 15 (Gr) and year is less than 1582 (Gr), 
         Ethiopian new year occurs in August */
      if (until <= 37 && year <= 1575)
         gregorianMonths[0] = 31 ;

      /* get the saint name for the given year */
      String yearName = saintName[ce % 4] ;
      int gCounter = 1 ;                                  // a big ???
      
      /* generate the calendar based on Ethiopian, but include Gregorian */
      for (int i=0,m=30; i < calendarYear.length; i++) {
         /* if it is Ethiopian leap year, make the 13th month 6, else 5 */
         if (isEthiopianLeapYear && i == 12)        // 13th month in Eth
            m = 6 ;
         else if (i == 12)                          // 13th month in Eth 
            m = 5 ;
         
         /* work on months and days */         
         for (int j=0; j < m; j++) {
            Day dayObj = new Day(yearName, dayIndex, i+1, j+1,  ce, 
                                 gDays[dayIndex++], gMonth, gDate, gYear) ;
            calendarYear[i].add(dayObj) ;             
            dayIndex = (dayIndex == 7 ? 0 : dayIndex) ;         
            gDate++ ;
            if (gDate > gregorianMonths[gCounter]) {
               gMonth = (++gMonth) % 12 + (gMonth == 12 ? 12 : 0 ) ;
               if (gMonth == 1) gYear++ ;    // if it is Jan, gYear is up by 1
               gCounter++ ; 
               gDate = 1 ;
            } 
         }
      }
      /* update the new calendar wiht moving (religious) holidays */
      insertMovingHolidays() ;
      /* update the new calendar wiht monthly holidays */
      insertMonthlyHoliday() ;   
      /* update the new calendar wiht annual holidays */
      insertAnnualHoliday() ;      
      /* now deliver the calendar */
      return calendarYear ;
   }
   
   /**
    * This method generates the calendar for the requested year. It involves
    * a serious of steps.
    * <ul>
    *    <li>Determins the 1st day of both the Eth and Greg year</li>
    *    <li>Generates the equivalent Greg year for Eth</li>
    *    <li>If Greg leap year, March will have 29 days</li>
    *    <li>Using linked list for months and days, calendar is built</li>
    *    <li>Updates the calendar by adding holidays, and fasting holidays</li>
    * </ul>
    * Finally, it returns an array of a linked list consisting of the calendar.
    *
    * @since      2.0
    */
   public LinkedList[] createEthiopianGregorianCalendar() {
      /* if ce is bad, terminate method */
      if (ce <= 0) return null;
      
      /* Saint name assigned to Ethiopian calendar */
      String[] saintName = {"Yohaniss", "Mathiwoss", "Markos", "Lukus"} ;

      /* the 1st day, in counting, begins with Tuesday, thus Tintyon */     
      String[] tintyon = {"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"} ;      

      /* Gregorian days */
      String[] gDays = {"Tuesday", "Wednesday", "Thursday", "Friday", 
                        "Saturday", "Sunday", "Monday"} ;
            
      /* get the index of ethiopian day for (1,1,am) */
      /** The reason why I am using an integer to specify a day is because the Ethiopic day names written in
          Unicode were requiring that they are included within the code using the form \\u+xxxx, which is
          difficult to read, so I am using property files for all Ethiopic names and phrases used by this
          work.
       */
      int dayIndex = getEthiopianDayIndex(1, 1, ce) ;

      /* create a Date obj with a value of greg. converted from ethiopian */
      Day gDay = toGregorianDate(1, 1, ce) ;
      
      /* if this is an illegal date, terminate the func */
      if (gDay == null) return null ;
      
      /* initialize the starting day for gregorian calendar */
      int gDate  = gDay.getGregorianDate() ;
      int gMonth = gDay.getGregorianMonth() ;
      int gYear  = gDay.getGregorianYear() ;
            
      /* numbers of days in every month of gregorian year modified to start from August/september */   
      // int[] gregorianMonths  = {0, 30, 31, 30, 31, 31, 28, 31, 30, 31, 30, 31, 31, 30} ;
      
      int[] gregorianMonths  = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31} ;      

      /* if (gYear + 1) is leap year, then february has 29 days */
      int nextGregorianYear = gYear + 1 ;
      if ( ((nextGregorianYear % 4 == 0) && (nextGregorianYear % 100 != 0)) || (nextGregorianYear % 400 == 0) ) 
         gregorianMonths[2] = 29 ;

      /* declares an array of Ethiopian months */
      int[] ethiopianMonths  = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 5} ;      

      /* If this is Ethiopian leap year, the 13th month will have 6 days */
      ethiopianMonths[12] += ( (ce % 4) == 3? 1 : 0) ;
      
      /* get the saint name for the given year */
      String yearName = saintName[ce % 4] ;
      int gCounter = 1 ;                                  // a big ???
      
      /* generate the calendar based on Ethiopian, but include Gregorian */
      for (int i=0; i < calendarYear.length; i++) {         
         /* work on months and days */         
         for (int j=0; j < ethiopianMonths[i]; j++) {
            Day dayObj = new Day(yearName, dayIndex, i+1, j+1,  ce, 
                                 gDays[dayIndex++], gMonth, gDate, gYear) ;
            calendarYear[i].add(dayObj) ;             
            dayIndex = (dayIndex == 7 ? 0 : dayIndex) ;         
            gDate++ ;
            if (gDate > gregorianMonths[gMonth]) {
               gMonth = (++gMonth) % 12 + (gMonth == 12 ? 12 : 0 ) ;
               if (gMonth == 1) gYear++ ;    // if it is Jan, gYear is up by 1
               gCounter++ ; 
               gDate = 1 ;
            } 
         }
      }
      /* update the new calendar wiht moving (religious) holidays */
      insertMovingHolidays() ;
      /* update the new calendar wiht monthly holidays */
      insertMonthlyHoliday() ;   
      /* update the new calendar wiht annual holidays */
      insertAnnualHoliday() ;      
      /* now deliver the calendar */
      return calendarYear ;
   }
   
   
   /* ----------------------------------------------------------------------------------------------- */
   public static void  main(String[] args) {
/*
      Calendar.toEthiopianDate(1, 1, 9) ;      
      Calendar.toEthiopianDate(1, 1, 10) ;      
      Calendar.toEthiopianDate(1, 1, 11) ;      
      Calendar.toEthiopianDate(1, 1, 12) ;      
      Calendar.toEthiopianDate(1, 1, 13) ;      
      Calendar.toEthiopianDate(1, 1, 14) ;      
      Calendar.toEthiopianDate(1, 1, 15) ;                  
*/

/*
      Calendar.toEthiopianDate(8, 29, 9) ;      
      Calendar.toEthiopianDate(8, 29, 10) ;      
      Calendar.toEthiopianDate(8, 30, 11) ;      
      Calendar.toEthiopianDate(8, 29, 12) ;      
      Calendar.toEthiopianDate(8, 29, 13) ;      
      Calendar.toEthiopianDate(8, 29, 14) ;      
      Calendar.toEthiopianDate(8, 30, 15) ;                  
*/      

      /* testing Meskerem 1 for years that occurs before 1582 */
/*
      Calendar.toEthiopianDate(8, 29, 1488) ;      
      Calendar.toEthiopianDate(8, 29, 1489) ;      
      Calendar.toEthiopianDate(8, 29, 1490) ;      
      Calendar.toEthiopianDate(8, 30, 1491) ;      
      
      Calendar.toEthiopianDate(8, 29, 1492) ;      
      Calendar.toEthiopianDate(8, 29, 1493) ;      
      Calendar.toEthiopianDate(8, 29, 1494) ;                  
      Calendar.toEthiopianDate(8, 30, 1495) ;                       
*/
/*      
      Calendar.toEthiopianDate(9, 8, 1588) ;      
      Calendar.toEthiopianDate(9, 8, 1589) ;      
      Calendar.toEthiopianDate(9, 8, 1590) ;      
      Calendar.toEthiopianDate(9, 9, 1591) ;      
      
      Calendar.toEthiopianDate(9, 8, 1592) ;      
      Calendar.toEthiopianDate(9, 8, 1593) ;      
      Calendar.toEthiopianDate(9, 8, 1594) ;                  
      Calendar.toEthiopianDate(9, 9, 1595) ;        
      Calendar.toEthiopianDate(12, 30, 1595) ;        
*/
/*
      Calendar.toEthiopianDate(9, 27, 1582) ;  
      Calendar.toGregorianDate(1, 30, 1575) ;
      
      Calendar.toEthiopianDate(9, 28, 1582) ;
      Calendar.toGregorianDate(2, 1, 1575) ;
      
      Calendar.toEthiopianDate(9, 29, 1582) ;
      Calendar.toGregorianDate(2, 2, 1575) ;
      
      Calendar.toEthiopianDate(9, 30, 1582) ;      
      Calendar.toGregorianDate(2, 3, 1575) ;
      
      Calendar.toEthiopianDate(10, 1, 1582) ;                    
      Calendar.toGregorianDate(2, 4, 1575) ;
      
      Calendar.toEthiopianDate(10, 2, 1582) ; 
      Calendar.toGregorianDate(2, 5, 1575) ;
      
      Calendar.toEthiopianDate(10, 3, 1582) ;                          
      Calendar.toGregorianDate(2, 6, 1575) ;
   
      Calendar.toEthiopianDate(10, 4, 1582) ;                    
      Calendar.toGregorianDate(2, 7, 1575) ;
      
      Calendar.toEthiopianDate(10, 15, 1582) ;                          
      Calendar.toGregorianDate(2, 8, 1575) ;

      Calendar.toEthiopianDate(10, 15, 1582) ;                          
      Calendar.toGregorianDate(2, 8, 1575) ;

      Calendar.toGregorianDate(1, 1, 1585) ;
      Calendar.toGregorianDate(1, 1, 1586) ;
      Calendar.toGregorianDate(1, 1, 1587) ;
      Calendar.toGregorianDate(1, 1, 1588) ;      
 */
//      Calendar.toEthiopianDate(8, 29, 1261) ;      
      CalendarConversor.toGregorianDate(1, 1, 1581) ;                  
      
//      Calendar.toEthiopianDate(8, 29, 1262) ;      
      CalendarConversor.toGregorianDate(1, 1, 1582) ;         
      
//      Calendar.toEthiopianDate(8, 29, 1263) ;      
      CalendarConversor.toGregorianDate(1, 1, 1583) ;               
      
//      Calendar.toEthiopianDate(8, 30, 1264) ;      
      CalendarConversor.toGregorianDate(1, 1, 1584) ;      
      CalendarConversor.toEthiopianDate(07, 02, 2007) ;           
      
   }
   
}


