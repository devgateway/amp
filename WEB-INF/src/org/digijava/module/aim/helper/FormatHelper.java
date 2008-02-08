package org.digijava.module.aim.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Sebastian Dimunzio 
 * This a helper class for number formating,
 */
public class FormatHelper {

    private static Logger logger = Logger.getLogger(FormatHelper.class);

    	/**
         * Return an string based on Global Setting Number Format
         * @param number
         * @return
         */
    public static String formatNumber(Double number) {
	
	String format = "###,###,###,###.##";
	String decimalSeparator = ".";
	String groupSeparator = ",";

	// get setting from global setting
	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);

	String result;
	if (number == null) {
	    number = new Double(0d);
	}

	DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
	decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
	decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
	DecimalFormat formater = new DecimalFormat(format, decSymbols);
	result = formater.format(number);
	return result;
    }

    /**
         * Parse a String tring based on Global Setting Format to Double
         * 
         * @param number
         * @return
         * @throws ParseException
         */
    public static Double parseDouble(String number) {

	String format = "###,###,###,###.##";
	String decimalSeparator = ".";
	String groupSeparator = ",";

	// get setting from global setting
	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);

	Double result;
	DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
	decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
	decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
	DecimalFormat formater = new DecimalFormat(format, decSymbols);
	try {
	    result = formater.parse(number).doubleValue();
	} catch (ParseException e) {
	    logger.error("Error parsing String to double", e);
	   return null;
	}
	return result;
    }
    public static DecimalFormat getDefaultFormat() {

	String format = "###,###,###,###.##";
	String decimalSeparator = ".";
	String groupSeparator = ",";

	// get setting from global setting
	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);

	DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
	decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
	decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
	DecimalFormat formater = new DecimalFormat(format, decSymbols);
	
	return formater;
    }
    
    public static String getDecimalSymbol() {
	return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getDecimalSeparator());
    }

    public static String getGroupSymbol() {
	return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getGroupingSeparator());
    }

    
    
    public static String getDifference(String s1, String s2)
	{
		double d1 = parseDouble(s1) ;
		double d2 = parseDouble(s2) ;
		double d3 = d1 - d2 ;
		return formatNumber(d3) ;
	}
    
 
}
