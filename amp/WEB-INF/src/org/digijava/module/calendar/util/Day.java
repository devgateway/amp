package org.digijava.module.calendar.util;
import java.util.* ;
import java.io.* ;

public class Day {
   /* Variables for Ethiopian day */ 
   private String yearName  = "" ;
   private String dayName   = "" ;
   
   private int day     = 0 ;
   private int date    = 0 ;
   private int month   = 0 ;
   private int year    = 0 ;
   
   /* Linked list data structure is used to maintain holidays */
   private LinkedList holiday = new LinkedList() ;
   
   /* Holiday name is maintained in this variable*/
   private String holidayName         = "" ;
   private String holidayType         = "" ;   
   
   /* Ethiopian week days starting from Sunday in integer form */     
   int[] weekDays = {5, 6, 0, 1, 2, 3, 4} ;  // reorders Tintyon      
   
   /* Variables for Gregorian day */
   private String gregorianDay = "" ;
   private int gregorianDate   = 0 ;
   private int gregorianMonth  = 0 ;
   private int gregorianYear   = 0 ;
   
   /** 
    * Empty constructor
    */
   public Day() {}
   /**
    * Instance with Ethiopian day attributes
    */
   public Day(String yearName, int day, int month, int date, int year) {
      this.yearName  = yearName ;
      this.day   = day ;
      this.date  = date ;
      this.month = month ;
      this.year  = year ;
   }
   
   /** 
    * Instance with both Ethiopian and Gregorian day attributes
    */
   public Day(String yearName, int day, int month, int date, int year, 
              String gregorianDay, int gregorianMonth, int gregorianDate, 
                                                int gregorianYear) {
      this.yearName  = yearName ;
      this.day   = day ;
      this.date  = date ;
      this.month = month ;
      this.year  = year ;
      this.gregorianDay   = gregorianDay ;
      this.gregorianDate  = gregorianDate ;
      this.gregorianMonth = gregorianMonth ;
      this.gregorianYear  = gregorianYear ;
   }
   
   /**
    * Returns Tintyon, which is an integer form of the day of a week
    * @since          2.0
    */
   public int[] getWeekDays() { return weekDays ; 
   }
   /**
    * Sets a holiday instnace which is LinkedList
    * @param holiday  Any of those monthly, annual, or moving holiday
    * @since          2.0
    */
   public void setHoliday(LinkedList holiday) { 
      this.holiday  = holiday ; 
   }
   /**
    * Returns holiday which is linkedList
    * @since          2.0
    */
   public LinkedList getHoliday() { return holiday ;
   }

   /**
    * Sets the dayName variable to the name of the Ethiopian day
    * @param dayName  Ethiopian day name
    * @since           2.1
    */
   public void setDayName(String dayName) { 
      try {
         byte[] dayNameArray = dayName.getBytes("UTF-8") ; 
         this.dayName  = new String(dayNameArray, "UTF-8") ; 
      } catch (UnsupportedEncodingException uee) {}
   }
   
   /**
    * Returns Ethiopian day name 
    * @since           2.1
    */
   public String getDayName() { return dayName ; }  
   
   /**
    * Sets the name variable to the saint name year
    * @param yearName  Ethiopian calendar years are assigned saint names
    * @since           2.0
    */
   public void setYearName(String yearName) { 
      this.yearName  = yearName ; 
   }
   
   /**
    * Returns yearName Ethiopian calendar years are assigned saint names
    * @since           2.0
    */
   public String getYearName() { return yearName ; }
   
   /**
    * Sets the day variable
    * @param day       Ethiopian calendar day
    * @since           2.0
    */   
   public void setDay(int day) {
      this.day   = day ; 
   }
   
   /**
    * Returns day      Ethiopian calendar day
    * @since           2.0
    */   
   public int getDay()  { return day ; }
   
   /**
    * Sets the month variable
    * @param month     Ethiopian calendar month
    * @since           2.0
    */   
   public void setMonth(int month) {
      this.month = month ; 
   }
   
   /**
    * Returns month    Ethiopian calendar month
    * @since           2.0
    */   
   public int getMonth() { return month ; }
   
   /**
    * Sets the date variable
    * @param date      Ethiopian calendar date
    * @since           2.0
    */   
   public void setDate(int date) { 
      this.date  = date ; 
   }
   
   /**
    * Returns date     Ethiopian calendar date
    * @since           2.0
    */   
   public int getDate() { return date ; }
   /**
    * Sets the year variable
    * @param year      Ethiopian calendar year
    * @since           2.0
    */   
   public void setYear(int year) { 
      this.year  = year ; 
   }
   
   /**
    * Returns year     Ethiopian calendar year
    * @since           2.0
    */   
   public int getYear() { return year ; }
   
   /**
    * Sets the feastHoliday variable
    * @param holidayName Any of monthly, annual, or moving holidays
    * @since 2.0
    */   
   public void setHolidayName(String holidayName) { 
      this.holidayName = holidayName ; 
   }
   
   /**
    * Returns feastHolidays Any of monthly, annual, or moving holidays
    * @since 2.0
    */   
   public String getHolidayName()  { return holidayName ; }
   
   /**
    * Holiday type refers to monthly, annual, or moving holidays
    * @param holidayType Monthly, Annual, or Moving holiday
    * @since 2.0
    */   
   public void setHolidayType(String holidayType) { 
      this.holidayType = holidayType ; 
   }
   
   /**
    * Returns what type of holiday is this.
    * @since 2.0
    */   
   public String getHolidayType()  { return holidayType ; }   
   
   /**
    * Sets gregorian day
    * @param gregorianDay 
    * @since           2.0
    */   
   public void setGregorianDay(String gregorianDay)  { 
      this.gregorianDay = gregorianDay ; 
   }
   
   /**
    * Returns gregorian day
    * @since           2.0
    */      
   public String getGregorianDay() { return gregorianDay ; }
    
   /**
    * Sets gregorian month
    * @param gregorianMonth 
    * @since           2.0
    */   
   public void setGregorianMonth(int gregorianMonth)  { 
      this.gregorianMonth = gregorianMonth ; 
   }
   
   /**
    * Returns gregorian month
    * @since           2.0
    */      
   public int getGregorianMonth() { return gregorianMonth ; }
   
   /**
    * Sets gregorian date
    * @param gregorianDate 
    * @since           2.0
    */   
   public void setGregorianDate(int gregorianDate) { 
      this.gregorianDate  = gregorianDate ; 
   }
   
   /**
    * Returns gregorian date
    * @since           2.0
    */      
   public int getGregorianDate() { return gregorianDate ; }
   
   /**
    * Sets gregorian year
    * @param gregorianYear 
    * @since           2.0
    */   
   public void setGregorianYear(int gregorianYear) { 
      this.gregorianYear  = gregorianYear ; 
   }
   
   /**
    * Returns gregorian year
    * @since           2.0
    */      
   public int getGregorianYear() { return gregorianYear ; }   
}

