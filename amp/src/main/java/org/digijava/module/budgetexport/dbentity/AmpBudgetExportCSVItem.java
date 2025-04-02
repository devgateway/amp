package org.digijava.module.budgetexport.dbentity;

/**
 * User: flyer
 * Date: 2/6/12
 * Time: 4:44 PM
 */
public class AmpBudgetExportCSVItem {
    private Long id;
    private AmpBudgetExportMapRule rule;
    private String code;
    private String label;
    
    public AmpBudgetExportCSVItem(){}
    public AmpBudgetExportCSVItem(String code, String label, AmpBudgetExportMapRule rule){
        this.code = code;
        this.label = label;
        this.rule = rule;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpBudgetExportMapRule getRule() {
        return rule;
    }

    public void setRule(AmpBudgetExportMapRule rule) {
        this.rule = rule;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
