package org.digijava.module.budget.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budget.dbentity.AmpBudgetSector;

public class BudgetManagerForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<AmpBudgetSector> budgetsectors;
    private Long[] selectedsectors;
    private String budgetsectorname;
    private String budgetsectorcode;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBudgetsectorcode() {
        return budgetsectorcode;
    }

    public void setBudgetsectorcode(String budgetsectorcode) {
        this.budgetsectorcode = budgetsectorcode;
    }

    

    public String getBudgetsectorname() {
        return budgetsectorname;
    }

    public void setBudgetsectorname(String budgetsectorname) {
        this.budgetsectorname = budgetsectorname;
    }

    public Long[] getSelectedsectors() {
        return selectedsectors;
    }

    public void setSelectedsectors(Long[] selectedsectors) {
        this.selectedsectors = selectedsectors;
    }

    public ArrayList<AmpBudgetSector> getBudgetsectors() {
        return budgetsectors;
    }

    public void setBudgetsectors(ArrayList<AmpBudgetSector> budgetsectors) {
        this.budgetsectors = budgetsectors;
    }

}
