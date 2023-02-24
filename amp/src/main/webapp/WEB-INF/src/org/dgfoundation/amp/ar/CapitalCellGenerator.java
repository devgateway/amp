package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CapitalCellGenerator extends SyntheticCellGenerator {

    
    public CapitalCellGenerator(String metaDataName, String measureName,
            String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
    }

    @Override
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {
        MetaInfo<Double> mi = metaData.getMetaInfo(this.getMetaDataName());
        Double capitalPercent   = 0.0;
        if ( mi != null && mi.getValue() != null ) {
            capitalPercent      = mi.getValue();
        }
        double ret  = originalAmount * capitalPercent / 100.0;
        
        return ret;
    }
    
    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {
        ArrayList<MetaInfo> ret = new ArrayList<MetaInfo>();
        
        MetaInfo mi             = new MetaInfo(ArConstants.COLUMN_CAPITAL_EXPENDITRURE, "Capital");
        ret.add(mi);
        
        return ret;
    }

}
