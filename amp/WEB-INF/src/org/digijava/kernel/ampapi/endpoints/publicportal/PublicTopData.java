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

    private final List<Map<String, String>> data;

    private final Integer count;

    @JsonProperty("numberformat")
    private final String numberFormat;

    @JsonProperty("Currency")
    private final String currency;

    PublicTopData(Map<String, String> headers, Map<String, BigDecimal> totals,
            List<Map<String, String>> data, Integer count, String numberFormat, String currency) {
        this.headers = headers;
        this.totals = totals;
        this.data = data;
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

    public List<Map<String, String>> getData() {
        return data;
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
