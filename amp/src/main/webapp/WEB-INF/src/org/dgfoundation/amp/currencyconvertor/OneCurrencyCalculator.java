package org.dgfoundation.amp.currencyconvertor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * a class which knows how to calculate the exchange rates (base, X) for exactly one currency 
 * @author Dolghier Constantin
 *
 */
public class OneCurrencyCalculator {
    
    public final AmpCurrency currency;
        
    /**
     * (one X = [value] BASE_CURRENCIES)
     */
    protected final SortedMap<Long, Double> exchangeRates;
    
    /**
     * the real constructor 
     * @param currency
     * @param baseCurrency
     * @param conn
     */
    public OneCurrencyCalculator(AmpCurrency currency, Connection conn) {
        this(currency, init(currency, conn));
    }

    public OneCurrencyCalculator(AmpCurrency currency, Map<Long, Double> rates) {
        this.currency = currency;
        this.exchangeRates = new TreeMap<>(rates);
    }
    
    public OneCurrencyCalculator(ExchangeRates fromBase, ExchangeRates fromCurrency) {
        this(null, computeCurrencyRates(fromBase, fromCurrency));
    }
    
    protected static SortedMap<Long, Double> init(AmpCurrency currency, Connection conn) {
        AmpCurrency baseCurrency = CurrencyUtil.getAmpcurrency(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY));
        if (baseCurrency.getAmpCurrencyId().equals(currency.getAmpCurrencyId()))
            return new TreeMap<>();
        try {
            ExchangeRates fromBase = importRates(conn, baseCurrency, currency); // 1 BASE = X currencies
            ExchangeRates fromCurrency = importRates(conn, currency, baseCurrency); // 1 currency = X bases
            return computeCurrencyRates(fromBase, fromCurrency);
        }
        catch(SQLException e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    private static SortedMap<Long, Double> computeCurrencyRates(ExchangeRates fromBase, ExchangeRates fromCurrency) {
        Map<Long, Double> res = new HashMap<>();
        HashSet<Long> allDates = new HashSet<>(fromBase.rates.keySet());
        allDates.addAll(fromCurrency.rates.keySet());
        if (allDates.isEmpty())
            return new TreeMap<>(res);
        
        long minDayCode = Collections.min(allDates);
        long maxDayCode = Collections.max(allDates);
        for(long dayCode = minDayCode; dayCode <= maxDayCode; dayCode++) {
            DateRateInfo fromCurrencyRate = fromCurrency.getRatesOnDate(dayCode);
            DateRateInfo fromBaseRate = fromBase.getRatesOnDate(dayCode);
            double rate = chooseBestRate(fromCurrencyRate, fromBaseRate);
            res.put(dayCode, rate);
        }
        return new TreeMap<>(res);
    }
    
    public double getRate(long date) {
        if (exchangeRates.isEmpty())
            return 1.0;
        Double res = exchangeRates.get(date);
        if (res != null)
            return res;
        
        if (date < exchangeRates.firstKey())
            return exchangeRates.get(exchangeRates.firstKey());
        
        if (date > exchangeRates.lastKey())
            return exchangeRates.get(exchangeRates.lastKey());
        
        throw new RuntimeException("shouldn't get here");
    }
    
    /**
     * combines two sources of exchange rate (direct and inverse) to decide the best approximation of an exchange rate
     * @param directRate - the best approximation for a forward exchange rate
     * @param inverseRate - the best approximation for a backward exchange rate (1/[wanted_result])
     * @return
     */
    public static double chooseBestRate(DateRateInfo directRate, DateRateInfo inverseRate) {
        if (directRate == null && inverseRate == null) return 1.0;
        
        if (directRate == null)
            return 1 / inverseRate.rate;
        
        if (inverseRate == null)
            return directRate.rate;
        
        if (directRate.minDateDelta <= inverseRate.minDateDelta)
            return directRate.rate;
        
        return 1 / inverseRate.rate;
    }
    
    public static ExchangeRates importRates(java.sql.Connection conn, AmpCurrency fromCurrency, AmpCurrency toCurrency) throws SQLException {
        ExchangeRates rates = new ExchangeRates(fromCurrency.getAmpCurrencyId(), toCurrency.getAmpCurrencyId());
        try(org.dgfoundation.amp.ar.viewfetcher.RsInfo rsi = SQLUtils.rawRunQuery(conn, 
                String.format("SELECT to_char(exchange_rate_date, 'J')::integer, exchange_rate FROM amp_currency_rate WHERE (exchange_rate IS NOT NULL) AND (exchange_rate > 0) AND (from_currency_code = '%s') and (to_currency_code='%s')", 
                        fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode()), null)){
            rates.importRates(rsi.rs);
        }
        return rates;
    }
}
