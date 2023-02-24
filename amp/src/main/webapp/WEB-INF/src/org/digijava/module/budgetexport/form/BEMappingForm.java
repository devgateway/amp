package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;

import java.util.List;

/**
 * User: flyer
 * Date: 2/3/12
 * Time: 3:51 PM
 */
public class BEMappingForm extends ActionForm {
    Long id;
    Long ampReportId;
    List <AmpBudgetExportMapRule> rules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AmpBudgetExportMapRule> getRules() {
        return rules;
    }

    public void setRules(List<AmpBudgetExportMapRule> rules) {
        this.rules = rules;
    }

    /**
     * @return the ampReportId
     */
    public Long getAmpReportId() {
        return ampReportId;
    }

    /**
     * @param ampReportId the ampReportId to set
     */
    public void setAmpReportId(Long ampReportId) {
        this.ampReportId = ampReportId;
    }
    
}
