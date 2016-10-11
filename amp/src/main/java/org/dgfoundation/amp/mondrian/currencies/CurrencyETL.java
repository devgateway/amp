package org.dgfoundation.amp.mondrian.currencies;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;

import org.dgfoundation.amp.currencyconvertor.OneCurrencyCalculator;
import org.digijava.module.aim.dbentity.AmpCurrency;


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
	public final Connection conn;
	public final OneCurrencyCalculator currencyCalculator;
	
	public final static Double MINIMUM_EXCHANGE_RATE_VALUE = 0.0000000000001;
	public final static String SQL_FORMATTER = String.format("%%.%df", (int) (Math.log10(1 / MINIMUM_EXCHANGE_RATE_VALUE) + 1));
	
	/**
	 * constructs an instance
	 * @param currency
	 * @param baseCurrency
	 * @param conn
	 */
	public CurrencyETL(AmpCurrency currency, Connection conn) {
		this.conn = conn;
		this.currencyCalculator = new OneCurrencyCalculator(currency, conn);
	}
		
	/**
	 * writes the exchange rates for all the dates specified (or maybe more)
	 * (day_code, currency_id, exchange_rate)
	 * @param allDates
	 * @throws SQLException
	 */
	public List<List<Object>> work(SortedSet<Long> allDates) throws SQLException {
		Map<Long, Double> values = new TreeMap<>();
		
		for(long date:allDates) {
			double value = currencyCalculator.getRate(date);
			values.put(date, value);
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
			res.add(Arrays.<Object>asList(entry.getKey(), this.currencyCalculator.currency.getAmpCurrencyId(), valueToWrite));
		}
		return res;
	}
}
