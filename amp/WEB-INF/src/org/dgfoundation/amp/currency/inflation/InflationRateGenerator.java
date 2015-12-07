/**
 * 
 */
package org.dgfoundation.amp.currency.inflation;

import java.util.Calendar;
import java.util.Date;
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
	
	protected Map<Long, Map<Date, Map<Date, Double>>> tempData = new TreeMap<Long, Map<Date, Map<Date, Double>>>();
	
	public InflationRateGenerator() {
	}
	
	/**
	 * @param from the date from which to calculate inflation rate
	 * @param to the date to which inflation rate must be detected
	 * @see #getInflationRateDeltaPartial(SortedMap)
	 */
	public double getInflationRateDeltaPartial(Date from, Date to, 
			SortedMap<Date, AmpInflationRate> sortedInflationRates) {
		if (from.after(to))
			throw new RuntimeException(
					String.format("'from' date must be no later that 'to' date, but 'from' = %s, 'to' = %s", from, to));
		// move to next day to make sure that only full periods are included
		Calendar c = Calendar.getInstance();
		c.setTime(from);
		c.add(Calendar.DATE, 1);
		from = c.getTime();
		c.setTime(to);
		c.add(Calendar.DATE, 1);
		to = c.getTime();
		sortedInflationRates = sortedInflationRates.subMap(from, to);
		if (sortedInflationRates.size() == 0)
			return 1d;
		// detect if it was generated before
		Long currId = sortedInflationRates.values().iterator().next().getCurrency().getAmpCurrencyId();
		Map<Date, Map<Date, Double>> availableData = tempData.get(currId);
		if (availableData == null) {
			availableData = new TreeMap<Date, Map<Date, Double>>();
			tempData.put(currId, availableData);
		}
		Map<Date, Double> fromMap = availableData.get(sortedInflationRates.firstKey());
		if (fromMap == null) {
			fromMap = new TreeMap<Date, Double>();
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
	public static final double getInflationRateDeltaPartial(SortedMap<Date, AmpInflationRate> sortedInflationRates) {
		double irc = 1d;
		for (Entry<Date, AmpInflationRate> entry : sortedInflationRates.entrySet()) {
			irc = irc * (entry.getValue().getInflationRate() / 100 + 1);
		}
		return irc;
	}

}
