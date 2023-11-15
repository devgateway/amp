package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpGPISurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrgType;

import java.util.Collection;
import java.util.Map;

public class ManageGPIForm extends ActionForm {

    private Map<String, String> measures;
    private String indicator5aActualDisbursement;
    private String indicator5aPlannedDisbursement;
    private String indicator6ScheduledDisbursements;
    private String indicator9bDisbursements;
    private Collection<AmpGPISurveyIndicator> indicators;
    private AmpGPISurveyIndicator indicator;
    private Collection<String> selectedDonorTypes;
    private Collection<AmpOrgType> availableDonorTypes; 

    public ManageGPIForm() {
    }

    public Map<String, String> getMeasures() {
        return measures;
    }

    public String getIndicator5aActualDisbursement() {
        return indicator5aActualDisbursement;
    }

    public void setIndicator5aActualDisbursement(
            String indicator5aActualDisbursement) {
        this.indicator5aActualDisbursement = indicator5aActualDisbursement;
    }

    public String getIndicator5aPlannedDisbursement() {
        return indicator5aPlannedDisbursement;
    }

    public void setIndicator5aPlannedDisbursement(
            String indicator5aPlannedDisbursement) {
        this.indicator5aPlannedDisbursement = indicator5aPlannedDisbursement;
    }

    public String getIndicator6ScheduledDisbursements() {
        return indicator6ScheduledDisbursements;
    }

    public void setIndicator6ScheduledDisbursements(
            String indicator6ScheduledDisbursements) {
        this.indicator6ScheduledDisbursements = indicator6ScheduledDisbursements;
    }

    public String getIndicator9bDisbursements() {
        return indicator9bDisbursements;
    }

    public void setIndicator9bDisbursements(String indicator9bDisbursements) {
        this.indicator9bDisbursements = indicator9bDisbursements;
    }

    public void setMeasures(Map<String, String> measures) {
        this.measures = measures;
    }

    public Collection<AmpGPISurveyIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(Collection<AmpGPISurveyIndicator> indicators) {
        this.indicators = indicators;
    }

    public AmpGPISurveyIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpGPISurveyIndicator indicator) {
        this.indicator = indicator;
    }

    public Collection<String> getSelectedDonorTypes() {
        return selectedDonorTypes;
    }

    public void setSelectedDonorTypes(Collection<String> selectedDonorTypes) {
        this.selectedDonorTypes = selectedDonorTypes;
    }

    public Collection<AmpOrgType> getAvailableDonorTypes() {
        return availableDonorTypes;
    }

    public void setAvailableDonorTypes(Collection<AmpOrgType> availableDonorTypes) {
        this.availableDonorTypes = availableDonorTypes;
    }

}
