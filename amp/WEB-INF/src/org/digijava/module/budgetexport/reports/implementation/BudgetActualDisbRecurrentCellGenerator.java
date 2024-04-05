/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.dgfoundation.amp.ar.ActualDisbRecurrentCellGenerator;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author alex
 *
 */
public class BudgetActualDisbRecurrentCellGenerator extends
        ActualDisbRecurrentCellGenerator {
    
    ColWorkerInsider insider;
    HttpSession session;

    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public BudgetActualDisbRecurrentCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
    }

    @Override
    public void setSession ( HttpSession session ) {
        this.insider    = ColWorkerInsider.getOrBuildInsider("v_mode_of_payment_capital_recurrent", ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT, session);
        this.session = session;
    }
    
    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {
        ArrayList<MetaInfo> ret = new ArrayList<MetaInfo>();
        String value            = this.insider.encoder.encode( "Recurrent" );
        MetaInfo mi             = new MetaInfo(ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT, value);
        ret.add(mi);
        
        return ret;
    }
}
