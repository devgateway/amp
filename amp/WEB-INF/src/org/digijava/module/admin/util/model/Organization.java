package org.digijava.module.admin.util.model;

public class Organization {
    private Long id;
    private Long organization;
    private Double percentage;
    private String internal_id;

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getInternal_id() {
        return internal_id;
    }

    public void setInternal_id(String internal_id) {
        this.internal_id = internal_id;
    }


    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

