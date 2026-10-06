package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

public class ReportsDashboard {
    private String donorAgency;
    private String implementingAgency;
    private String responsibleOrganization;
    private String pillar;
    private String implementationLevel;
    private String procurementSystem;
    private String location;
    private String status;
    private String reportingSystem;
    private String typeOfAssistance;
    private String currency;
    //private String year;
    private BigDecimal actualCommitment;
    private BigDecimal actualDisbursement;
    private Long activityCount;
    // Comma separated AMP activity IDs aggregated under the current hierarchy path
    private String activityIds;
    private String projectTitle;
    // Translations for field labels: List of maps, each map contains "label" key and locale codes as keys
    private java.util.List<java.util.Map<String, String>> translations;
    private String secondarySector;

    public ReportsDashboard() {
        this.actualCommitment = BigDecimal.ZERO;
        this.actualDisbursement = BigDecimal.ZERO;
        this.activityCount = 0L;
    }


    public String getDonorAgency() {
        return donorAgency;
    }

    public void setDonorAgency(String donorAgency) {
        this.donorAgency = donorAgency;
    }

    public String getImplementingAgency() {
        return implementingAgency;
    }

    public void setImplementingAgency(String implementingAgency) {
        this.implementingAgency = implementingAgency;
    }

    public String getPillar() {
        return pillar;
    }

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public String getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(String activityIds) {
        this.activityIds = activityIds;
    }

    /*
        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    */
    public BigDecimal getActualCommitment() {
        return actualCommitment;
    }

    public void setActualCommitment(BigDecimal actualCommitment) {
        this.actualCommitment = actualCommitment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getReportingSystem() {
        return reportingSystem;
    }

    public void setReportingSystem(String reportingSystem) {
        this.reportingSystem = reportingSystem;
    }

    public String getTypeOfAssistance() {
        return typeOfAssistance;
    }

    public void setTypeOfAssistance(String typeOfAssistance) {
        this.typeOfAssistance = typeOfAssistance;
    }

    public Long getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Long activityCount) {
        this.activityCount = activityCount;
    }
    public String getProcurementSystem() {
        return procurementSystem;
    }
    public void setProcurementSystem(String procurementSystem) {
        this.procurementSystem = procurementSystem;
    }

    public String getResponsibleOrganization() {
        return responsibleOrganization;
    }

    public void setResponsibleOrganization(String responsibleOrganization) {
        this.responsibleOrganization = responsibleOrganization;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public java.util.List<java.util.Map<String, String>> getTranslations() {
        return translations;
    }

    public void setTranslations(java.util.List<java.util.Map<String, String>> translations) {
        this.translations = translations;
    }

    public String getSecondarySector() {
        return secondarySector;
    }

    public void setSecondarySector(String secondarySector) {
        this.secondarySector = secondarySector;
    }
}
