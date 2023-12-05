package org.dgfoundation.amp.currencyconvertor;

import java.time.LocalDate;

/**
 * a class which can convert amounts from one currency to another currency
 * @author Dolghier Constantin
 *
 */
public interface CurrencyConvertor {
    
    /**
     * returns the exchange rate from one currency to another on a given date. <br />

     * @param fromCurrency
     * @param toCurrency
     * @param fixedExchangeRate - in case this one is non-null and non-zero, then fromCurrency is converted to the AMP base currency instead at the given exchange rate
     * @param date
     * @return
     */
    public double getExchangeRate(String fromCurrencyCode, String toCurrencyCode, Double fixedExchangeRate, LocalDate date);
    
}
