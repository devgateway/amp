package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class ProjectAmount {

    private Long id;

    private String name;

    private BigDecimal amount;

    private String formattedAmount;

    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
