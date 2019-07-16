package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class AidPredictabilityAmount {

    private BigDecimal amount;

    private String formattedAmount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFormattedAmount() {
        return formattedAmount;
    }

    public void setFormattedAmount(String formattedAmount) {
        this.formattedAmount = formattedAmount;
    }
}
