/**
 * 
 */
package org.dgfoundation.amp.currency.inflation;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.digijava.module.aim.dbentity.AmpInflationRate;

/**
 * Generates Inflation Rates 
 * 
 * @author Nadejda Mandrescu
 */
public class InflationRateGenerator {
    
    protected static final Logger logger = Logger.getLogger(InflationRateGenerator.class);
    protected static final long _1_DAY_TO_MS = TimeUnit.DAYS.toMillis(1);
    protected static final double EPSILON = Math.ulp(0d);
    // we set it to true during testing, and if ever really needed, can be used in prod
    public static boolean GENERATE_ALL = true;
    
    /** temporary cache {@code <from, to, inflation> }*/
    protected MultiKeyMap tempData = new MultiKeyMap();
    
    /** inflation sorted ascending by date */
    protected SortedMap<Long, Double> sortedInflationRates = new TreeMap<Long, Double>();
    
    /**
     * Inflation Rate Generator based on same currency inflation rates list
     * @param inflationRates
     */
    public InflationRateGenerator(List<AmpInflationRate> inflationRates) {
        validateAndInit(inflationRates);
    }
    
    private void validateAndInit(List<AmpInflationRate> rates) {
        Long currId = null;
        for (AmpInflationRate r : rates) {
            if (currId == null) {
                currId = r.getCurrency().getAmpCurrencyId();
            } else if (currId != r.getCurrency().getAmpCurrencyId()) {
                throw new RuntimeException("Cannot generate inflation rates: mixed currencies inflation rates");
            }
            sortedInflationRates.put(r.getPeriodStart().getTime(), r.getInflationRate());
        }
        init();
        sortedInflationRates = (SortedMap<Long, Double>) Collections.unmodifiableSortedMap(sortedInflationRates);
    }
    
    private void init() {
        /*
         * DEFLATOR: add "No inflation" period manually for now (until requirements keep changing)
         * Later, calculate it based on actual settings snapshot.
         * Until then: a) for 1 value series, approximate for now the default period to be 1 year 
         * b) otherwise detect consider first interval (we can also do average if ever needed)
         */
        if (sortedInflationRates.size() > 0 && 
                Math.abs(sortedInflationRates.get(sortedInflationRates.lastKey()) - 0d) > EPSILON) {
            long noInflationPeriodStart = 0;
            if (sortedInflationRates.size() == 1) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(sortedInflationRates.lastKey());
                c.add(Calendar.YEAR, 1);
                noInflationPeriodStart = c.getTimeInMillis();
            } else {
                Iterator<Long> iter = sortedInflationRates.keySet().iterator();
                noInflationPeriodStart = sortedInflationRates.lastKey() - iter.next() + iter.next();   
            }
            sortedInflationRates.put(noInflationPeriodStart, 0d);
        }
    }
    
    public MultiKeyMap getAllGeneratedInflationRates() {
        if (!GENERATE_ALL)
            throw new RuntimeException("Not allowed, was not configured to generated intermidate inflation rates");
        return tempData;
    }

    protected SortedMap<Long, Double> getSortedByDateInflationRates() {
        return sortedInflationRates;
    }
    
    /**
     * @param from the date from which to calculate inflation rate
     * @param to the date to which inflation rate must be detected
     * @see #getInflationRateDeltaPartial(SortedMap)
     */
    public double getInflationRate(Long from, Long to) {
        if (from > to)
            throw new RuntimeException(
                    String.format("'from' date must be no later that 'to' date, but 'from' = %s, 'to' = %s", from, to));
        /*
         * we have to remove (see design) inflation rate periods that 'from' and 'to' are making part of, therefore:
         * from + 1 day : to be sure the period is removed also if from == start period date
         * to + 2 days: if this is the last day of the period, then we have to take it into account
         * => try to include next period as well by adding 2 days (e.g. 31Dec + 2) and then explicitly remove 
         * the last period (e.g. the one starting on 1st of Jan, but keeping previous this way)
         * Note: it is based on assumption that we always have a "NoInflation" period (the least one to remove)
         */
        from += _1_DAY_TO_MS;
        to += 2 * _1_DAY_TO_MS;
        SortedMap<Long, Double> sir = sortedInflationRates.subMap(from, to);
        if (sir.size() > 0)
            sir.headMap(sir.lastKey());
        
        if (sir.size() == 0)
            return 1d;
        double inflation = GENERATE_ALL ? getPartialInflationBetweenAll(sir.firstKey(), sir.lastKey(), sir)
                : getPartialInflationBetween(sir);
        return inflation;
    }
    
    /**
     * <pre>
     * Generates the "product" part of the Inflation Rate Change formula:
     * (irc1/100 + 1) x (irc2/100 + 1) x ... x (ircn/100 + 1)
     * which still has to be converted to % change. Full formula:
     * IRCfrom-to = [ (irc1/100 + 1) x (irc2/100 + 1) x ... x (ircn/100 + 1) - 1] x 100
     * </pre>
     */
    protected double getPartialInflationBetween(SortedMap<Long, Double> sir) {
        double inflation = 1d;
        for (Double v : sir.values()) {
            inflation = inflation * (v/100 + 1);
        }
        return inflation;
    }
    
    /**
     * Generates inflation rates delta for all intermediate periods (for testing)
     * @see #getInflationBetween(Long, Long, SortedMap)
     */
    protected double getPartialInflationBetweenAll(Long firstPeriod, Long lastPeriod, SortedMap<Long, Double> sir) {
        if (firstPeriod == lastPeriod)
            return 1d;
        Double inflation = (Double) tempData.get(firstPeriod, lastPeriod);
        if (inflation == null) {
            // calculate and generate intermediate values as well for future
            SortedMap<Long, Double> subSir = sir.tailMap(firstPeriod + 1);
            double upperInflation = subSir.size() == 0 ? 1d: getPartialInflationBetweenAll(subSir.firstKey(), 
                    lastPeriod, subSir);
            inflation = (sir.get(firstPeriod) / 100 + 1) * upperInflation;
            tempData.put(firstPeriod, lastPeriod, inflation);
            // and other intermediate values generation
            subSir = sir.headMap(lastPeriod);
            if (subSir.size() > 0)
                getPartialInflationBetweenAll(firstPeriod, subSir.lastKey(), subSir);
        }
        return inflation;
    }
    
    public static SortedMap<String, Double> toDatePeriodStrRates(MultiKeyMap input) {
        SortedMap<String, Double> result = new TreeMap<String, Double>();
        for (MapIterator iter = input.mapIterator(); iter.hasNext(); ) {
            MultiKey inputKey = (MultiKey) iter.next();
            result.put(new Date((Long) inputKey.getKey(0)) + " to " + new Date((Long) inputKey.getKey(1)),
                    // and also convert to full exchange rate change between these 2 dates
                    AlgoUtils.keepNDecimals(((Double) iter.getValue() - 1) * 100, 6));
        }
        return result;
    }

}
