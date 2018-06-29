package org.digijava.kernel.ampapi.endpoints.currency.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ExchangeRatesForPair {

    @JsonProperty("currency-pair")
    private final CurrencyPair currencyPair;

    @ApiModelProperty("List of exchange rates")
    private final List<ExchangeRate> rates;

    public ExchangeRatesForPair(CurrencyPair currencyPair, List<ExchangeRate> rates) {
        this.currencyPair = currencyPair;
        this.rates = rates;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public List<ExchangeRate> getRates() {
        return rates;
    }
}
