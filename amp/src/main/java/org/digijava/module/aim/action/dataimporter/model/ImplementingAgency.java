package org.digijava.module.aim.action.dataimporter.model;

import java.util.Objects;

public class ImplementingAgency extends Organization {

    private Float percentage;

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImplementingAgency)) return false;
        ImplementingAgency that = (ImplementingAgency) o;
        return Objects.equals(getOrganization(), that.getOrganization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganization());
    }

    @Override
    public String toString() {
        return "ImplementingAgency{" +
                "id=" + this.getId() +
                ", organization=" + this.getOrganization() +
                ", percentage=" + percentage +
                '}';
    }

}
