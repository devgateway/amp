package org.digijava.module.aim.helper;

/**
 * Created by acartaleanu on 10/24/15.
 */
public class BudgetStructure {

    private String budgetStructureName;
    private String budgetStructurePercentage;

    public BudgetStructure(String name, Float percentage) {
        this.budgetStructureName = name;
        this.budgetStructurePercentage = FormatHelper.formatPercentage(percentage);
    }
    public String getBudgetStructureName() {
        return budgetStructureName;
    }

    public String getBudgetStructurePercentage() {
        return budgetStructurePercentage;
    }
}
