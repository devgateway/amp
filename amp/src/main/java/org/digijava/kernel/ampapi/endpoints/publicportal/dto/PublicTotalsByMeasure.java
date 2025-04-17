package org.digijava.kernel.ampapi.endpoints.publicportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian de Anquin
 */
public class PublicTotalsByMeasure {

    @JsonProperty("currency")
    @ApiModelProperty(example = "USD")
    private String currency;

    @JsonProperty("total")
    @ApiModelProperty(example = "2222.235")
    private BigDecimal total;

    @JsonProperty("count")
    @ApiModelProperty(example = "2222")
    private Integer count;

    @JsonProperty("measure")
    @ApiModelProperty(example = "Actual Commitments")
    private Map<String, String> measure;

    public PublicTotalsByMeasure() {
        measure = new HashMap<>();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Map<String, String> getMeasure() {
        return measure;
    }

    public void setMeasure(Map<String, String> measure) {
        this.measure = measure;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
