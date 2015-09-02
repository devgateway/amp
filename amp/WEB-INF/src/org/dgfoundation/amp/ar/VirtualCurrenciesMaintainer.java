package org.dgfoundation.amp.ar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.caching.AmpCaching;

/**
 * This class is used for doing maintenance of virtual currencies <br />
 * Because this routine is called rarely (realistically once per day) and it processes small amounts of data (under 1k rows written to amp_currency_rate), 
 * it is not terribly optimized (it is not incremental and does not do any fingerprinting)
 * @author Dolghier Constantin
 *
 */
public class VirtualCurrenciesMaintainer {
	
	protected static Logger logger = Logger.getLogger(VirtualCurrenciesMaintainer.class);
	public final static int NR_DIGITS = 6;
	
	public void work() {
		List<AmpCurrency> baseCurrencies = PersistenceManager.getSession().createQuery("SELECT DISTINCT ir.baseCurrency FROM " + AmpInflationRate.class.getName() + " ir WHERE ir.constantCurrency IS TRUE").list();
		for(AmpCurrency baseCurrency:baseCurrencies) {
			List<AmpInflationRate> rates = PersistenceManager.getSession()
					.createQuery("SELECT ir FROM " + AmpInflationRate.class.getName() + " ir WHERE (ir.year between :start and :end) AND ir.baseCurrency.ampCurrencyId = " + baseCurrency.getAmpCurrencyId() + " ORDER BY ir.year")
					.setInteger("start", AmpInflationRate.MIN_DEFLATION_YEAR)
					.setInteger("end", AmpInflationRate.MAX_DEFLATION_YEAR)
					.list();
			logger.warn("redoing virtual currencies based on " + baseCurrency + ", the inflation data entries are: " + rates);
			createVirtualCurrenciesBasedOn(rates, baseCurrency);
			AmpCaching.getInstance().currencyCache.reset();
		}
	}
	
	public void createVirtualCurrenciesBasedOn(List<AmpInflationRate> rates, AmpCurrency baseCurrency) {
		if (!baseCurrency.equals(AmpARFilter.getDefaultCurrency())) {
			throw new RuntimeException("using non-base-currencies as a base for Constant currencies not implemented yet");
		}
				
		int baseYear = clamp(rates.isEmpty() ? 2010 : rates.get(0).getYear() - 1, 2000, 2010);
		SortedMap<Integer, Double> priceIndices = computePriceIndices(rates, baseYear);
		
		Set<String> relevantVCurrencyCodes = new HashSet<>();
		for(AmpInflationRate rate:rates) {
			if (!rate.isConstantCurrency())
				continue;
			AmpCurrency virtualCurrency = ensureVirtualCurrencyExists(rate);
			
			SortedMap<Integer, Double> rebasedIndices = rebasePriceIndices(priceIndices, baseYear, rate.getYear());
			List<AmpCurrencyRate> ratesToWrite = computeRatesToWrite(virtualCurrency, baseCurrency, rebasedIndices);			
			
			saveExchangeRates(virtualCurrency, ratesToWrite);
			relevantVCurrencyCodes.add(virtualCurrency.getCurrencyCode().toLowerCase());
		}
		markDisappearedCurrenciesAsUnavailable(rates, baseCurrency, relevantVCurrencyCodes);
	}
	
	public void markDisappearedCurrenciesAsUnavailable(List<AmpInflationRate> rates, AmpCurrency baseCurrency, Set<String> relevantVCurrencyCodes) {
		List<AmpCurrency> virtCurrencies = PersistenceManager.getSession().createQuery("FROM " + AmpCurrency.class.getName() + " ac WHERE ac.virtual IS TRUE").list();
		for(AmpCurrency curr:virtCurrencies) {
			if (curr.isVirtual() && curr.getCurrencyCode().toLowerCase().startsWith(baseCurrency.getCurrencyCode().toLowerCase()) && !relevantVCurrencyCodes.contains(curr.getCurrencyCode().toLowerCase())) {
				deleteCurrencyIfPossible(curr);
			}
		}
		AmpCaching.getInstance().currencyCache.reset();
	}
	
