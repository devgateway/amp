package org.digijava.kernel.ampapi.endpoints.currency.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.util.ISO8601DateSerializer;

/**
 * @author Octavian Ciubotaru
 */
public class ExchangeRate {

    @JsonSerialize(using = ISO8601DateSerializer.class)
    private Date date;

    private Double rate;

    public ExchangeRate(Date date, Double rate) {
        this.date = date;
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public Double getRate() {
        return rate;
    }
}
