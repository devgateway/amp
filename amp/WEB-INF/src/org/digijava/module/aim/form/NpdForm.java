package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpCurrency;

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

}
