package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class FundingTypeAmount {

    private Long id;

    private String type;

    private BigDecimal amount;

    private String formattedAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
