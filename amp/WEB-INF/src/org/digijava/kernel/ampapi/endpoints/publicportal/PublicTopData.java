package org.digijava.kernel.ampapi.endpoints.publicportal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class PublicTopData {

    private final Map<String, String> headers;

    private final Map<String, BigDecimal> totals;

    private final List<Map<String, String>> topData;

    private final Integer count;

    @JsonProperty("numberformat")
    private final String numberFormat;

    @JsonProperty("Currency")
    private final String currency;

    PublicTopData(Map<String, String> headers, Map<String, BigDecimal> totals,
            List<Map<String, String>> topData, Integer count, String numberFormat, String currency) {
        this.headers = headers;
        this.totals = totals;
        this.topData = topData;
        this.count = count;
        this.numberFormat = numberFormat;
        this.currency = currency;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, BigDecimal> getTotals() {
        return totals;
    }

    public List<Map<String, String>> getTopData() {
        return topData;
    }

    public Integer getCount() {
        return count;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public String getCurrency() {
        return currency;
    }
}
