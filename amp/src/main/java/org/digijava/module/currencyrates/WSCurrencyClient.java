package org.digijava.module.currencyrates;

import java.util.HashMap;
/**
 * 
 * @author Marcelo Sotero
 * 
 */
public interface WSCurrencyClient {
    public static final int CONNECTION_ERROR = -2;
    public static final int INVALID_CURRENCY_CODE = -1;

    HashMap<String, Double> getCRatesBasedUSD(String[] codeCurrencies);
    HashMap<String, Double> getCurrencyRates(String[] currencyCode, String baseCurrency) throws Exception;
    Double getCurrencyRateBasedUSD(String codeCurrency);
    void setUsername(String pUsername);
    void setPassword(String pPassword);
}
