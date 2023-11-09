package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

public class ReportsDashboard {
    private String donorAgency;
    private String pillar;
    private String year;
    private BigDecimal actualCommitment;

    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }

    public String getPillar() {
        return pillar;
    }

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public BigDecimal getActualCommitment() {
        return actualCommitment;
    }

    public void setActualCommitment(BigDecimal actualCommitment) {
        this.actualCommitment = actualCommitment;
    }
}
