package org.digijava.module.aim.helper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;

import mondrian.rolap.BitKey.Big;
import mondrian.util.Bug;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;

public class CurrencyWorker {
	private static Logger logger = Logger.getLogger(CurrencyWorker.class);

	public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###.##");

	private static BigDecimal resultDbl = new BigDecimal(0);

	private static String resultStr = "";

	private static double exchangeRate = 0.0;

	public static BigDecimal convertToBigDecimal(BigDecimal amt, double fromExchangeRate,
			double toExchangeRate)
	{
		if (logger.isDebugEnabled())
			logger.debug("convert passed amt=" + amt + " ,fromExchangeRate="
					+ fromExchangeRate + ",toExchangeRate" + toExchangeRate);
		if (fromExchangeRate != toExchangeRate&&fromExchangeRate!=0) {
			BigDecimal inter = new BigDecimal(1 / fromExchangeRate);
			inter = inter.multiply(amt) ;
			resultDbl = inter.multiply(new BigDecimal(toExchangeRate))  ;
		} else {
			resultDbl = amt;
		}

		return resultDbl;
	}

	public static String convert(BigDecimal amt, double fromExchangeRate,
			double toExchangeRate) {

		resultDbl	= convertToBigDecimal(amt, fromExchangeRate, toExchangeRate);

		//*** fix for AMP-1755
//		DecimalFormat format = new DecimalFormat();
		String inputString= FormatHelper.formatNumber(resultDbl);
//		String inputString = String.valueOf(resultDbl);
//		resultStr = CurrencyWorker.formatAmount(inputString);

//		resultStr = mf.format(Math.round(resultDbl));

		if (logger.isDebugEnabled())
			logger.debug("convert returns=" + resultStr);
//		return resultStr;
		return inputString;
	}


	public static BigDecimal convertToUSD(BigDecimal amnt,String fromCurrencyCode)  throws AimException{
		exchangeRate = CurrencyUtil.getLatestExchangeRate(fromCurrencyCode);
		return amnt.multiply(new BigDecimal(exchangeRate)) ;
	}
        public static double convertToDefaultCurr(double amnt,String fromCurrencyCode) throws AimException {
          double amount=0;
                try {
                  exchangeRate = CurrencyUtil.getLatestExchangeRate(
                      fromCurrencyCode);
                }
                catch (AimException ex) {
                  throw ex;
                }
                amount=amnt/exchangeRate;
                return  amount;
        }


	public static BigDecimal convertFromUSD(BigDecimal amnt, String toCurrencyCode) throws AimException{
		exchangeRate = CurrencyUtil.getLatestExchangeRate(toCurrencyCode);
		return amnt.divide(new BigDecimal(exchangeRate));
	}

	public static BigDecimal convertFromUSD(BigDecimal amnt, Long toCurrencyId) throws AimException{
		AmpCurrency ampcurrency = CurrencyUtil.getAmpcurrency(toCurrencyId);
		return convertFromUSD(amnt,ampcurrency.getCurrencyCode());
	}



	public static BigDecimal convert(BigDecimal amt, String currencyCode) {
		if (logger.isDebugEnabled())
			logger.debug("convert passed amt=" + amt.toString() + " ,currencyCode="
					+ currencyCode);
		exchangeRate = CurrencyUtil.getExchangeRate(currencyCode);
		resultDbl = amt.multiply(new BigDecimal(exchangeRate));
		return resultDbl;
	}

	public static BigDecimal convert1(BigDecimal amt, double fromExchangeRate,
			double toExchangeRate) {
		if (fromExchangeRate != toExchangeRate && fromExchangeRate != 0d) {
			BigDecimal inter = new BigDecimal(1 / fromExchangeRate);
			inter = inter.multiply(amt);
			resultDbl = inter.multiply(new BigDecimal(toExchangeRate));
		} else {
			resultDbl = amt;
		}

		return resultDbl;
	}


	
	public static DecimalWraper convertWrapper(BigDecimal amt, double fromExchangeRate,double toExchangeRate, Date date) {
		DecimalWraper result = new DecimalWraper();
		BigDecimal reference = new BigDecimal(1d);
		BigDecimal amount = amt;
		BigDecimal fromRate = new BigDecimal(fromExchangeRate);
		BigDecimal toRate = new BigDecimal(toExchangeRate);
		BigDecimal inter = reference.divide(fromRate,30,java.math.RoundingMode.HALF_EVEN);
		
		if (fromExchangeRate != toExchangeRate) {
			BigDecimal tmp;
			tmp=amount.multiply(inter);
			result.setValue(tmp.multiply(toRate));
			result.setCalculations(result.toString() + "= ((" + 1 + "/"
					+ fromExchangeRate + ") * " + amount.toString() + " * " + toExchangeRate + ") " + date.toString());
			} else {
			result.setValue(amount);
			result.setCalculations("No need it's due rate");

		}
		return result;
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
			return FormatHelper.formatNumber(new Double(0));
		double value = 0;
		try {
			value = FormatHelper.parseDouble(amt);
		} catch (NumberFormatException e) {
			logger.error("Trying to parse " + amt + " to double :" + e);
			return FormatHelper.formatNumber(new Double(0));
		}
		return FormatHelper.formatNumber(value);

	}


	public static String removeCharsFromDouble(String s)
	{
		String tmp=s;
		String text="";
		if ( tmp != null )	{
			char arr[] = tmp.toCharArray() ;
			//text = "" ;
			for (int i = 0 ; i < tmp.length() ; i++ )
			{
				if ( arr[i] >= '0' && arr[i]<='9' || arr[i] == '.')
					text += arr[i] ;
			}
		}
		else
			text += "0.0";
		return text;
	}


	public static double formatToDouble(String amt)
	{
		if(amt==null)
			return 0;
		String fmt = amt.trim();
		fmt = fmt.replaceAll(",", "");
		fmt=removeCharsFromDouble(fmt);
		double value = 0;
		try
		{
			value = Double.parseDouble(fmt);
		}
		catch(NumberFormatException e)
		{
			logger.error("Trying to parse " + fmt + " to double :" + e);
			return -1;
		}
		return value;
	}

	public static boolean validateAmount(String s) {
		boolean valid = true;
		//s=removeCharsFromDouble(s);
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			logger.error("Invalid amount " + s);
			valid = false;
		}
		return valid;
	}
}
