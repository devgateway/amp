package org.digijava.kernel.ampapi.endpoints.ndd;

import java.math.BigDecimal;

public class DetailByYear {

    private final String projectTitle;
    private final BigDecimal amount;

    public DetailByYear(String projectTitle, BigDecimal amount) {
        this.projectTitle = projectTitle;
        this.amount = amount;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
