package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

/**
 * Action for for Graphs on NPD
 * @author Irakli Kobiashvili - ikobiashvili@picktek.com
 *
 */
public class NpdGraphForm extends ActionForm {
    private Long currentProgramId;
    private long[] selectedIndicators;
    private String[] selectedYears;
    private Long timestamp;

    
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String[] getSelectedYears() {
        return selectedYears;
    }

    public void setSelectedYears(String[] selectedYears) {
        this.selectedYears = selectedYears;
    }

    public long[] getSelectedIndicators() {
        return selectedIndicators;
    }

    public void setSelectedIndicators(long[] selectedIndicators) {
        this.selectedIndicators = selectedIndicators;
    }

    public Long getCurrentProgramId() {
        return currentProgramId;
    }

    public void setCurrentProgramId(Long currentProgramId) {
        this.currentProgramId = currentProgramId;
    }

}
