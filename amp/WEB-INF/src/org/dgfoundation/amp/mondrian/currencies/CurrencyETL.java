package org.dgfoundation.amp.mondrian.currencies;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * an instance of this class runs the ETL for a certain currency against the [Base Currency], writing the results in the mondrian_exchange_rates DB table
 * uses both CURR->BASE and BASE->CURR exchanges in the process
 * the generates mondrian_exchange_rates table has the following meaning of a row with column values 
 * mer[day, currency_id] = exchange_rate means: on day <strong>day</strong>, one CURRENCY equaled <strong>exchange_rate</strong> BASE_CURRENCY'es
 * 
 * @author Dolghier Constantin
 *
 */
public class CurrencyETL {
	public final AmpCurrency currency;	
	public final Connection conn;
	public final AmpCurrency baseCurrency;
	
	public final static Double MINIMUM_EXCHANGE_RATE_VALUE = 0.0000000000001;
	public final static String SQL_FORMATTER = String.format("%%.%df", (int) (Math.log10(1 / MINIMUM_EXCHANGE_RATE_VALUE) + 1));
	
	/**
	 * usual constructor for production usage
	 * @param currency
	 * @param conn
	 */
	public CurrencyETL(AmpCurrency currency, Connection conn) {
		this(currency, CurrencyUtil.getAmpcurrency(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY)), conn);
	}

	/**
	 * the real constructor 
	 * @param currency
	 * @param baseCurrency
	 * @param conn
	 */
	private CurrencyETL(AmpCurrency currency, AmpCurrency baseCurrency, Connection conn) {
		this.currency = currency;
		this.conn = conn;
		this.baseCurrency = baseCurrency;
	}
		
	/**
	 * writes the exchange rates for all the dates specified (or maybe more)
	 * (day_code, currency_id, exchange_rate)
	 * @param allDates
	 * @throws SQLException
	 */
	public List<List<Object>> work(SortedSet<Long> allDates) throws SQLException {
		Map<Long, Double> values;
		
		if (baseCurrency.getAmpCurrencyId().equals(this.currency.getAmpCurrencyId())) {
			// exchange_rate [a,a] = 1.0 any time of the year
			values = new TreeMap<>();
			for (long date:allDates) {
				values.put(date, 1.0);
			}
		} else {
			ExchangeRates fromBase = importRates(conn, baseCurrency, currency); // 1 BASE = X currencies
			ExchangeRates fromCurrency = importRates(conn, currency, baseCurrency); // 1 currency = X bases
		
			values = computeCurrencyRates(allDates, fromBase, fromCurrency);
		}
		
		return serializeCurrencyRates(values);
	}
	
	/**
	 * (day_code, currency_id, exchange_rate)
	 * serializes currency rates to the database table mondrian_exchange_rates
	 * @param values
	 * @throws SQLException
	 */
	protected List<List<Object>> serializeCurrencyRates(Map<Long, Double> values) throws SQLException {
		List<List<Object>> res = new ArrayList<>();
		for (Entry<Long, Double> entry:values.entrySet()) {
			Double valueToWrite = Math.max(entry.getValue(), MINIMUM_EXCHANGE_RATE_VALUE);
			res.add(Arrays.<Object>asList(entry.getKey(), this.currency.getAmpCurrencyId(), valueToWrite));
		}
		return res;
	}
	
	/**
	 * uses two sources of exchange rates to compute the exchange rate (one X = [value] BASE_CURRENCIES)
	 * @param fromBase
	 * @param fromCurrency
	 * @return
	 */
	public static SortedMap<Long, Double> computeCurrencyRates(SortedSet<Long> allDates, ExchangeRates fromBase, ExchangeRates fromCurrency) {
		if (fromBase.fromCurrId != fromCurrency.toCurrId || fromBase.toCurrId != fromCurrency.fromCurrId)
			throw new RuntimeException(
					String.format("only allowed to confront direct and inverse rates, but instead got: (%d -> %d) vs (%d -> %d)", fromBase.fromCurrId, fromBase.toCurrId, fromCurrency.fromCurrId, fromCurrency.toCurrId));
		
		SortedMap<Long, Double> res = new TreeMap<>();
		for (long date:allDates) {
			DateRateInfo fromCurrencyRate = fromCurrency.getRatesOnDate(date);
			DateRateInfo fromBaseRate = fromBase.getRatesOnDate(date);
			double rate = chooseBestRate(fromCurrencyRate, fromBaseRate);
			res.put(date, rate);
		}
		return res;
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
		try(RsInfo rsi = SQLUtils.rawRunQuery(conn, 
				String.format("SELECT to_char(exchange_rate_date, 'J')::integer, exchange_rate FROM amp_currency_rate WHERE (exchange_rate IS NOT NULL) AND (exchange_rate > 0) AND (from_currency_code = '%s') and (to_currency_code='%s')", 
						fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode()), null)){
			rates.importRates(rsi.rs);
		}
		return rates;
	}
}
