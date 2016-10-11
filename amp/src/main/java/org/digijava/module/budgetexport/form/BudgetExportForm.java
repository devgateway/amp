/**
 * 
 */
package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;

import java.util.List;

/**
 * @author Alex
 *
 */
public class BudgetExportForm extends ActionForm {
    List<AmpBudgetExportProject> projects;

    public List<AmpBudgetExportProject> getProjects() {
        return projects;
    }

    public void setProjects(List<AmpBudgetExportProject> projects) {
        this.projects = projects;
    }
}
