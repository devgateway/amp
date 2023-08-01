/**
 * 
 */
package org.dgfoundation.amp.currency.inflation;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.currency.ConstantCurrency;
import org.dgfoundation.amp.currency.CurrencyInflationUtil;
import org.dgfoundation.amp.currencyconvertor.ExchangeRates;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpCurrencyRate;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpInflationRate;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.hibernate.Session;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


/**
 * Generates Exchange Rates for Constant Currencies
 * @see https://wiki.dgfoundation.org/display/AMPDOC/Applying+Inflation+Rates
 * 
 * @author Nadejda Mandrescu
 */
public class CCExchangeRate {
    protected static final Logger logger = Logger.getLogger(CCExchangeRate.class);
    
    /** 1 quarter in ms (approximate quarter to 90 days) */
    protected static final long _1_QUARTER_TO_MS = 90* TimeUnit.DAYS.toMillis(1);
    protected static final long _1_DAY_TO_MS = TimeUnit.DAYS.toMillis(1);
    protected static final long LOWEST_PERIOD_MS = _1_QUARTER_TO_MS;
    
    public final AmpCurrency baseCurrency;
    
    /** the Constant Currency for which to generate exchange rates */
    private ConstantCurrency cc;
    private AmpCurrency standardCurrencyReferredByCC;
    private Map<String, InflationRateGenerator> irgPerCurrency = new HashMap<String, InflationRateGenerator>();
    private String inflationRatesCurrency;
    
    /** Exchange rates from the base currency to any other <date.getTime(), <other-currency-code, exchange-rate>> */
    private Map<String, Map<Long, Double>> fromBaseToOtherCurrency;
    
    private Map<String, List<AmpInflationRate>> inflationRatesPerCurrency;
    
    public CCExchangeRate(List<AmpInflationRate> inflationRates) {
        this.baseCurrency = CurrencyUtil.getDefaultCurrency();
        this.inflationRatesPerCurrency = groupByCurrencyCode(inflationRates);
        initExchanteRates();
    }
    
