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

		/*
		 * Modified by Priyajith Formated the amount to contain decimal places
		 * for ex. if the String value is 2,500,000 this will be made into
		 * 2,500,000.00
		 */
		//resultStr = insertDecmalPlaces(resultStr);

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
		/*
		 * logger.debug("convert passed amt="+ amt +"
		 * ,fromExchangeRate="+fromExchangeRate
		 * +",toExchangeRate"+toExchangeRate) ;
		 */
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
		

		/*
		String dec = "";
		if (fmt.indexOf(".") != -1) {
			dec = fmt.substring(fmt.indexOf(".") + 1, fmt.length());
			fmt = fmt.substring(0, fmt.indexOf("."));
		}

		if (fmt.trim().length() == 0)
			fmt = "0";

		StringBuffer buf = new StringBuffer(fmt);
		int itrCnt = (buf.length() - 1) / 3;
		buf = buf.reverse();
		for (int i = 0; i < itrCnt; i++) {
			buf.insert((((i + 1) * 3) + i), ',');
		}
		buf = buf.reverse();
		if (dec.trim().length() != 0) {
			buf.append('.');
			buf.append(dec);
		}

		fmt = buf.toString();
		*/
	}
	
	/*
	public static String insertDecmalPlaces(String amt) {
		if (amt == null) return null;
		String resultStr = amt.trim();
		if (resultStr.indexOf(".") == -1) {
			resultStr += ".00";
		} else {
			int index = resultStr.indexOf(".");
			if (index + 1 < resultStr.length()) {
				String temp = resultStr
						.substring(index + 1, resultStr.length());
				if (temp.length() == 1) {
					resultStr += "0";
				}
			} else {
				resultStr += "00";
			}
		}
		
		return resultStr;
	}*/
	
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