	/**
	 * delete a currency IFF it is not referenced anywhere in the DB (for example, through saved reports)
	 * @param curr
	 * @throws SQLException 
	 */
	protected void deleteCurrencyIfPossible(AmpCurrency curr) {
		logger.info("marking virtual currency " + curr + " as deleted, because it has not been requested as a deflated currency anymore");
		curr.setActiveFlag(0);
		PersistenceManager.getSession().flush();
// 		code below commented out because: 1. it's not very smart  2. it sometimes leads to PostgreSQL hanging the thread on DELETE		
//		try(java.sql.Connection conn = PersistenceManager.getJdbcConnection()) {
//			SQLUtils.executeQuery(conn, "DELETE FROM amp_currency WHERE amp_currency_id = " + curr.getAmpCurrencyId());
//		}
//		catch(Exception e) {
//			// do nothing - we tried to delete currency and failed
//			logger.error("could not delete currency " + curr + ", probably used in some saved report(s)");
//		}
	}
	
	protected void saveExchangeRates(AmpCurrency virtualCurrency, List<AmpCurrencyRate> ratesToWrite) {
		PersistenceManager.getSession().createQuery("DELETE FROM " + AmpCurrencyRate.class.getName() + " acr WHERE acr.fromCurrencyCode=:currCode OR acr.toCurrencyCode=:currCode")
			.setString("currCode", virtualCurrency.getCurrencyCode())
			.executeUpdate();
		PersistenceManager.getSession().flush();

		for(AmpCurrencyRate exchange:ratesToWrite) {
			PersistenceManager.getSession().save(exchange);
		}
		PersistenceManager.getSession().flush();
	}
	
	/**
	 * given as an input a Map<Year, price_index> generates a list of AmpCurrencyRates to be written to the DB <br />
	 * 4 entries are generated per year-info: (1st_january, 31th_december) x (direct_exchange, inverse_exchange)
	 * @param virtualCurrency
	 * @param baseCurrency
	 * @param rebasedIndices
	 * @return
	 */
	public static List<AmpCurrencyRate> computeRatesToWrite(AmpCurrency virtualCurrency, AmpCurrency baseCurrency, SortedMap<Integer, Double> rebasedIndices) {
		List<AmpCurrencyRate> ratesToWrite = new ArrayList<>();
		
		for(int year:rebasedIndices.keySet()) {
			Date yearStartDate = new GregorianCalendar(year, 0, 1, 3, 0).getTime();
			Date yearEndDate = new GregorianCalendar(year, 11, 31, 21, 0).getTime();
			double fromBaseToConstantRate = AlgoUtils.keepNDecimals(rebasedIndices.get(year), NR_DIGITS);
			double fromConstantToBaseRate = AlgoUtils.keepNDecimals(1.0 / fromBaseToConstantRate, NR_DIGITS);
			
			ratesToWrite.addAll(Arrays.asList(
					new AmpCurrencyRate(virtualCurrency.getCurrencyCode(), baseCurrency.getCurrencyCode(), fromBaseToConstantRate, yearStartDate, 3),
					new AmpCurrencyRate(virtualCurrency.getCurrencyCode(), baseCurrency.getCurrencyCode(), fromBaseToConstantRate, yearEndDate, 3)));
			
			ratesToWrite.addAll(Arrays.asList(
					new AmpCurrencyRate(baseCurrency.getCurrencyCode(), virtualCurrency.getCurrencyCode(), fromConstantToBaseRate, yearStartDate, 3),
					new AmpCurrencyRate(baseCurrency.getCurrencyCode(), virtualCurrency.getCurrencyCode(), fromConstantToBaseRate, yearEndDate, 3)));
		}
//		logger.error("processing " + virtualCurrency.toString() + ", list of added stuff");
//		for(AmpCurrencyRate acr:ratesToWrite) {
//			logger.error(String.format("\t[%s, %s -> %s, %.5f]", acr.getExchangeRateDate(), acr.getFromCurrencyCode(), acr.getToCurrencyCode(), acr.getExchangeRate()));
//		}
		return ratesToWrite;
	}
	
