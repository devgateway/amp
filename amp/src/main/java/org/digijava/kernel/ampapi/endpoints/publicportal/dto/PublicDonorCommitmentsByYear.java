package org.digijava.kernel.ampapi.endpoints.publicportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PublicDonorCommitmentsByYear {

    @JsonProperty("year")
    @ApiModelProperty(example = "2025")
    private Integer year;

    @JsonProperty("currency")
    @ApiModelProperty(example = "USD")
    private String currency;

    @JsonProperty("total")
    @ApiModelProperty(example = "30480.2")
    private BigDecimal total = BigDecimal.ZERO;

    @JsonProperty("donorTotals")
    private List<PublicDonorCommitment> donorTotals = new ArrayList<>();

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public List<PublicDonorCommitment> getDonorTotals() {
        return donorTotals;
    }

    public void setDonorTotals(List<PublicDonorCommitment> donorTotals) {
        this.donorTotals = donorTotals;
    }
}
