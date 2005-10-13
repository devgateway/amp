package org.digijava.module.aim.helper;

import org.digijava.module.aim.util.DbUtil;
import org.apache.log4j.Logger;
import java.text.DecimalFormat;

public class CurrencyWorker {
	private static Logger logger = Logger.getLogger(CurrencyWorker.class);

	public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###");

	private static double temp = 0.0;

	private static double resultDbl = 0.0;

	private static String resultStr = "";

	private static double exchangeRate = 0.0;

	public static String convert(double amt, double fromExchangeRate,
			double toExchangeRate) {
		if (logger.isDebugEnabled())
			logger.debug("convert passed amt=" + amt + " ,fromExchangeRate="
					+ fromExchangeRate + ",toExchangeRate" + toExchangeRate);
		if (fromExchangeRate != toExchangeRate) {
			double inter = 1 / fromExchangeRate;
			inter = inter * amt;
			resultDbl = inter * toExchangeRate;
		} else {
			resultDbl = amt;
		}
		resultStr = mf.format(Math.round(resultDbl));

		if (logger.isDebugEnabled())
			logger.debug("convert returns=" + resultStr);
		return resultStr;
	}

	public static double convert(double Amt, String currencyCode) {
		if (logger.isDebugEnabled())
			logger.debug("convert passed amt=" + Amt + " ,currencyCode="
					+ currencyCode);
		exchangeRate = DbUtil.getExchangeRate(currencyCode);
		resultDbl = exchangeRate * Amt;
		return resultDbl;
	}

	public static double convert1(double amt, double fromExchangeRate,
			double toExchangeRate) {
		if (fromExchangeRate != toExchangeRate) {
			double inter = 1 / fromExchangeRate;
			inter = inter * amt;
			resultDbl = inter * toExchangeRate;
		} else {
			resultDbl = amt;
		}
		return Math.round(resultDbl);
	}

	/**
	 * Formats the amount to include commas and decimal places Commas will be
	 * inserted after every three digits
	 * 
	 * @param amt The aount value in String which is to be formatted
	 * @return The formatted ammount
	 * 
	 * eg: The input will be a String like 25000000 and output will be a
	 * formatted string in the form 25,000,000.00
	 */
	public static String formatAmount(String amt) {

		if (amt == null)
			return "0.00";

		String fmt = amt.trim();
		fmt = fmt.replaceAll(",", "");

		double value = 0;
		try {
			value = Double.parseDouble(fmt);
		} catch (NumberFormatException e) {
			logger.error("Trying to parse " + fmt + " to double :" + e);
			return "0.00";
		}
		
		return DecimalToText.ConvertDecimalToText(value);
		
	}
	
	public static boolean validateAmount(String s) {
		boolean valid = true;
		try {
			double temp = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			logger.error("Invalid amount " + s);
			valid = false;
		} 
		return valid;
	}
}
