/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ActualDisbSplitCapRecTotalsCellGenerator;

/**
 * @author Alex
 *
 */
public class BudgetActualDisbSplitCapRecTotalsCellGenerator extends
        ActualDisbSplitCapRecTotalsCellGenerator {

    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public BudgetActualDisbSplitCapRecTotalsCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        
        this.capGenerator   = new BudgetActualDisbCapitalCellGenerator(metaDataName, measureName, originalMeasureName);
        this.recGenerator   = new BudgetActualDisbRecurrentCellGenerator(metaDataName, measureName, originalMeasureName);
    }
    
    @Override
    public void setSession ( HttpSession session ) {
        this.capGenerator.setSession(session);
        this.recGenerator.setSession(session);
    }

}
