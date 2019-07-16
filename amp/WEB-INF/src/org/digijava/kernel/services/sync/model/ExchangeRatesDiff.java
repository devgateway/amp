package org.digijava.kernel.services.sync.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRatesForPair;
import org.digijava.kernel.ampapi.endpoints.util.ISO8601DateSerializer;

/**
 * @author Octavian Ciubotaru
 */
public class ExchangeRatesDiff {

    @JsonProperty("changed-dates")
    @JsonSerialize(contentUsing = ISO8601DateSerializer.class)
    private final List<Date> changedDates;

    @JsonProperty("exchange-rates-for-pairs")
    private final List<ExchangeRatesForPair> exchangeRatesForPairs;

    public ExchangeRatesDiff(List<Date> changedDates, List<ExchangeRatesForPair> exchangeRatesForPairs) {
        this.changedDates = changedDates;
        this.exchangeRatesForPairs = exchangeRatesForPairs;
    }

    public List<Date> getChangedDates() {
        return changedDates;
    }

    public List<ExchangeRatesForPair> getExchangeRatesForPairs() {
        return exchangeRatesForPairs;
    }
}
