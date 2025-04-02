package org.dgfoundation.amp.ar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FiscalPeriodHelper {
    
    private final static String FIRST_YEAR_REPLACEMENT  = "FFFF";
    private final static String LAST_YEAR_REPLACEMENT   = "LLLL";
    
    private final String[] MONTHS_ARRAY = { "JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY",
               "AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
    
    private final List<String> MONTHS   = Collections.unmodifiableList( Arrays.asList(MONTHS_ARRAY) );
    
    private int monthDifference = 0;
    
    private int yearDifference  = 0;
    
    private String nameModel    = null;
    
    public FiscalPeriodHelper(Map<String, String> monthMapping, int defaultMonthDifference) throws Exception {
        this.monthDifference    = defaultMonthDifference;
        if (monthMapping != null && monthMapping.size() > 0) {

            Iterator<Entry<String, String>> iter    = monthMapping.entrySet().iterator();
            Entry<String,String> e                  = iter.next();
            String key                              = e.getKey();
            String value                            = e.getValue();
            if ( key != null && value != null ) {
                int origMonthIndex                      = MONTHS.indexOf(key.trim().toUpperCase());
                int fiscalMonthIndex                    = MONTHS.indexOf(value.trim().toUpperCase());
                
                this.monthDifference                    = fiscalMonthIndex - origMonthIndex;
            }
        }
    }
    
    public FiscalPeriodHelper(Map<String, String> yearMapping) throws Exception {
        
        String yearRegEx    = "\\d{4}?";
        Pattern pattern     = Pattern.compile(yearRegEx);
        
        if (yearMapping == null || yearMapping.size() == 0)
            throw new Exception("Map should not be empty!");
        
        Iterator<Entry<String, String>> iter    = yearMapping.entrySet().iterator();
        Entry<String,String> e                  = iter.next();
        Integer origYear                        = Integer.parseInt(e.getKey());
        String fiscalYear                       = e.getValue();
        
        if ( fiscalYear != null ) {
            Matcher matcher = pattern.matcher(fiscalYear);
            if ( matcher.find() ) {
                String firstYearString  = matcher.group();
                Integer firstYear       = Integer.parseInt(firstYearString);
                this.yearDifference     = firstYear - origYear;
                String newString        = matcher.replaceFirst( FiscalPeriodHelper.FIRST_YEAR_REPLACEMENT );
                matcher.reset(newString);
                if (matcher.find()) {
                    this.nameModel      = matcher.replaceFirst(FiscalPeriodHelper.LAST_YEAR_REPLACEMENT); 
                }
                else 
                    this.nameModel      = newString;
                
            }
        }
        
    }
    
    public String getFiscalMonth(String originalMonth) throws Exception {
        String myMonth  = originalMonth.trim().toUpperCase();
        int myId        = MONTHS.indexOf(myMonth);
        
        int newId       = myId + this.monthDifference;
        
        if ( newId > 11 )
            newId   = newId - 12;
        if ( newId < 0 ) 
            newId   = newId + 12;
        
        
        if ( newId <0 || newId >11) 
            throw new Exception("Couldn't find corresponding fiscal month for month" + originalMonth);
        
        String ret      = MONTHS.get(newId);
        return ret;
    }
    
    public String getFiscalYear(String originalYearString) throws Exception {
        int originalYear    = Integer.parseInt(originalYearString);
        
        return this.getFiscalYear(originalYear);
        
    }
    
    public String getFiscalYear (int originalYear) {
        String ret  =  nameModel.replace(FIRST_YEAR_REPLACEMENT, (originalYear+this.yearDifference) + "");
        ret         = ret.replace(LAST_YEAR_REPLACEMENT, (originalYear+this.yearDifference+1) + "");
        
        return ret;
    }
    
}
