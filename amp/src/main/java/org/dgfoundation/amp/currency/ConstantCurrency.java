/**
 * 
 */
package org.dgfoundation.amp.currency;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

/**
 * Constant Currency
 * 
 * @author Nadejda Mandrescu
 */
public class ConstantCurrency {
    // Patterns to constant currency naming
    /** Constant currency code = "{@code <curr_code><year>-<calendar_id>}" */
    public static final String CODE_PATTERN = "%s%d-%d";
    /** Constant currency code = "{@code Constant <curr_name> <year> (<calendar_name>)}" */
    public static final String NAME_PATTERN = "Constant %s %d (%s)";
    // Patterns from constant currency naming
    public static final Pattern PARSE_YEAR_PATTERN = Pattern.compile("[1-9][0-9]*");
    /**
     * Actual Constant Currency
     */
    public final AmpCurrency currency;
    
    /**
     * Calendar it is linked to 
     */
    public final AmpFiscalCalendar calendar;
    
    /**
     * The year for which this calendar is selected
     */
    public final Integer year;
    /** StandardCurrencyCode */
    public final String standardCurrencyCode;

    public ConstantCurrency(AmpCurrency constantCurrency) {
        this.currency = constantCurrency;
        this.calendar = constantCurrency.getCalendar();
        this.year = retrieveYear(constantCurrency.getCurrencyCode());
        this.standardCurrencyCode = retrieveStandardCurrencyCode(constantCurrency.getCurrencyCode());
    }
    
    public static String buildConstantCurrencyCode(AmpCurrency standardCurrency, AmpFiscalCalendar calendar, 
            int year) {
        return String.format(CODE_PATTERN, standardCurrency.getCurrencyCode(), year, calendar.getAmpFiscalCalId());
    }
    
    public static String buildConstantCurrencyName(AmpCurrency standardCurrency, AmpFiscalCalendar calendar, 
            int year) {
        return String.format(NAME_PATTERN, standardCurrency.getCurrencyName(), year, calendar.getName());
    }
    
    public static Integer retrieveYear(String constantCurrencyCode) {
        Matcher m = PARSE_YEAR_PATTERN.matcher(constantCurrencyCode);
        if (m.find()) {
            return Integer.valueOf(m.group());
        }
        return null;
    }
    
    public static String retrieveStandardCurrencyCode(String constantCurrencyCode) {
        Matcher m = PARSE_YEAR_PATTERN.matcher(constantCurrencyCode);
        if (m.find()) {
            return constantCurrencyCode.substring(0, m.start());
        }
        return null;
    }
    
    public static String retrieveCCCurrencyCodeWithoutCalendar(String constantCurrencyCode) {
        if (constantCurrencyCode != null && constantCurrencyCode.contains("-"))
            return constantCurrencyCode.substring(0, constantCurrencyCode.indexOf("-"));
        return constantCurrencyCode;
    }
    
    @Override
    public String toString() {
        return currency == null ? "null" : currency.getCurrencyCode();
    }

}
