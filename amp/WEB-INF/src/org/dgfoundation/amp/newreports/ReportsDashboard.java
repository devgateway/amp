package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

public class ReportsDashboard {
    private String donorAgency;
    private String pillar;
    private String implementationLevel;

    private String country;
    private String status;
    private String currency;
    private String year;
    private BigDecimal actualCommitment;
    private BigDecimal actualDisbursement;
    public ReportsDashboard() {
        this.actualCommitment = BigDecimal.ZERO;
        this.actualDisbursement = BigDecimal.ZERO;
    }


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

    public String getImplementationLevel() {
        return implementationLevel;
    }

    public void setImplementationLevel(String implementationLevel) {
        this.implementationLevel = implementationLevel;
    }

    public BigDecimal getActualDisbursement() {
        return actualDisbursement;
    }

    public void setActualDisbursement(BigDecimal actualDisbursement) {
        this.actualDisbursement = actualDisbursement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public void sumWith(ReportsDashboard other) {
        if (other.actualCommitment != null) {
            this.actualCommitment = this.actualCommitment.add(other.actualCommitment);
        }
        if (other.actualDisbursement != null) {
            this.actualDisbursement = this.actualDisbursement.add(other.actualDisbursement);
        }
    }
}
