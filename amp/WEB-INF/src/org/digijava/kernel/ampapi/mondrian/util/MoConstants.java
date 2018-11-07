package org.digijava.kernel.ampapi.mondrian.util;

/**
 * 
 * @author Nadejda Mandrescu
 * @since July 2014 - Mondrian from-scratch reimplementation
 */
public final class MoConstants {

    //Values
    public static final Integer UNDEFINED_KEY = 999999999;
    public static final String FILTER_UNDEFINED_MAX = String.valueOf(UNDEFINED_KEY - 1);

    /**
     * date as it comes out of Mondrian running atop MonetDB
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * Undefined amount as minimum value supported by MonetDB, that is almost "-Double.MAX_VALUE".
     * Must be defined explicitly instead of null, otherwise measures data will also be empty even if it exists.
     * */ 
    public static final String UNDEFINED_AMOUNT_STR = "-999888777666";
    
}
