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
public class ActualDisbCapitalCellGenerator extends SyntheticCellGenerator {

    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public ActualDisbCapitalCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.SyntheticCellGenerator#computeAmount(double, java.util.Set)
     */
    @Override
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {
        
        
        MetaInfo<String> mi = metaData.getMetaInfo(this.getMetaDataName() );
        if (mi != null && (CategoryConstants.MODE_OF_PAYMENT_CAPITAL_DEVELOPMENT.getValueKey().equals( mi.getValue() ) || 
                CategoryConstants.MODE_OF_PAYMENT_MINOR_CAPITAL.getValueKey().equals( mi.getValue()))) {
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
        
        MetaInfo mi             = new MetaInfo(ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT, "Capital");
        ret.add(mi);
        
        return ret;
    }

}
