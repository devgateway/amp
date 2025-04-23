package org.digijava.module.gpi.model;

import org.digijava.module.aim.dbentity.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Collection;

public class GPIFilter {

    AmpFiscalCalendar calendar;
    AmpCurrency currency;
    Collection<AmpOrganisation> donors;
    Collection<AmpOrgGroup> donorGroups;
    Collection<AmpSector> sectors;
    Collection<AmpCategoryValue> statuses;
    Collection<AmpCategoryValue> financingInstruments;
    Collection<AmpOrgType> donorTypes;
    int startYear;
    int endYer;

    public AmpFiscalCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(AmpFiscalCalendar calendar) {
        this.calendar = calendar;
    }

    public AmpCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(AmpCurrency currency) {
        this.currency = currency;
    }

    public Collection<AmpOrganisation> getDonors() {
        return donors;
    }

    public void setDonors(Collection<AmpOrganisation> donors) {
        this.donors = donors;
    }

    public Collection<AmpOrgGroup> getDonorGroups() {
        return donorGroups;
    }

    public void setDonorGroups(Collection<AmpOrgGroup> donorGroups) {
        this.donorGroups = donorGroups;
    }

    public Collection<AmpSector> getSectors() {
        return sectors;
    }

    public void setSectors(Collection<AmpSector> sectors) {
        this.sectors = sectors;
    }

    public Collection<AmpCategoryValue> getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection<AmpCategoryValue> statuses) {
        this.statuses = statuses;
    }

    public Collection<AmpCategoryValue> getFinancingInstruments() {
        return financingInstruments;
    }

    public void setFinancingInstruments(Collection<AmpCategoryValue> financingInstruments) {
        this.financingInstruments = financingInstruments;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYer() {
        return endYer;
    }

    public void setEndYer(int endYer) {
        this.endYer = endYer;
    }

    public Collection<AmpOrgType> getDonorTypes() {
        return donorTypes;
    }

    public void setDonorTypes(Collection<AmpOrgType> donorTypes) {
        this.donorTypes = donorTypes;
    }

}
