package org.digijava.module.aim.form;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;

public class NationalPlaningDashboardForm
    extends ActionForm {

    private ArrayList programs;
    private AmpTheme currentProgram;
    private Long currentProgramId;
    private ArrayList activities;
    private String actionMethod;
    private boolean showChart;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        currentProgramId = null;
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

    public ArrayList getActivities() {
        return activities;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public boolean isShowChart() {
        return showChart;
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

    public void setActivities(ArrayList activities) {
        this.activities = activities;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public void setShowChart(boolean showChart) {
        this.showChart = showChart;
    }
}
