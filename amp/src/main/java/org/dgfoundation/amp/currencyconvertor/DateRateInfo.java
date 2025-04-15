package org.dgfoundation.amp.currencyconvertor;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * immutable class which holds data regarding some [currencies' pair] rate around a certain date
 * @author Dolghier Constantin
 *
 */
public class DateRateInfo {
    /**
     * the date for which a rate was requested
     */
    public final long requestedDate;
    
    /**
     * the delta, in days, between the requested date and the returned result - the minimum found in the DB is returned
     */
    public final long minDateDelta;
    
    /**
     * the exchange rate on the day of {@link #requestedDate} +- {@link #minDateDelta}
     */
    public final double rate;

    public DateRateInfo(long requestedDate, long minDateDelta, double rate) {
        this.requestedDate = requestedDate;
        this.minDateDelta = Math.abs(minDateDelta);
        this.rate = rate;
    }
    
    @Override public int hashCode() {
        return new HashCodeBuilder().append(requestedDate).append(minDateDelta).append(rate).toHashCode();
    }
    
    @Override public boolean equals(Object other) {
        DateRateInfo dri = (DateRateInfo) other;
        return this.requestedDate == dri.requestedDate && this.minDateDelta == dri.minDateDelta && Math.abs(this.rate - dri.rate) <= 0.00001;
    }

    @Override public String toString() {
        return String.format("(reqDate = %d, delta = %d, rate = %.5f)", requestedDate, minDateDelta, rate);
    }
}