    public Map<String, InflationRateGenerator> getIrgPerCurrency() {
        return irgPerCurrency;
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
            
            List<AmpInflationRate> inflationRates = getInflationRates();
            InflationRateGenerator irg = getInflationRateGenerator(inflationRatesCurrency, inflationRates);
            Long periodEnd = getClosedPeriodEnd(irg.getSortedByDateInflationRates());
            
            return generateExchangeRates(periodEnd, irg);
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
    protected List<AmpCurrencyRate> generateExchangeRates(Long periodEnd, InflationRateGenerator irg) {
        List<AmpCurrencyRate> exchangeRatesToCC = new ArrayList<AmpCurrencyRate>();
        // check if inflation rate is provided for the base currency
        boolean isInflCurrBaseCurrency = baseCurrency.getCurrencyCode().equals(inflationRatesCurrency);
         
        double iRcurrToBaseOnToDate = isInflCurrBaseCurrency ? 1.0 : 1 /
                getExchangeRate(fromBaseToOtherCurrency.get(inflationRatesCurrency), periodEnd);
        double baseToCCstdCurrOnToDate = baseCurrency.getCurrencyCode().equals(cc.standardCurrencyCode) ? 1.0 :
            getExchangeRate(fromBaseToOtherCurrency.get(cc.standardCurrencyCode), periodEnd);
        // inflation rate currency exchange rate to std currency of the constant currency on To date
        double iRCurrToCCstdCurrOnToDate = iRcurrToBaseOnToDate * baseToCCstdCurrOnToDate;
        
        ExchangeRates fromBaseToInflCurrRates = new ExchangeRates(baseCurrency.getAmpCurrencyId(), -1);
        fromBaseToInflCurrRates.importRates(fromBaseToOtherCurrency.get(inflationRatesCurrency));
        
        Calendar c = Calendar.getInstance();
        SortedSet<Long> ccExchangeRatesDates = generateCCExchangeRateDates(irg.getSortedByDateInflationRates().keySet());
        for (Long from : ccExchangeRatesDates) {
            Long to = periodEnd;
            double baseToIRCurrOnFromDate = isInflCurrBaseCurrency ? 1 : fromBaseToInflCurrRates.getRatesOnDate(from).rate;
            
            c.setTimeInMillis(from);
            
            boolean inverse = false;
            if (from > to) {
                to = from;
                from = periodEnd;
                inverse = true;
            }
            double irc = irg.getInflationRate(from, to);
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
        er.importRates(exchangeRates);
        return er.getRatesOnDate(date).rate;
    }
    
    /**
     * <pre>
     * Build CC exchange rate dates:
     * 1) for the currency in which we have inflation rates: use only inflation rate dates
     * 2) for other currencies to which we propagate inflation rate via exchange rate:
     *  a) for no exchange rates periods: all dates for available inflation rates, 
     *      e.g. if we have exchange rates between 2010-2015 and quarterly inflation rates between 1970-2015, 
     *      then we will consider the dates 1970-01-01,1970-04-01, ..., 2009-10-01
     *  
     *  b) for infl. rates period that has exchange rates to CC std currency: 
     *   i) keep approximate exchange rates for start & end periods, to minimize inflation rates impact 
     *   for approximate exchange rates to the constant currency 
     *   ii) build up at least quarterly intervals when infl.rate period is higher (e.g. yearly), 
     *   to be sure to reflect the other currency inflation somehow reflected via exchange rate
     * 
     * NOTE:
     * Keep in mind that taking all suitable exchange rate dates for accuracy will end up with AMP-21954
     * </pre>
     * @return sorted dates for the new exchange rates to be generated for the current CC  
     */
    protected SortedSet<Long> generateCCExchangeRateDates(Set<Long> inflationDates) {
        // 1)
        SortedSet<Long> dates = new TreeSet<Long>(inflationDates);
        // 2)
        if (dates.size() > 0 && !this.inflationRatesCurrency.equals(this.standardCurrencyReferredByCC)) {
            SortedSet<Long> availableExchRateDates = new TreeSet<Long>();
            if (fromBaseToOtherCurrency.containsKey(inflationRatesCurrency))
                availableExchRateDates.addAll(fromBaseToOtherCurrency.get(this.inflationRatesCurrency).keySet());
            if (fromBaseToOtherCurrency.containsKey(standardCurrencyReferredByCC))
                availableExchRateDates.addAll(fromBaseToOtherCurrency.get(this.standardCurrencyReferredByCC).keySet());
            SortedSet<Long> allDates = new TreeSet<Long>(availableExchRateDates);
            allDates.addAll(dates);
            Long[] inflDates = dates.toArray(new Long[dates.size()]);
            for (int i = 0; i + 1 < inflDates.length; i++) {
                Long from = inflDates[i] + 1;
                Long to = inflDates[i + 1];
                SortedSet<Long> fromTo = availableExchRateDates.subSet(from, to);
                if (fromTo.size() > 0) {
                    // add last exchange rate from current period
                    do {
                        dates.add(fromTo.last());
                        // and repeat as long as there are lower periods with exchange rates
                        fromTo = fromTo.subSet(from, Math.max(from, Math.min(to - LOWEST_PERIOD_MS, fromTo.last())));
                    } while (fromTo.size() > 0 && from + LOWEST_PERIOD_MS < fromTo.last());
                }
            }
        }
        return dates;
    }
    
    /**
     * @param reference the inflation rates reference
     * @return
     */
    protected Long getClosedPeriodEnd(SortedMap<Long, Double> reference) {
        // we do our exchange rates based on Gregorian, so we need to get Constant Currency period end in Gregorian
        Long periodEnd = FiscalCalendarUtil.toGregorianDate(cc.calendar, cc.year + 1, -1).getTime();
        // as agreed, we will approximate to the closest period end from existing inflation rates periods
        SortedMap<Long, Double> subMap = reference.headMap(periodEnd);
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
        return periodEnd - _1_DAY_TO_MS;
    }
    
    protected boolean isValid() {
        if (inflationRatesPerCurrency.isEmpty()) {
            logger.error("No inflation rates available, cannot generate constant currency exchange rates for " + this.cc);
        } else if (this.cc == null || this.standardCurrencyReferredByCC == null) {
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
            if (!isFromTheBaseCurrency && !baseCurrency.getCurrencyCode().equals(r.getToCurrencyCode())) {
                /*
                 * DEFLATOR: skipping any other to other currencies now, until the feature requirements are stable
                 * Out of scope here: we need Currency Rates Manager to workaround this 
                 * and provide propagated exchange rates for periods when a different base currency was used
                 */
                continue;
            }
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
    
    private List<AmpInflationRate> getInflationRates() {
        List<AmpInflationRate> inflationRates = inflationRatesPerCurrency.get(cc.standardCurrencyCode);
        // if there are inflation rates for the current currency or if we need to propagate it from some other 
        if (inflationRates != null) {
            inflationRatesCurrency = cc.standardCurrencyCode;
        } else {
            // currently expected only 1 "other" currency inflation rates
            Entry<String , List<AmpInflationRate>> first = inflationRatesPerCurrency.entrySet().iterator().next();
            inflationRatesCurrency = first.getKey();
            inflationRates = first.getValue();
        }
        return inflationRates;
    }
    
    private InflationRateGenerator getInflationRateGenerator(String currencyCode, 
            List<AmpInflationRate> inflationRates) {
        InflationRateGenerator irg = irgPerCurrency.get(currencyCode);
        if (irg == null) {
            irg = new InflationRateGenerator(inflationRates);
            irgPerCurrency.put(currencyCode, irg);
        }
        return irg;
    }
    
    /**
     * Regenerate exchange rates to all constant currencies
     * 
     * @param calledFromQuartzJob flag to decide the calling environment
     */
    public static void regenerateConstantCurrenciesExchangeRates(boolean calledFromQuartzJob) {
        regenerateConstantCurrenciesExchangeRates(calledFromQuartzJob, null);
    }
    
    /**
     * Regenerate exchange rates to all constant currencies
     * 
     * @param calledFromQuartzJob flag to decide the calling environment
     * @param calendar the fiscal calendar for which Constant Currency rates must be updated
     *                 or null if all constant currencies must be updated 
     */
    public static synchronized void regenerateConstantCurrenciesExchangeRates(boolean calledFromQuartzJob, 
            AmpFiscalCalendar cal) {
        logger.debug("regenerateConstantCurrenciesExchangeRates START");
        CCExchangeRate ccER = new CCExchangeRate(CurrencyInflationUtil.getInflationRates());
        logger.debug("after CurrencyInflationUtil.getInflationRates()");
        // limit the constant currencies to regenerate to calendar if specified
        List<ConstantCurrency> ccs = cal == null ? CurrencyInflationUtil.getAllConstantCurrencies() :
            CurrencyInflationUtil.wrap(cal.getConstantCurrencies());
        List<AmpCurrencyRate> newRates = new ArrayList<AmpCurrencyRate>();
        List<String> ccCodes = new ArrayList<String>();
        
        logger.debug("before Actual regeneration process");
        for (ConstantCurrency cc : ccs) {
            ccCodes.add(cc.currency.getCurrencyCode());
            newRates.addAll(ccER.generateExchangeRates(cc));
        }
        logger.debug("after Actual regeneration process");
        // cleanup all previous rates for these constant currencies
        CurrencyUtil.deleteCurrencyRates(ccCodes);
        logger.debug("after deleting old currency rates");
        if (!calledFromQuartzJob) {
            AmpCaching.getInstance().currencyCache.reset();
        }
        try {
            Session session = PersistenceManager.getSession();
            for (AmpCurrencyRate acr: newRates) {
                session.save(acr);
            }
            logger.debug("after saving new currency rates");
            session.flush();
            logger.debug("regenerateConstantCurrenciesExchangeRates END");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        
    }
    
    protected Map<String, List<AmpInflationRate>> groupByCurrencyCode(List<AmpInflationRate> inflationRates) {
        Map<String, List<AmpInflationRate>> inflRates = new HashMap<String, List<AmpInflationRate>>();
        for (AmpInflationRate air : inflationRates) {
            String currCode = air.getCurrency().getCurrencyCode();
            List<AmpInflationRate> rates = inflRates.get(currCode);
            if (rates == null) {
                rates = new ArrayList<AmpInflationRate>();
                inflRates.put(currCode, rates);
            }
            rates.add(air);
        }
        return inflRates;
    }

}
