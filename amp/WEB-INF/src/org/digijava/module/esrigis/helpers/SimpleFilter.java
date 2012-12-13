package org.digijava.module.esrigis.helpers;

import java.util.ArrayList;

public class SimpleFilter {
	
	private String currency;
	private String startyear;
	private String endyear;
	private String fiscalcalendar;
	private String projectstatus;
	private String typeofassistance;
	private String financinginstrument;
	private ArrayList<String> structuretypes;
	private boolean onbudget;
	private String organizationgroup;
	private ArrayList<SimpleDonor> selecteddonors;
	private ArrayList<SimpleDonor> impselecteddonors;
	private String implementingagency; 
	private String organizationtype;
	private ArrayList<String> regions;
	private ArrayList<String> zones;
	private String sector;
	
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

	public String getProjectstatus() {
		return projectstatus;
	}

	public void setProjectstatus(String projectstatus) {
		this.projectstatus = projectstatus;
	}

	public String getTypeofassistance() {
		return typeofassistance;
	}

	public void setTypeofassistance(String typeofassistance) {
		this.typeofassistance = typeofassistance;
	}

	public String getFinancinginstrument() {
		return financinginstrument;
	}

	public void setFinancinginstrument(String financinginstrument) {
		this.financinginstrument = financinginstrument;
	}

	public ArrayList<String> getStructuretypes() {
		return structuretypes;
	}

	public void setStructuretypes(ArrayList<String> structuretypes) {
		this.structuretypes = structuretypes;
	}

	public boolean isOnbudget() {
		return onbudget;
	}

	public void setOnbudget(boolean onbudget) {
		this.onbudget = onbudget;
	}

	public String getOrganizationtype() {
		return organizationtype;
	}

	public void setOrganizationtype(String organizationtype) {
		this.organizationtype = organizationtype;
	}

	public ArrayList<String> getRegions() {
		return regions;
	}

	public void setRegions(ArrayList<String> regions) {
		this.regions = regions;
	}

	public ArrayList<String> getZones() {
		return zones;
	}

	public void setZones(ArrayList<String> zones) {
		this.zones = zones;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getOrganizationgroup() {
		return organizationgroup;
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
	
}
