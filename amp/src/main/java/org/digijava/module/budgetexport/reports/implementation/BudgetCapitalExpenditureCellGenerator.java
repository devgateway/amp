/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CapitalExpenditureCellGenerator;
import org.dgfoundation.amp.ar.MetaInfo;

/**
 * @author Alex Gartner
 *
 */
public class BudgetCapitalExpenditureCellGenerator extends
        CapitalExpenditureCellGenerator {

    ColWorkerInsider insider;
    HttpSession session;
    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public BudgetCapitalExpenditureCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
    }
    
    @Override
    public void setSession ( HttpSession session ) {
        this.insider    = ColWorkerInsider.getOrBuildInsider("v_capital_and_exp", "Capital Expenditure", session);
        this.session = session;
    }
    
    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {
        ArrayList<MetaInfo> ret = new ArrayList<MetaInfo>();
        String value            = this.insider.encoder.encode( "Expenditure" );
        MetaInfo mi             = new MetaInfo(ArConstants.COLUMN_CAPITAL_EXPENDITRURE, value );
        ret.add(mi);
        
        return ret;
    }
    

}
