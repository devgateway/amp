package org.digijava.module.aim.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.util.convert.converter.AbstractNumberConverter;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.exotic.AmpDateFormatterFactory;

/**
 * @author Sebastian Dimunzio 
 * This a helper class for number formatting
 */
public class FormatHelper {

    private static Logger logger = Logger.getLogger(FormatHelper.class);
    
    /**
    * Parse a String tring based on Global Setting Format to Double
    *
    * @param number
    * @return
    * @throws ParseException
    */
    public static Double parseDouble(String number) {
        if(number==null) return null;
        if("".equalsIgnoreCase(number)){
            return new Double(0);
        }
        try {
            return getDecimalFormat().parse(number).doubleValue();
        } catch (ParseException e) {
            logger.error("Error parsing String to double", e);
            return null;
        }
    }

    public static String getDifference(String one, String two) {
        return formatNumber(parseDouble(one) - parseDouble(two));
    } 
    
    public static BigDecimal parseBigDecimal(String number) {
        if (number == null) return null;
        if (number.isEmpty()) {
            return new BigDecimal(0);
        }
        try {
            return new BigDecimal(getDecimalFormat().parse(number).toString());
        } catch (ParseException e) {
            logger.error("Error parsing String to double", e);
            return null;
        }
    }
    
    public static String formatNumber(double nr) {
        if (nr == 0) 
            return "";
        return getDecimalFormat().format(new Double(nr));
    }
    
    /**
    * Return an string based on Global Setting Number Format
    * @param number
    * @return
    */
    public static String formatNumber(Double number) {
        return formatNumber((Number)number);
    }

    public static String formatNumber(Number number) {
        if (number == null || number.doubleValue() == 0.0)
            return "";
        return getDecimalFormat().format(number);
    }

   /**
    * Return a string based on Global Setting Number Format, not rounded. 
    * The number has the symbols decimal and group separator, but shows all decimal digits
    * @param number
    * @return 
    */
    public static String formatNumberNotRounded(Double number) {
        if (number == null) {
            number = new Double(0d);
        }
        return getDecimalFormatNotRounded().format(number);
    }
    
    public static DecimalFormat getDecimalFormatNotRounded(){
        String decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
        String groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);
        DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
        decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
        return new DecimalFormat("###,###.###", decSymbols);
    }
    
    public static DecimalFormat getDecimalFormat(){
        return getDefaultFormat();
    }
    
    public static DecimalFormat getDecimalFormat(boolean replaceSpacesToNoBreakSpace){
        return getDefaultFormat(replaceSpacesToNoBreakSpace);
    }
    
    public static DecimalFormat getDefaultFormat() {
        return getDefaultFormat(false);
    }

    public static String formatPercentage(Number number) {
        if ( number == null || number.doubleValue() == 0.0)
            return "";
        return getPercentageDefaultFormat(false).format(number);
    }

    /**
     * used in percentage rendering in AF preview and similar places
     * @param replaceSpacesToNoBrSpace
     * @return
     */
    public static DecimalFormat getPercentageDefaultFormat(boolean replaceSpacesToNoBrSpace) {
        // get settings from global setting
        String format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
        String decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
        String groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);
        if(replaceSpacesToNoBrSpace) {
            decimalSeparator=decimalSeparator.replace(' ', '\u00A0');
            groupSeparator=groupSeparator.replace(' ', '\u00A0');
        }
        DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
        decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        if(groupSeparator!=null){
            decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
        }
        DecimalFormat formatter = new DecimalFormat(format, decSymbols);
        return formatter;
    }

    /**
     * 
     * @param replaceSpacesToNoBrSpace Convert spaces to no-break space (U+00A0) to fix problems with browser conversions.
     * Space is not valid thousands-separator, but no-br space is.
     * @see AbstractNumberConverter
     * @return
     */
    public static DecimalFormat getDefaultFormat(boolean replaceSpacesToNoBrSpace) {
    String format = "###,###,###,###.##";
    String decimalSeparator = ".";
    String groupSeparator = ",";
    // get setting from global setting
    format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
    decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
    groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);
    if(replaceSpacesToNoBrSpace) {
        decimalSeparator=decimalSeparator.replace(' ', '\u00A0');
        groupSeparator=groupSeparator.replace(' ', '\u00A0');
    }

    int amountsUnitCode = Integer.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS));

    if(amountsUnitCode != AmpARFilter.AMOUNT_OPTION_IN_UNITS) {
        //use the decimal separator to learn how many decimals we have:
        //for format definition decimal place is defined by .  
        int groupPlace = format.indexOf(".");
        if (groupPlace == -1) {
            //no decimal places, we don't allow that when thousands or millions = on, we add three
            format += ".000";
        } else {
            String[] formatBreaked = format.split("[.]");
                String decimal = formatBreaked[1];
                if (decimal.length() < 3) {
                    for (int i = 1; i < (3 - decimal.length()); i++) {
                        //if decimal is .00 it will add one 0 more .000 if decimal format is .## it will add a new # more
                        decimal += decimal.charAt(decimal.length() - 1);
                    }
                }
                format = formatBreaked[0]+"."+decimal;
        }
        /*
            int flength=format.length();
            for(int i=1;i<=(flength-groupPlace-2);i++)
                format+=format.charAt(flength - 1);
            }*/
    }
    
    DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
    decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
    
    if(groupSeparator!=null){
          decSymbols.setGroupingSeparator(groupSeparator.charAt(0)); 
     }
    DecimalFormat formater = new DecimalFormat(format, decSymbols);
    
    return formater;
    }
    
    public static String getDecimalSymbol() {
        return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getDecimalSeparator());
    }

    public static String getGroupSymbol() {
        return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getGroupingSeparator());
    }

    public final static String formatDate(GregorianCalendar date, String formatString) {
        return date == null ? null : new SimpleDateFormat(formatString).format(date.getTime());
    }
    
    public final static boolean isValidDateString(String sDate, SimpleDateFormat formatter) {
        if (sDate == null)
            return true;
        if (sDate.length() == 0)
            return true;
        try {
            return (formatter.parse(sDate) != null);
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static GregorianCalendar parseLocalizedDate(String sDate) {
        return parseDate(sDate, Locale.forLanguageTag(TLSUtils.getEffectiveLangCode()));
    }
    
    public static GregorianCalendar parseDate(String sDate) {
        return parseDate(sDate, Locale.getDefault());
    }
    
    private static GregorianCalendar parseDate(String sDate, Locale locale){
        if (sDate == null) return null;
        String defaultFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
        SimpleDateFormat formatter = new SimpleDateFormat(defaultFormat, locale);
        GregorianCalendar result = new GregorianCalendar();
        try {
            result.setTime(formatter.parse(sDate));
            return result;
        } catch (ParseException e) {
            throw new RuntimeException("The source string doesn't conform to format \"" + defaultFormat + "\"", e);
        }
    }
    
    public static Date parseDate2(String sDate){
        try {
            LocalDate ld = AmpDateFormatterFactory.getDefaultFormatter().parseDate(sDate);
            return java.sql.Date.valueOf(ld);
        } catch (Exception e) {
            logger.error("Can't parse date " + sDate, e);
            return null;
        }
    }
    
   public static String formatDate(Date date) {
       if(date == null) return null;
       String defaultFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
       return new SimpleDateFormat(defaultFormat, Locale.forLanguageTag(TLSUtils.getEffectiveLangCode())).format(date); 
   }
}
