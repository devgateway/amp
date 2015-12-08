/**
 * 
 */
package org.dgfoundation.amp.currency.inflation;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpInflationRate;

/**
 * Generates Inflation Rates 
 * 
 * @author Nadejda Mandrescu
 */
public class InflationRateGenerator {
	protected static final Logger logger = Logger.getLogger(InflationRateGenerator.class);
	
	protected Map<Long, Map<Long, Map<Long, Double>>> tempData = new TreeMap<Long, Map<Long, Map<Long, Double>>>();
	
	public InflationRateGenerator() {
	}
	
	/**
	 * @param from the date from which to calculate inflation rate
	 * @param to the date to which inflation rate must be detected
	 * @see #getInflationRateDeltaPartial(SortedMap)
	 */
	public double getInflationRateDeltaPartial(Long from, Long to, 
			SortedMap<Long, AmpInflationRate> sortedInflationRates) {
		if (from > to)
			throw new RuntimeException(
					String.format("'from' date must be no later that 'to' date, but 'from' = %s, 'to' = %s", from, to));
		// move to next day to make sure that only full periods are included
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(from);
		c.add(Calendar.DATE, 1);
		from = c.getTimeInMillis();
		c.setTimeInMillis(to);
		c.add(Calendar.DATE, 1);
		to = c.getTimeInMillis();
		sortedInflationRates = sortedInflationRates.subMap(from, to);
		if (sortedInflationRates.size() == 0)
			return 1d;
		// detect if it was generated before
		Long currId = sortedInflationRates.values().iterator().next().getCurrency().getAmpCurrencyId();
		Map<Long, Map<Long, Double>> availableData = tempData.get(currId);
		if (availableData == null) {
			availableData = new TreeMap<Long, Map<Long, Double>>();
			tempData.put(currId, availableData);
		}
		Map<Long, Double> fromMap = availableData.get(sortedInflationRates.firstKey());
		if (fromMap == null) {
			fromMap = new TreeMap<Long, Double>();
			availableData.put(sortedInflationRates.firstKey(), fromMap);
		}
		Double value = fromMap.get(sortedInflationRates.lastKey());
		if (value == null) {
			value = getInflationRateDeltaPartial(sortedInflationRates);
			fromMap.put(sortedInflationRates.lastKey(), value);
		}
		return value;
	}
	
	/**
	 * <pre>
	 * Generates the "product" part of the Inflation Rate Change formula:
	 * (irc1/100 + 1) x (irc2/100 + 1) x ... x (ircn/100 + 1)
	 * which still has to be converted to % change. Full formula:
	 * IRCfrom-to = [ (irc1/100 + 1) x (irc2/100 + 1) x ... x (ircn/100 + 1) - 1] x 100
	 * </pre>
	 */
	public static final double getInflationRateDeltaPartial(SortedMap<Long, AmpInflationRate> sortedInflationRates) {
		double irc = 1d;
		for (Entry<Long, AmpInflationRate> entry : sortedInflationRates.entrySet()) {
			irc = irc * (entry.getValue().getInflationRate() / 100 + 1);
		}
		return irc;
	}

}
