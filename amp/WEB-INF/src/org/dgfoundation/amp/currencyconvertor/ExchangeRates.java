package org.dgfoundation.amp.currencyconvertor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;


/**
 * class which holds a series of raw exchange rates between the same (pair, direction) of currencies. For example, GBP/USD (but not viceversa!)
 * @author Dolghier Constantin
 *
 */
public class ExchangeRates {
    
    /**
     * Map<day_code, exchange_rate>
     */
    protected TreeMap<Long, Double> rates = new TreeMap<>();
    
    public final long fromCurrId;
    public final long toCurrId;
    
    public ExchangeRates(long fromCurrId, long toCurrId) {
        this.fromCurrId = fromCurrId;
        this.toCurrId = toCurrId;
    }
    
    public void importRate(long date, double rate) {
        rates.put(date, rate);
    }

    public void importRates(Map<Long, Double> rates) {
        for(Map.Entry<Long, Double> entry : rates.entrySet()) {
            importRate(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * returns the rate closest to the requested date
     * returns null iff could not find anything (e.g. table empty)
     * @param date
     * @return
     */
    public DateRateInfo getRatesOnDate(long date) {
        if (rates.containsKey(date))
            return buildDateRateInfo(date, date);
        
        // gone till here -> we don't have an entry for this date, so trying to get the closest
        Long dateBefore = rates.floorKey(date);
        Long dateAfter = rates.ceilingKey(date);
        
        if (dateBefore == null && dateAfter == null) return null; // nothing found
        
        // gone till here -> at least one of the dates is nonnull
        if (dateAfter == null)
            return buildDateRateInfo(dateBefore, date);

        if (dateBefore == null)
            return buildDateRateInfo(dateAfter, date);
        
        // gone till here -> none of the dates is null
        DateRateInfo after = buildDateRateInfo(dateAfter, date);
        DateRateInfo before = buildDateRateInfo(dateBefore, date);
        if (after.minDateDelta < before.minDateDelta)
            return after;
        return before;
        
    }
    
    protected DateRateInfo buildDateRateInfo(long rateDate, long requestedDate) {
        return new DateRateInfo(requestedDate, rateDate - requestedDate, rates.get(rateDate));
    }
    
    /**
     * imports the rates from a ResultSet which yields entries of the type (long date, double rate)
     * @param rs
     */
    public void importRates(ResultSet rs) throws SQLException {
        while (rs.next()) {
            long date = rs.getLong(1);
            double rate = rs.getDouble(2);
            rates.put(date, rate);
        }
    }
    
    @Override public String toString() {
        return String.format("ExchangeRates for %d->%d: %s", this.fromCurrId, this.toCurrId, this.rates.toString());
    }
}