	/**
	 * uses an input of inflation rates to compute the values for ALL the years between 1970 and 2050
	 * @param rates
	 * @param baseYear
	 * @return
	 */
	public static SortedMap<Integer, Double> computePriceIndices(List<AmpInflationRate> rates, int baseYear) {
		int minYear = rates.isEmpty() ? baseYear : rates.get(0).getYear();
		int maxYear = rates.isEmpty() ? baseYear : rates.get(rates.size() - 1).getYear();
		
		for(int i = 1; i < rates.size() - 1; i++)
			if (!rates.get(i - 1).getBaseCurrency().equals(rates.get(i).getBaseCurrency()))
				throw new RuntimeException("all the inflation rates given to computePriceIndices should have the same base currency!");
		
		SortedMap<Integer, Double> inflRates = new TreeMap<>();
		for(AmpInflationRate rate:rates)
			inflRates.put(rate.getYear(), rate.getInflationRate());
		
		SortedMap<Integer, Double> res = new TreeMap<>();
		res.put(baseYear, 1.0); // by definition, since we are computing all the other prices related to this one
		for(int yr = baseYear + 1; yr <= maxYear; yr++) {
			double inflation = inflRates.containsKey(yr) ? inflRates.get(yr) : 0;
			res.put(yr, AlgoUtils.keepNDecimals(res.get(yr - 1) * (1 + inflation / 100), NR_DIGITS));
		}
		for(int yr = baseYear - 1; yr >= minYear; yr--) {
			double inflation = inflRates.containsKey(yr) ? inflRates.get(yr) : 0;
			res.put(yr, AlgoUtils.keepNDecimals(res.get(yr + 1) / (1 + inflation / 100), NR_DIGITS));
		}
		return res;
	}
	
	/**
	 * rebases a price index from one year to another 
	 * @param priceIndices
	 * @param baseYear
	 * @param newBaseYear
	 * @return
	 */
	public static SortedMap<Integer, Double> rebasePriceIndices(SortedMap<Integer, Double> priceIndices, int baseYear, int newBaseYear) {
		if (!priceIndices.containsKey(newBaseYear)) {
			throw new RuntimeException("could not rebase constant currency to year " + newBaseYear);
		}
		double newBaseYearPriceIndex = priceIndices.get(newBaseYear);
		SortedMap<Integer, Double> res = new TreeMap<>();
		for(int yr:priceIndices.keySet()) {
			double newPriceIndex = AlgoUtils.keepNDecimals(priceIndices.get(yr) / newBaseYearPriceIndex, NR_DIGITS);
			res.put(yr, newPriceIndex);
		}
		return res;
	}

	/**
	 * ensures that, for a list of AmpInflationRate instances all linked to the same base (real) currency, all of them have a corresponding
	 * @param rates
	 * @param baseCurrency
	 * @return
	 */
	protected AmpCurrency ensureVirtualCurrencyExists(AmpInflationRate rate) {
		String newCurrencyCode = buildConstantCurrencyCode(rate.getBaseCurrency(), rate.getYear());
		AmpCurrency newCurr = CurrencyUtil.getCurrencyByCode(newCurrencyCode);
		if (newCurr != null)
			return newCurr;

		newCurr = createVirtualCurrency(rate);
		PersistenceManager.getSession().save(newCurr);
		return newCurr;
	}
	
	/**
	 * does NOT save to the DB. Use {@link #ensureVirtualCurrencyExists(AmpInflationRate)} if you want persistence
	 * @return
	 */
	protected AmpCurrency createVirtualCurrency(AmpInflationRate rate) {
		AmpCurrency newCurr = new AmpCurrency();
		newCurr.setActiveFlag(1);
		newCurr.setCountryLocation(rate.getBaseCurrency().getCountryLocation());
		newCurr.setCountryName(rate.getBaseCurrency().getCountryName());
		newCurr.setVirtual(true);
		newCurr.setCurrencyCode(buildConstantCurrencyCode(rate.getBaseCurrency(), rate.getYear()));
		newCurr.setCurrencyName(buildConstantCurrencyName(rate.getBaseCurrency(), rate.getYear()));
		return newCurr;
	}
	
	protected String buildConstantCurrencyName(AmpCurrency baseCurrency, int year) {
		return String.format("Constant %d %s", year, baseCurrency.getCurrencyCode());
	}
	
	protected String buildConstantCurrencyCode(AmpCurrency baseCurrency, int year) {
		return String.format("%s%d", baseCurrency.getCurrencyCode(), year);
	}
	
	/**
	 * returns the input value, unless it is smaller than a given minvalue or bigger than a given maxvalue
	 * @param a
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(int a, int min, int max) {
		if (a < min) return min;
		if (a > max) return max;
		return a;
	}

}
