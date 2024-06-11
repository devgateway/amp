package org.digijava.module.admin.util.model;

import java.util.Objects;

public class DonorOrganization {
    private Long id;
    private Long organization;
    private Double percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonorOrganization)) return false;
        DonorOrganization that = (DonorOrganization) o;
        return Objects.equals(getOrganization(), that.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganization());
    }

    @Override
    public String toString() {
        return "DonorOrganization{" +
                "id=" + id +
                ", organization=" + organization +
                ", percentage=" + percentage +
                '}';
    }

    // Getters and setters
}
