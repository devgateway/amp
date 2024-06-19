package org.digijava.module.admin.util.model;


import java.util.Objects;

public class DonorOrganization extends Organization {
    private Double percentage;

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
                "id=" + this.getId() +
                ", organization=" + this.getOrganization() +
                ", percentage=" + percentage +
                '}';
    }

    // Getters and setters
}
