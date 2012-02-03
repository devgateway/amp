package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:04 PM
 */
public class BEProjectForm extends ActionForm {
    private Long id;
    private String name;
    private String description;


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
}
