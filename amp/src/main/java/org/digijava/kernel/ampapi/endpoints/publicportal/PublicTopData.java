package org.digijava.kernel.ampapi.endpoints.publicportal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class PublicTopData {
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicHeadersPH")
    private final Map<String, String> headers;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicTopTotalsPH")
    private final Map<String, BigDecimal> totals;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.PublicTopDataPH")
    private final List<Map<String, String>> data;

    @ApiModelProperty(example = "5")
    private final Integer count;

    @JsonProperty("numberformat")
    @ApiModelProperty(example = "###,###,###,###")
    private final String numberFormat;

    @JsonProperty("Currency")
    @ApiModelProperty(example = "USD")
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
