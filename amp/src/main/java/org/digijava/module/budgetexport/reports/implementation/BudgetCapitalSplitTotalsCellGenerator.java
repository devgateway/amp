/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.CapitalCellGenerator;
import org.dgfoundation.amp.ar.CapitalExpenditureCellGenerator;
import org.dgfoundation.amp.ar.CapitalSplitTotalsCellGenerator;

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
