package org.digijava.kernel.ampapi.endpoints.publicportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class PublicDonorReportingSystemCommitment {

    @JsonProperty("donorId")
    @ApiModelProperty(example = "43")
    private Long donorId;

    @JsonProperty("reportingSystemId")
    @ApiModelProperty(example = "12")
    private Long reportingSystemId;

    @JsonProperty("total")
    @ApiModelProperty(example = "1085.4")
    private BigDecimal total;

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Long getReportingSystemId() {
        return reportingSystemId;
    }

    public void setReportingSystemId(Long reportingSystemId) {
        this.reportingSystemId = reportingSystemId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}