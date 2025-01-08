package org.digijava.module.aim.action.dataimporter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties("expenditures")
public class Funding {
    private Long funding_id;
    private Long donor_organization_id;
    private Object actual_start_date;
    private Object actual_completion_date;
    private Object original_completion_date;
    private Object reporting_date;
    private Set<Transaction> commitments= new HashSet<>();
    private Set<Transaction> disbursements= new HashSet<>();

    @JsonProperty("expenditures")
    private Set<Transaction> expenditures= new HashSet<>();

    private Long type_of_assistance;
    private Long financing_instrument;
    private Object funding_status;
    private long group_versioned_funding;
    private Long source_role;

    public Long getFunding_id() {
        return funding_id;
    }

    public void setFunding_id(Long funding_id) {
        this.funding_id = funding_id;
    }

    public Long getDonor_organization_id() {
        return donor_organization_id;
    }

    public void setDonor_organization_id(Long donor_organization_id) {
        this.donor_organization_id = donor_organization_id;
    }

    public Object getActual_start_date() {
        return actual_start_date;
    }

    public void setActual_start_date(Object actual_start_date) {
        this.actual_start_date = actual_start_date;
    }

    public Object getActual_completion_date() {
        return actual_completion_date;
    }

    public void setActual_completion_date(Object actual_completion_date) {
        this.actual_completion_date = actual_completion_date;
    }

    public Set<Transaction> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(Set<Transaction> expenditures) {
        this.expenditures = expenditures;
    }


    public Object getOriginal_completion_date() {
        return original_completion_date;
    }

    public void setOriginal_completion_date(Object original_completion_date) {
        this.original_completion_date = original_completion_date;
    }

    public Object getReporting_date() {
        return reporting_date;
    }

    public void setReporting_date(Object reporting_date) {
        this.reporting_date = reporting_date;
    }

    public Set<Transaction> getCommitments() {
        return commitments;
    }

    public void setCommitments(Set<Transaction> commitments) {
        this.commitments = commitments;
    }

    public Set<Transaction> getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(Set<Transaction> transactions) {
        this.disbursements = transactions;
    }

    public Long getType_of_assistance() {
        return type_of_assistance;
    }

    public void setType_of_assistance(Long type_of_assistance) {
        this.type_of_assistance = type_of_assistance;
    }

    public Long getFinancing_instrument() {
        return financing_instrument;
    }

    public void setFinancing_instrument(Long financing_instrument) {
        this.financing_instrument = financing_instrument;
    }

    public Object getFunding_status() {
        return funding_status;
    }

    public void setFunding_status(Object funding_status) {
        this.funding_status = funding_status;
    }

    public long getGroup_versioned_funding() {
        return group_versioned_funding;
    }

    public void setGroup_versioned_funding(long group_versioned_funding) {
        this.group_versioned_funding = group_versioned_funding;
    }

    public Long getSource_role() {
        return source_role;
    }

    public void setSource_role(Long source_role) {
        this.source_role = source_role;
    }

    @Override
    public String toString() {
        return "Funding{" +
                "funding_id=" + funding_id +
                ", donor_organization_id=" + donor_organization_id +
                ", actual_start_date=" + actual_start_date +
                ", actual_completion_date=" + actual_completion_date +
                ", original_completion_date=" + original_completion_date +
                ", reporting_date=" + reporting_date +
                ", commitments=" + commitments +
                ", disbursements=" + disbursements +
                ", type_of_assistance=" + type_of_assistance +
                ", financing_instrument=" + financing_instrument +
                ", funding_status=" + funding_status +
                ", group_versioned_funding=" + group_versioned_funding +
                ", source_role=" + source_role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Funding)) return false;
        Funding funding = (Funding) o;
        return Objects.equals(getDonor_organization_id(), funding.getDonor_organization_id()) && Objects.equals(getType_of_assistance(), funding.getType_of_assistance()) && Objects.equals(getFinancing_instrument(), funding.getFinancing_instrument()) && Objects.equals(getFunding_status(), funding.getFunding_status()) && Objects.equals(getSource_role(), funding.getSource_role());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDonor_organization_id(), getType_of_assistance(), getFinancing_instrument(), getFunding_status(), getSource_role());
    }

    // Getters and setters
}
