package org.digijava.kernel.ampapi.endpoints.ndd;

import java.math.BigDecimal;

public class DetailByYear {

    private final Long id;
    private final String projectTitle;
    private final BigDecimal amount;

    public DetailByYear(Long id, String projectTitle, BigDecimal amount) {
        this.projectTitle = projectTitle;
        this.amount = amount;
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }
}
