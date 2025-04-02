package org.digijava.module.budgetexport.util;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ArConstants.SyntheticColumnsMeta;
import org.digijava.module.budgetexport.reports.implementation.BudgetActualDisbCapitalCellGenerator;
import org.digijava.module.budgetexport.reports.implementation.BudgetActualDisbRecurrentCellGenerator;
import org.digijava.module.budgetexport.reports.implementation.BudgetActualDisbSplitCapRecTotalsCellGenerator;
import org.digijava.module.budgetexport.reports.implementation.BudgetCapitalCellGenerator;
import org.digijava.module.budgetexport.reports.implementation.BudgetCapitalExpenditureCellGenerator;
import org.digijava.module.budgetexport.reports.implementation.BudgetCapitalSplitTotalsCellGenerator;

public class BudgetExportConstants {
    
    public final static String BUDGET_EXPORT_PROJECT_ID = "budget_export_project_id";
    
    public final static String BUDGET_EXPORT_TYPE   = "budget_export_type";
    
    public final static List<SyntheticColumnsMeta> syntheticColumns = Arrays.asList(
            new SyntheticColumnsMeta("Planned Disbursements - Capital", new BudgetCapitalCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements - Capital","Planned Disbursements")),
            new SyntheticColumnsMeta("Planned Disbursements - Expenditure", new BudgetCapitalExpenditureCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements - Expenditure","Planned Disbursements")),
            new SyntheticColumnsMeta("Planned Disbursements", new BudgetCapitalSplitTotalsCellGenerator(ArConstants.CAPITAL_PERCENT, "Planned Disbursements","Planned Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements - Capital", new BudgetActualDisbCapitalCellGenerator(ArConstants.MODE_OF_PAYMENT, "Actual Disbursements - Capital","Actual Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements - Recurrent", new BudgetActualDisbRecurrentCellGenerator(ArConstants.MODE_OF_PAYMENT, "Actual Disbursements - Recurrent","Actual Disbursements")),
            new SyntheticColumnsMeta("Actual Disbursements", new BudgetActualDisbSplitCapRecTotalsCellGenerator(ArConstants.MODE_OF_PAYMENT, "Actual Disbursements","Actual Disbursements"))

    ) ;
}
