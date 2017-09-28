package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class YearUtil {
    private static Logger logger = Logger.getLogger(YearUtil.class) ;
    
    /** This method will return a collection of year objects for the Year 
     * from, to select options
     * @return Collection
     */
    public static Collection getYears() {
        if ( logger.isDebugEnabled() )
            logger.debug("GETYEARS() < ");      
        GregorianCalendar gc = new GregorianCalendar() ;
        ArrayList years = new ArrayList() ;
        
        int year = gc.get(Calendar.YEAR) ;
        int startYear  = year - Constants.FROM_YEAR_RANGE ;
        int endYear = year+ Constants.TO_YEAR_RANGE;
        int temp = startYear ;
        
        while ( temp <= endYear ) {
            Year y = new Year() ;
            y.setYear(temp) ;
            years.add(y) ;
            temp++ ;
        }
        if ( logger.isDebugEnabled() )
            logger.debug("GETYEARS() RETURNING COLLECTION OF SIZE : " +     years.size());
        return years ;
    }
}
