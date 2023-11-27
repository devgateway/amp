package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

public class ReportsDashboard {
    private String donorAgency;
    private String pillar;
    private String country;
    private String year;
    private String implimentationLevel;
    private BigDecimal actualCommitment;
    private BigDecimal actualDisbursment;

    private String status;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getActualDisbursment() {
        return actualDisbursment;
    }

    public void setActualDisbursment(BigDecimal actualDisbursment) {
        this.actualDisbursment = actualDisbursment;
    }

    public String getImplimentationLevel() {
        return implimentationLevel;
    }

    public void setImplimentationLevel(String implimentationLevel) {
        this.implimentationLevel = implimentationLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
