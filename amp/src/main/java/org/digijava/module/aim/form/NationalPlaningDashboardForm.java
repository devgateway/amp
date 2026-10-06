package org.digijava.module.aim.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NationalPlaningDashboardForm extends ActionForm {

    private ArrayList programs;
    private AmpTheme currentProgram;
    private Long currentProgramId;
    private List activities;
    private String actionMethod;
    private boolean showChart;
    private Set indicators;
    private long[] selectedIndicators;
    private List valuesForSelectedIndicators;
    private int fromYear;
    private int toYear;
    private int fromyearActivities;
    private int toYearActivities;
    private Collection years;
    private long[] selectedDonors;
    private Collection donors;
    private String[] selectedStatuses;
    private Collection activityStatuses;
    private long[] selectedLocations;
    private Collection locations;
    private Collection programTree;
    private String xmlTree;
    private boolean showIndicators;
    private String fundingSum;
    private String[] selectedYears;
    
    private Collection allThemes;

    public String[] getSelectedYears() {
        return selectedYears;
    }

    public void setSelectedYears(String[] selectedYears) {
        this.selectedYears = selectedYears;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        currentProgramId = null;
        selectedIndicators = null;
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }

    public ArrayList getPrograms() {
        return programs;
    }

    public AmpTheme getCurrentProgram() {
        return currentProgram;
    }

    public Long getCurrentProgramId() {
        return currentProgramId;
    }

    public List getActivities() {
        return activities;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public long[] getSelectedIndicators() {
        return selectedIndicators;
    }

    public List getValuesForSelectedIndicators() {
        return valuesForSelectedIndicators;
    }

    public Collection getYears() {
        return years;
    }

    public Collection getDonors() {
        return donors;
    }

    public long[] getSelectedDonors() {
        return selectedDonors;
    }

    public String[] getSelectedStatuses() {
        return selectedStatuses;
    }

    public int getFromYear() {
        return fromYear;
    }

    public int getFromyearActivities() {
        return fromyearActivities;
    }

    public int getToYear() {
        return toYear;
    }

    public int getToYearActivities() {
        return toYearActivities;
    }

    public Collection getActivityStatuses() {
        return activityStatuses;
    }

    public Collection getLocations() {
        return locations;
    }

    public long[] getSelectedLocations() {
        return selectedLocations;
    }

    public Collection getProgramTree() {
        return programTree;
    }

    public String getXmlTree() {
        return xmlTree;
    }

    public void setPrograms(ArrayList programs) {
        this.programs = programs;
    }

    public void setCurrentProgram(AmpTheme currentProgram) {
        this.currentProgram = currentProgram;
    }

    public void setCurrentProgramId(Long currentProgramId) {
        this.currentProgramId = currentProgramId;
    }

    public void setActivities(List activities) {
        this.activities = activities;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public void setShowChart(boolean showChart) {
        this.showChart = showChart;
    }

    public void setSelectedIndicators(long[] selectedIndicators) {
        this.selectedIndicators = selectedIndicators;
    }

    public void setValuesForSelectedIndicators(List valuesForSelectedIndicators) {
        this.valuesForSelectedIndicators = valuesForSelectedIndicators;
    }

    public void setYears(Collection years) {
        this.years = years;
    }

    public void setDonors(Collection donors) {
        this.donors = donors;
    }

    public void setSelectedDonors(long[] selectedDonors) {
        this.selectedDonors = selectedDonors;
    }

    public void setSelectedStatuses(String[] selectedStatuses) {
        this.selectedStatuses = selectedStatuses;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public void setFromyearActivities(int fromyearActivities) {
        this.fromyearActivities = fromyearActivities;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public void setToYearActivities(int toYearActivities) {
        this.toYearActivities = toYearActivities;
    }

    public void setActivityStatuses(Collection activityStatuses) {
        this.activityStatuses = activityStatuses;
    }

    public void setLocations(Collection locations) {
        this.locations = locations;
    }

    public void setSelectedLocations(long[] selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public void setProgramTree(Collection programTree) {
        this.programTree = programTree;
    }

    public void setXmlTree(String xmlTree) {
        this.xmlTree = xmlTree;
    }

    public boolean isShowIndicators() {
        return showIndicators;
    }

    public void setShowIndicators(boolean showIndicators) {
        this.showIndicators = showIndicators;
    }

    public String getFundingSum() {
        return fundingSum;
    }

    public void setFundingSum(String fundingSum) {
        this.fundingSum=fundingSum;
    }
    
    public void setAllThemes(Collection allThemes) {
        this.allThemes  = allThemes;
    }
    public Collection getAllThemes () {
        return this.allThemes;
    }
    
    public Set getIndicators() {
        return indicators;
    }
    public void setIndicators(Set indicators) {
        this.indicators = indicators;
    }
}
