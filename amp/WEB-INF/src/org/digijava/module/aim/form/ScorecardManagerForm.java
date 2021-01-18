package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class ScorecardManagerForm extends ActionForm {

    private static final long serialVersionUID = 1701410613054380912L;
    private Boolean validationPeriod;
    private Integer validationTime;
    private Double percentageThreshold = null;
    private String action;

    private String[] selectedCategoryValues;
    private Collection<AmpCategoryValue> categoryValues = null;
    private Set<AmpScorecardSettingsCategoryValue> closedStatuses;

    private List<String> quarters;

    private String[] selectedQuarters;
    
    public Boolean getValidationPeriod() {
        return validationPeriod;
    }

    public void setValidationPeriod(Boolean validationPeriod) {
        this.validationPeriod = validationPeriod;
    }

    public Integer getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(Integer validationTime) {
        this.validationTime = validationTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getPercentageThreshold() {
        return percentageThreshold;
    }

    public void setPercentageThreshold(Double percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    public Collection<AmpCategoryValue> getCategoryValues() {
        return categoryValues;
    }

    public void setCategoryValues(Collection<AmpCategoryValue> categoryValues) {
        this.categoryValues = categoryValues;
    }

    public Set<AmpScorecardSettingsCategoryValue> getClosedStatuses() {
        return closedStatuses;
    }

    public void setClosedStatuses(Set<AmpScorecardSettingsCategoryValue> closedStatuses) {
        this.closedStatuses = closedStatuses;
    }

    public String[] getSelectedCategoryValues() {
        return selectedCategoryValues;
    }

    public void setSelectedCategoryValues(String[] selectedCategoryValues) {
        this.selectedCategoryValues = selectedCategoryValues;
    }

    public List<String> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<String> quarters) {
        this.quarters = quarters;
    }

    public String[] getSelectedQuarters() {
        return selectedQuarters;
    }

    public void setSelectedQuarters(String[] selectedQuarters) {
        this.selectedQuarters = selectedQuarters;
    }
}
