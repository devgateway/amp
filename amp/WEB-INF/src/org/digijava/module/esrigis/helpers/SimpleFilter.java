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
	private boolean onbudget;
	private String organizationgroup;
	private ArrayList<SimpleDonor> selecteddonors;
	private ArrayList<SimpleDonor> impselecteddonors;
	private String implementingagency; 
	private ArrayList<String> organizationtype;
	private ArrayList<String> sector;
	private String locationfiltered;
	
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

	public boolean isOnbudget() {
		return onbudget;
	}

	public void setOnbudget(boolean onbudget) {
		this.onbudget = onbudget;
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
	
}
