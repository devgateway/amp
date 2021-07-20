package org.digijava.kernel.ampapi.endpoints.publicportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

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
    private String measure;

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

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
