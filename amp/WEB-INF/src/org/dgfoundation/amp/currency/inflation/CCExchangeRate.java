/**
 * 
 */
package org.dgfoundation.amp.currency.inflation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.dgfoundation.amp.mondrian.currencies.ExchangeRates;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.caching.AmpCaching;


/**
 * Generates Exchange Rates for Constant Currencies
 * @see https://wiki.dgfoundation.org/display/AMPDOC/Applying+Inflation+Rates
 * 
 * @author Nadejda Mandrescu
 */
public class CCExchangeRate {
	protected static final Logger logger = Logger.getLogger(CCExchangeRate.class);
	
	public final AmpCurrency baseCurrency;
	
	/** the Constant Currency for which to generate exchange rates */
	private ConstantCurrency cc;
	private AmpCurrency standardCurrencyReferredByCC;
	private InflationRateGenerator irg;
	private String inflationRatesCurrency;
	
	/** Exchange rates from the base currency to any other <date.getTime(), <other-currency-code, exchange-rate>> */
	private Map<String, Map<Long, Double>> fromBaseToOtherCurrency;
	
	private Map<String, SortedMap<Long, AmpInflationRate>> inflationRatesPerCurrency;
	
	public CCExchangeRate(InflationRateGenerator irg, 
			Map<String, SortedMap<Long, AmpInflationRate>> inflationRatesPerCurrency) {
		this.baseCurrency = CurrencyUtil.getDefaultCurrency();
		this.irg = irg;
		this.inflationRatesPerCurrency = inflationRatesPerCurrency;
		initExchanteRates();
	}
	
	/**
	 * Generate exchange rates for the given constant currency
	 * @param cc the constant currency
	 * @param inflationRatesPerCurrency all inflation rates available in the system
	 * @return list of exchange rates to the given constant currency
	 */
	public List<AmpCurrencyRate> generateExchangeRates(ConstantCurrency cc) {
		this.cc = cc;
		this.standardCurrencyReferredByCC = CurrencyUtil.getCurrencyByCode(cc.standardCurrencyCode);
		if (isValid()) {
			/* ------------------- TBD START ---------------------------------
			 * DEFLATOR: TBD if to limit constant currencies to be available only for the currency for which inflation rates are provided
			 *  => if so, then process only the appropriate list for the current constant currency
			 *  ------------------- TBD END --------------------------------- */
			
			SortedMap<Long, AmpInflationRate> inflationRates = inflationRatesPerCurrency.get(cc.standardCurrencyCode);
			// if there are inflation rates for the current currency
			// or if we need to propagate it from some other 
			if (inflationRates != null) {
				inflationRatesCurrency = cc.standardCurrencyCode;
			} else {
				// currently expected only 1 "other" currency inflation rates
				Entry<String , SortedMap<Long, AmpInflationRate>> first = inflationRatesPerCurrency.entrySet().iterator().next();
				inflationRatesCurrency = first.getKey();
				inflationRates = first.getValue();
			}
			
			Long periodEnd = getClosedPeriodEnd(inflationRates);
			
			// standard exchange rates from the base currency to the inflation rate currency
			Map<Long, Double> fromBaseRates = fromBaseToOtherCurrency.get(inflationRatesCurrency);
			ExchangeRates exchageRates = new ExchangeRates(baseCurrency.getAmpCurrencyId(), 
					inflationRates.entrySet().iterator().next().getValue().getCurrency().getAmpCurrencyId());
			exchageRates.importRate(fromBaseRates);
			/*
			 * also keep approximate exchange rates for start & end periods of given inflation rates,
			 * to minimize inflation rates impact for approximate exchange rates to the constant currency
			 */
			Calendar c = Calendar.getInstance();
			for (Long inflRatePeriodStart : inflationRates.keySet()) {
				for (int day = -1; day < 1; day ++) {
					c.setTimeInMillis(inflRatePeriodStart);
					c.add(Calendar.DATE, day);
					long date = c.getTime().getTime();
					if (!fromBaseRates.containsKey(date)) {
						fromBaseRates.put(date, exchageRates.getRatesOnDate(date).rate);
					}
				}
			}
			
			return generateExchangeRates(periodEnd, fromBaseRates, inflationRates);
		}
		return Collections.emptyList();
	}
	
