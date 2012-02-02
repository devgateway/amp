package org.digijava.module.budgetexport.dbentity;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:44 PM
 */
public class AmpBudgetExportMapItem {
    private Long id;
    private AmpBudgetExportMapRule rule;
    private String importedCode;
    private Long ampObjectID;

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

    public String getImportedCode() {
        return importedCode;
    }

    public void setImportedCode(String importedCode) {
        this.importedCode = importedCode;
    }

    public Long getAmpObjectID() {
        return ampObjectID;
    }

    public void setAmpObjectID(Long ampObjectID) {
        this.ampObjectID = ampObjectID;
    }
}
