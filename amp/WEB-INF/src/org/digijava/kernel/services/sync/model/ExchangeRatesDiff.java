package org.digijava.kernel.services.sync.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