	/**
	 * Generate exchange rates from the Base currency to the Constant Currency on all dates.
	 * The formula is:
	 * {@code
	 * <base-to-IRcurr-on-From-date> * <IRCfrom-to> * <IRcurr-to-base-on-To-date> * <base-to-CCstdCurr-on-To-date> 
	 * }
	 * @param periodEnd the period end of the constant inflation 
	 * @param fromBaseToInflCurrRates inflation rates from Base to Inflation Currency 
	 * @param inflationRates
	 */
	protected List<AmpCurrencyRate> generateExchangeRates(Long periodEnd, Map<Long, Double> fromBaseToInflCurrRates, 
			SortedMap<Long, AmpInflationRate> inflationRates) {
		List<AmpCurrencyRate> exchangeRatesToCC = new ArrayList<AmpCurrencyRate>();
		// check if inflation rate is provided for the base currency
		boolean isInflCurrBaseCurrency = baseCurrency.getCurrencyCode().equals(inflationRatesCurrency);
		 
		double iRcurrToBaseOnToDate = isInflCurrBaseCurrency ? 1.0 : 1 /
				getExchangeRate(fromBaseToOtherCurrency.get(inflationRatesCurrency), periodEnd);
		double baseToCCstdCurrOnToDate = baseCurrency.getCurrencyCode().equals(cc.standardCurrencyCode) ? 1.0 :
			getExchangeRate(fromBaseToOtherCurrency.get(cc.standardCurrencyCode), periodEnd);
		// inflation rate currency exchange rate to std currency of the constant currency on To date
		double iRCurrToCCstdCurrOnToDate = iRcurrToBaseOnToDate * baseToCCstdCurrOnToDate;
		
		Calendar c = Calendar.getInstance();
		Calendar debugTo = Calendar.getInstance();
		
		for (Entry<Long, Double> inflRatesCurrExchangeRates : fromBaseToInflCurrRates.entrySet()) {
			Long from = inflRatesCurrExchangeRates.getKey();
			Long to = periodEnd;
			double baseToIRCurrOnFromDate = isInflCurrBaseCurrency ? 1 : getExchangeRate(fromBaseToInflCurrRates, from);
			
			c.setTimeInMillis(from);
			System.out.println("From=" + c.getTime());
			debugTo.setTimeInMillis(to);
			System.out.println("To=" + debugTo.getTime());
			debugTo.setTimeInMillis(periodEnd);
			System.out.println("PeriodEnd=" + debugTo.getTime());
			System.out.println("inflationRates.size()=" +inflationRates.size());
			System.out.flush();
			
			boolean inverse = false;
			if (from > to) {
				to = from;
				from = periodEnd;
				inverse = true;
			}
			double irc = irg.getInflationRateDeltaPartial(from, to, inflationRates);
			if (inverse) {
				irc = 1 / irc;
			}
			
			irc = baseToIRCurrOnFromDate * irc * iRCurrToCCstdCurrOnToDate;
			
			AmpCurrencyRate acr = new AmpCurrencyRate();
			acr.setExchangeRateDate(c.getTime());
			acr.setFromCurrencyCode(baseCurrency.getCurrencyCode());
			acr.setToCurrencyCode(cc.currency.getCurrencyCode());
			acr.setExchangeRate(irc);
			exchangeRatesToCC.add(acr);
		}
		return exchangeRatesToCC;
	}
	
	protected double getExchangeRate(Map<Long, Double> exchangeRates, Long date) {
		ExchangeRates er = new ExchangeRates(0, 0);
		er.importRate(exchangeRates);
		return er.getRatesOnDate(date).rate;
	}
	
	/**
	 * @param reference the inflation rates reference
	 * @return
	 */
	protected Long getClosedPeriodEnd(SortedMap<Long, AmpInflationRate> reference) {
		// we do our exchange rates based on Gregorian, so we need to get Constant Currency period end in Gregorian
		Long periodEnd = FiscalCalendarUtil.toGregorianDate(cc.calendar, cc.year + 1, -1).getTime();
		// as agreed, we will approximate to the closes period end from existing inflation rates periods
		SortedMap<Long, AmpInflationRate> subMap = reference.headMap(periodEnd);
		Long prev = subMap.isEmpty() ? null : subMap.lastKey();
		subMap = reference.tailMap(periodEnd);
		Long next = subMap.isEmpty() ? null : subMap.firstKey();
		// we cannot decide to which end is closer if one is missing, so just keeping current end date (abnormal case)
		if (prev == null || next == null)
			return periodEnd;
		// detect closest start Period date
		if (periodEnd - prev < next - periodEnd) {
			periodEnd = prev;
		} else {
			periodEnd = next;
		}
		// adjust to previous period end date by deducting 1 day
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(periodEnd);
		c.add(Calendar.DATE, -1);
		return c.getTime().getTime();
	}
	
