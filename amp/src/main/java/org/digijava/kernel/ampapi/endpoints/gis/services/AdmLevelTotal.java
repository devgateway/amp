package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class AdmLevelTotal {

    @JsonProperty("admID")
    private String admId;

    private BigDecimal amount;

    public AdmLevelTotal(String admId, BigDecimal amount) {
        this.admId = admId;
        this.amount = amount;
    }

    public String getAdmId() {
        return admId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AdmLevelTotal{" +
                "admId='" + admId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
