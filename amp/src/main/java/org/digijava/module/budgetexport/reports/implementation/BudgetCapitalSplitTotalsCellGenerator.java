/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.dgfoundation.amp.ar.CapitalSplitTotalsCellGenerator;

import javax.servlet.http.HttpSession;

/**
 * @author alex
 *
 */
public class BudgetCapitalSplitTotalsCellGenerator extends
        CapitalSplitTotalsCellGenerator {

    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public BudgetCapitalSplitTotalsCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        
        eGenerator  = new BudgetCapitalExpenditureCellGenerator(metaDataName, measureName,originalMeasureName);
        cGenerator  = new BudgetCapitalCellGenerator(metaDataName, measureName,originalMeasureName);
    }
    
    @Override
    public void setSession ( HttpSession session ) {
        eGenerator.setSession(session);
        cGenerator.setSession(session);
    }

}
