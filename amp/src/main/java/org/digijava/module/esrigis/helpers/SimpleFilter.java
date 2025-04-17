package org.digijava.module.esrigis.helpers;

import java.util.ArrayList;

public class SimpleFilter {
    
    private String currency;
    private String startyear;
    private String endyear;
    private String fiscalcalendar;
    private ArrayList<String> projectstatus;
    private ArrayList<String> typeofassistance;
    private ArrayList<String> financinginstrument;
    private ArrayList<String> structuretypes;
    private String selectedBudget;
    private String organizationgroup;
    private ArrayList<SimpleDonor> selecteddonors;
    private ArrayList<SimpleDonor> impselecteddonors;
    private String implementingagency; 
    private ArrayList<String> organizationtype;
    private ArrayList<String> sector;
    private ArrayList <String> natplanobj;
    private ArrayList <String> primaryprograms;
    private ArrayList <String> secondaryprograms;
    private String locationfiltered;
    private Long selectedPeacebuildingMarkerId;
    
    public String getLocationfiltered() {
        return locationfiltered;
    }

    public void setLocationfiltered(String locationfiltered) {
        this.locationfiltered = locationfiltered;
    }

    public ArrayList<String> getSector() {
        return sector;
    }

    public void setSector(ArrayList<String> sector) {
        this.sector = sector;
    }

    public ArrayList<SimpleDonor> getSelecteddonors() {
        return selecteddonors;
    }

    public void setSelecteddonors(ArrayList<SimpleDonor> selecteddonors) {
        this.selecteddonors = selecteddonors;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStartyear() {
        return startyear;
    }

    public void setStartyear(String startyear) {
        this.startyear = startyear;
    }

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(String endyear) {
        this.endyear = endyear;
    }

    public String getFiscalcalendar() {
        return fiscalcalendar;
    }

    public void setFiscalcalendar(String fiscalcalendar) {
        this.fiscalcalendar = fiscalcalendar;
    }

    public ArrayList<String> getTypeofassistance() {
        return typeofassistance;
    }

    public void setTypeofassistance(ArrayList<String> typeofassistance) {
        this.typeofassistance = typeofassistance;
    }

    public ArrayList<String> getProjectstatus() {
        return projectstatus;
    }

    public void setProjectstatus(ArrayList<String> projectstatus) {
        this.projectstatus = projectstatus;
    }

    public ArrayList<String> getStructuretypes() {
        return structuretypes;
    }

    public ArrayList<String> getFinancinginstrument() {
        return financinginstrument;
    }

    public void setFinancinginstrument(ArrayList<String> financinginstrument) {
        this.financinginstrument = financinginstrument;
    }

    public void setStructuretypes(ArrayList<String> structuretypes) {
        this.structuretypes = structuretypes;
    }

    public String getSelectedBudget() {
        return selectedBudget;
    }

    public void setSelectedBudget(String selectedBudget) {
        this.selectedBudget = selectedBudget;
    }

    public String getOrganizationgroup() {
        return organizationgroup;
    }

    public ArrayList<String> getOrganizationtype() {
        return organizationtype;
    }

    public void setOrganizationtype(ArrayList<String> organizationtype) {
        this.organizationtype = organizationtype;
    }

    public void setOrganizationgroup(String organizationgroup) {
        this.organizationgroup = organizationgroup;
    }

    public String getImplementingagency() {
        return implementingagency;
    }

    public void setImplementingagency(String implementingagency) {
        this.implementingagency = implementingagency;
    }

    public ArrayList<SimpleDonor> getImpselecteddonors() {
        return impselecteddonors;
    }

    public void setImpselecteddonors(ArrayList<SimpleDonor> impselecteddonors) {
        this.impselecteddonors = impselecteddonors;
    }

    public Long getSelectedPeacebuildingMarkerId() {
        return selectedPeacebuildingMarkerId;
    }

    public void setSelectedPeacebuildingMarkerId(Long selectedPeacebuildingMarkerId) {
        this.selectedPeacebuildingMarkerId = selectedPeacebuildingMarkerId;
    }
    
  
    public ArrayList<String> getNatplanobj() {
        return natplanobj;
    }

    public void setNatplanobj(ArrayList<String> natplanobj) {
        this.natplanobj = natplanobj;
    }

    public ArrayList<String> getPrimaryprograms() {
        return primaryprograms;
    }

    public void setPrimaryprograms(ArrayList<String> primaryprograms) {
        this.primaryprograms = primaryprograms;
    }

    public ArrayList<String> getSecondaryprograms() {
        return secondaryprograms;
    }

    public void setSecondaryprograms(ArrayList<String> secondaryprograms) {
        this.secondaryprograms = secondaryprograms;
    }


}
