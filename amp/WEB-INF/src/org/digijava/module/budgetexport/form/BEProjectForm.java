package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.digijava.module.budgetexport.util.AmpEntityNameIdPair;

import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:04 PM
 */
public class BEProjectForm extends ActionForm {
    private Long id;
    private String name;
    private String description;
    private Long selReport;
    private List<AmpEntityNameIdPair> availReports;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSelReport() {
        return selReport;
    }

    public void setSelReport(Long selReport) {
        this.selReport = selReport;
    }

    public List<AmpEntityNameIdPair> getAvailReports() {
        return availReports;
    }

    public void setAvailReports(List<AmpEntityNameIdPair> availReports) {
        this.availReports = availReports;
    }
}
