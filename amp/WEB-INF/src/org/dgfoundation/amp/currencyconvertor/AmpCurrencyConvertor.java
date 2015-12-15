package org.dgfoundation.amp.currencyconvertor;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * the AMP schema currency convertor. Thread-safe
 * @author Dolghier Constantin
 *
 */
public class AmpCurrencyConvertor implements CurrencyConvertor {
	
	protected ConcurrentHashMap<Long, OneCurrencyCalculator> currencyCalculators = new ConcurrentHashMap<>();
	
	/**
	 * the biggest event_id which has been factored in the state of the currencies calculator
	 */
	protected long lastCurrencyChangeFactoredIn = -1;

	private static AmpCurrencyConvertor instance = new AmpCurrencyConvertor();
	protected AmpCurrency baseCurrency = CurrencyUtil.getBaseCurrency();
	
	public static AmpCurrencyConvertor getInstance() {
		return instance;
	}
	
	protected void checkCache() {
		long lastCurrencyChange = PersistenceManager.getLong(
			PersistenceManager.getSession().createSQLQuery("select max(event_id) from amp_etl_changelog where entity_name = 'exchange_rate'").uniqueResult());
		
		if (lastCurrencyChange > this.lastCurrencyChangeFactoredIn) {
			currencyCalculators.clear();
			
		}
		this.lastCurrencyChangeFactoredIn = lastCurrencyChange;
	}
	
	/**
	 * returns a currency calculator; also adds it to the map if missing
	 * @param currency
	 * @return
	 */
	protected OneCurrencyCalculator getCalculator(long currencyId) {
		OneCurrencyCalculator res = currencyCalculators.get(currencyId);
		if (res != null)
			return res;
		
		OneCurrencyCalculator newRes = PersistenceManager.getSession().doReturningWork(conn -> new OneCurrencyCalculator(CurrencyUtil.getAmpcurrency(currencyId), conn));
		currencyCalculators.put(newRes.currency.getId(), newRes);
		return newRes;
	}
	
	@Override
	public double getExchangeRate(AmpCurrency fromCurrency, AmpCurrency toCurrency, Double fixedExchangeRate, LocalDate date) {
		if (fromCurrency == toCurrency)
			return 1d;
		
		checkCache();
		
		if (fixedExchangeRate != null && fixedExchangeRate > 0) {
			return getExchangeRate(baseCurrency, toCurrency, null, date) / fixedExchangeRate; 
		}
		
		long julianCode = DateTimeUtil.toJulianDayNumber(date);
		double res = getCalculator(fromCurrency.getId()).getRate(julianCode) / getCalculator(toCurrency.getId()).getRate(julianCode);
		return res;
	}
}
