/**
 * 
 */
package org.dgfoundation.amp.ar;

import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author alex
 *
 */
public class ActualDisbRecurrentCellGenerator extends SyntheticCellGenerator {

    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public ActualDisbRecurrentCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.SyntheticCellGenerator#computeAmount(double, java.util.Set)
     */
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {
        
        
        MetaInfo<String> mi = metaData.getMetaInfo(this.getMetaDataName() );
        if (mi != null && (CategoryConstants.MODE_OF_PAYMENT_SALARIES_WAGES.getValueKey().equals( mi.getValue() ) || 
                CategoryConstants.MODE_OF_PAYMENT_GOODS_SERVICES.getValueKey().equals( mi.getValue())) ) {
            return originalAmount;
        }
            
        return 0d;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.SyntheticCellGenerator#syntheticMetaInfo()
     */
    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {
        ArrayList<MetaInfo> ret = new ArrayList<MetaInfo>();
        
        MetaInfo mi             = new MetaInfo(ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT, "Recurrent");
        ret.add(mi);
        
        return ret;
    }

}
