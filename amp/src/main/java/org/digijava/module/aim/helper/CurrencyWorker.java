package org.digijava.module.aim.helper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;

public class CurrencyWorker {
    private static Logger logger = Logger.getLogger(CurrencyWorker.class);

    public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###.##");

    private static double resultDbl = 0.0;

    private static String resultStr = "";

    private static double exchangeRate = 0.0;

    public static double convertToDouble(double amt, double fromExchangeRate,
            double toExchangeRate)
    {
        if (logger.isDebugEnabled())
            logger.debug("convert passed amt=" + amt + " ,fromExchangeRate="
                    + fromExchangeRate + ",toExchangeRate" + toExchangeRate);
        if (fromExchangeRate != toExchangeRate&&fromExchangeRate!=0) {
            double inter = 1 / fromExchangeRate;
            inter = inter * amt;
            resultDbl = inter * toExchangeRate;
        } else {
            resultDbl = amt;
        }

        return resultDbl;
    }

    public static String convert(double amt, double fromExchangeRate,
            double toExchangeRate) {

        resultDbl   = convertToDouble(amt, fromExchangeRate, toExchangeRate);

        //*** fix for AMP-1755
//      DecimalFormat format = new DecimalFormat();
        String inputString= FormatHelper.formatNumber(resultDbl);
//      String inputString = String.valueOf(resultDbl);
//      resultStr = CurrencyWorker.formatAmount(inputString);

//      resultStr = mf.format(Math.round(resultDbl));

        if (logger.isDebugEnabled())
            logger.debug("convert returns=" + resultStr);
//      return resultStr;
        return inputString;
    }


    public static double convertToUSD(double amnt,String fromCurrencyCode)  throws AimException{
        exchangeRate = CurrencyUtil.getLatestExchangeRate(fromCurrencyCode);
        return amnt*exchangeRate;
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


    public static double convertFromUSD(double amnt, String toCurrencyCode) throws AimException{
        exchangeRate = CurrencyUtil.getLatestExchangeRate(toCurrencyCode);
        return amnt/exchangeRate;
    }

    public static double convertFromUSD(double amnt, Long toCurrencyId) throws AimException{
        AmpCurrency ampcurrency = CurrencyUtil.getAmpcurrency(toCurrencyId);
        return convertFromUSD(amnt,ampcurrency.getCurrencyCode());
    }



    public static double convert(double Amt, String currencyCode) {
        if (logger.isDebugEnabled())
            logger.debug("convert passed amt=" + Amt + " ,currencyCode="
                    + currencyCode);
        exchangeRate = CurrencyUtil.getExchangeRate(currencyCode);
        resultDbl = exchangeRate * Amt;
        return resultDbl;
    }

    public static double convert1(double amt, double fromExchangeRate,
            double toExchangeRate) {
        if (fromExchangeRate != toExchangeRate && fromExchangeRate != 0) {
            double inter = 1 / fromExchangeRate;
            inter = inter * amt;
            resultDbl = inter * toExchangeRate;
        } else {
            resultDbl = amt;
        }

        return resultDbl;
    }

    public static DecimalWraper convertWrapper(double amt, double fromExchangeRate, double toExchangeRate, Date date)
    {
        DecimalWraper result = new DecimalWraper();
        BigDecimal reference = new BigDecimal(1d);
        BigDecimal amount = new BigDecimal(amt);
        BigDecimal fromRate = new BigDecimal(fromExchangeRate);

        BigDecimal toRate = new BigDecimal(toExchangeRate);
        BigDecimal inter = reference.divide(fromRate,30,java.math.RoundingMode.HALF_EVEN);
        
        if (fromExchangeRate != toExchangeRate) {
            BigDecimal tmp = amount.multiply(inter);
            result.setValue(tmp.multiply(toRate));
            
            // calculations is only used for debugging reasons but it is one of the major bottlenecks in maps and dashboards
            // do not enable it unless temporarily and *never* commit code with this ON
            /*result.setCalculations(result.toString() + "= ((" + 1 + "/"
                    + fromExchangeRate + ") * " + amount.toString() + " * " + toExchangeRate + ") " + date.toString());*/
            } else {
            result.setValue(amount);
            //result.setCalculations("No need it's due rate");
        }
        return result;
    }
    
    /**
     * Formats the amount to include commas and decimal places Commas will be
     * inserted after every three digits
     *
     * @param amt The amount value in String which is to be formatted
     * @return The formatted amount
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
        if ( tmp != null )  {
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
