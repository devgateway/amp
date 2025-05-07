package org.digijava.module.budget.form;

import org.apache.struts.action.ActionForm;

public class EditBudgetSectorForm extends ActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    private String budgetsectorname;
    private Long id;
    private String budgetsectorcode;
    
    
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