	protected boolean isValid() {
		if (inflationRatesPerCurrency.isEmpty()) {
			logger.error("No inflation rates available, cannot generate constant currency exchange rates");
		} if (this.cc == null || this.standardCurrencyReferredByCC == null) {
			logger.error("Cannot calculate exchange rate for " + this.cc);
		} else {
			return true;
		}
		return false;
	}
	
	
	protected void initExchanteRates() {
		fromBaseToOtherCurrency = new HashMap<String, Map<Long, Double>>();
		// also store exchange rates to the base currency itself, for dates reference
		Map<Long, Double> baseExchangeRates = new TreeMap<Long, Double>();
		fromBaseToOtherCurrency.put(baseCurrency.getCurrencyCode(), baseExchangeRates);
		
		List<AmpCurrencyRate> acr = CurrencyInflationUtil.getStandardExchangeRates(); 
		for (AmpCurrencyRate r : acr) {
			boolean isFromTheBaseCurrency = baseCurrency.getCurrencyCode().equals(r.getFromCurrencyCode());
			String otherCurrency = isFromTheBaseCurrency ? r.getToCurrencyCode() : r.getFromCurrencyCode();
			Map<Long, Double> exRates = fromBaseToOtherCurrency.get(otherCurrency);
			if (exRates == null) {
				exRates = new TreeMap<Long, Double>();
				fromBaseToOtherCurrency.put(otherCurrency, exRates);
			}
			Double value = isFromTheBaseCurrency ? r.getExchangeRate() : 1 / r.getExchangeRate();
			exRates.put(r.getExchangeRateDate().getTime(), value);
			baseExchangeRates.put(r.getExchangeRateDate().getTime(), 1.0);
		}
		
	}
	
	/**
	 * Regenerate exchange rates to all constant currencies
	 */
	public static void regenerateConstantCurrenciesExchangeRates(boolean calledFromQuartzJob) {
		CCExchangeRate ccER = new CCExchangeRate(new InflationRateGenerator(), 
				getInflRatesgroupByCurrencyCodeAndOrderByDate());
		List<ConstantCurrency> ccs = CurrencyInflationUtil.getAllConstantCurrencies();
		List<AmpCurrencyRate> newRates = new ArrayList<AmpCurrencyRate>();
		List<String> ccCodes = new ArrayList<String>();
		
		for (ConstantCurrency cc : ccs) {
			ccCodes.add(cc.currency.getCurrencyCode());
			newRates.addAll(ccER.generateExchangeRates(cc));
		}
		// cleanup all previous rates for these constant currencies
		CurrencyUtil.deleteCurrencyRates(ccCodes);
		PersistenceManager.getSession().flush();
		if (!calledFromQuartzJob) {
			AmpCaching.getInstance().currencyCache.reset();
		}
		try {
			for (AmpCurrencyRate acr: newRates) {
				PersistenceManager.getSession().save(acr);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	protected static Map<String, SortedMap<Long, AmpInflationRate>> getInflRatesgroupByCurrencyCodeAndOrderByDate() {
		Map<String, SortedMap<Long, AmpInflationRate>> inflRates = 
				new HashMap<String, SortedMap<Long, AmpInflationRate>>();
		for (AmpInflationRate air : CurrencyInflationUtil.getInflationRates()) {
			String currCode = air.getCurrency().getCurrencyCode();
			SortedMap<Long, AmpInflationRate> rates = inflRates.get(currCode);
			if (rates == null) {
				rates = new TreeMap<Long, AmpInflationRate>();
				inflRates.put(currCode, rates);
			}
			rates.put(air.getPeriodStart().getTime(), air);
		}
		return inflRates;
	}

}
