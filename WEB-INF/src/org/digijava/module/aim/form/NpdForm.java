package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.ActivityItem;

public class NpdForm extends ActionForm {

	private int mode;
	private boolean recursive;
	private int graphWidth;
	private int graphHeight;
	private Long programId;
	private long[] selIndicators;
	private String[] selYears;
	private List years;
	private List indicators;
	private String dummyYear;
	private List donors;
	private long[] selectedDonors;
	//private List statuses;
	//private String[] selectedStatuses;
	private Long selectedStatuses;

    private AmpCurrency defCurrency;

	private String[] yearTo;
	private String[] yearFrom;
	private Collection allThemes;
	private String defaultProgram;

	private Collection<ActivityItem> activities;
	private String actualSum;
	private String actualDisbSum;
	private String plannedCommSum;
	
	//activities part
	private String statusId;
	private String startYear;
	private String endYear;
	private String donorIds;
	
	public Collection getAllThemes() {
		return allThemes;
	}

	public void setAllThemes(Collection allThemes) {
		this.allThemes = allThemes;
	}

	public String[] getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(String[] yearFrom) {
		this.yearFrom = yearFrom;
	}

	public String[] getYearTo() {
		return yearTo;
	}

	public void setYearTo(String[] yearTo) {
		this.yearTo = yearTo;
	}

	public List getDonors() {
		return donors;
	}

	public void setDonors(List donors) {
		this.donors = donors;
	}

	public long[] getSelectedDonors() {
		return selectedDonors;
	}

	public void setSelectedDonors(long[] selectedDonors) {
		this.selectedDonors = selectedDonors;
	}

	public List getIndicators() {
		return indicators;
	}

	public void setIndicators(List indicators) {
		this.indicators = indicators;
	}

	public int getGraphWidth() {
		return graphWidth;
	}

	public void setGraphWidth(int graphWidth) {
		this.graphWidth = graphWidth;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public long[] getSelIndicators() {
		return selIndicators;
	}

	public void setSelIndicators(long[] selIndicators) {
		this.selIndicators = selIndicators;
	}

	public List getYears() {
		return years;
	}

	public void setYears(List years) {
		this.years = years;
	}

	public String[] getSelYears() {
		return selYears;
	}

	public void setSelYears(String[] selYears) {
		this.selYears = selYears;
	}

	public int getGraphHeight() {
		return graphHeight;
	}

	public void setGraphHeight(int graphHeight) {
		this.graphHeight = graphHeight;
	}

	public String getDummyYear() {
		return dummyYear;
	}

	public void setDummyYear(String dummyYear) {
		this.dummyYear = dummyYear;
	}

	public boolean getRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public Long getSelectedStatuses() {
		return selectedStatuses;
	}

    public String getDefaultProgram() {
        return defaultProgram;
    }

    public AmpCurrency getDefCurrency() {
        return defCurrency;
    }

    public void setSelectedStatuses(Long selectedStatuses) {
		this.selectedStatuses = selectedStatuses;
	}

    public void setDefaultProgram(String defaultProgram) {
        this.defaultProgram = defaultProgram;
    }

    public void setDefCurrency(AmpCurrency defCurrency) {
        this.defCurrency = defCurrency;
    }

	public Collection<ActivityItem> getActivities() {
		return activities;
	}

	public void setActivities(Collection<ActivityItem> activities) {
		this.activities = activities;
	}

	public String getActualSum() {
		return actualSum;
	}

	public void setActualSum(String actualSum) {
		this.actualSum = actualSum;
	}

	public String getActualDisbSum() {
		return actualDisbSum;
	}

	public void setActualDisbSum(String actualDisbSum) {
		this.actualDisbSum = actualDisbSum;
	}

	public String getPlannedCommSum() {
		return plannedCommSum;
	}

	public void setPlannedCommSum(String plannedCommSum) {
		this.plannedCommSum = plannedCommSum;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getDonorIds() {
		return donorIds;
	}

	public void setDonorIds(String donorIds) {
		this.donorIds = donorIds;
	}
}
