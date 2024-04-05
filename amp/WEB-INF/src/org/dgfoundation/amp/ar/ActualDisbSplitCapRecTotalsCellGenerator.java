package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;

import java.util.Collection;
import java.util.Iterator;

public class ActualDisbSplitCapRecTotalsCellGenerator extends
        SyntheticCellGenerator {
    
    protected ActualDisbRecurrentCellGenerator recGenerator;
    protected ActualDisbCapitalCellGenerator capGenerator;

    public ActualDisbSplitCapRecTotalsCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        
        recGenerator    = new ActualDisbRecurrentCellGenerator(metaDataName, measureName, originalMeasureName);
        capGenerator    = new ActualDisbCapitalCellGenerator(metaDataName, measureName, originalMeasureName);
    }

    @Override
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Collection<CategAmountCell> generate(Collection<CategAmountCell> cells, int order) { 
        
        Collection<CategAmountCell> ret1    = recGenerator.generate(cells, order);
        Collection<CategAmountCell> ret2    = capGenerator.generate(cells, order);
        
        ret1.addAll(ret2);
        
        if ( cells != null ) {
            Iterator<CategAmountCell> iterCateg     = cells.iterator();
            while ( iterCateg.hasNext() ) {
                CategAmountCell categAmountCell = iterCateg.next();
                String fundingType              = categAmountCell.getMetaValueString(ArConstants.FUNDING_TYPE);
                
                if ( getOriginalMeasureName().equals(fundingType) ) {
                    
                    try {
                        iterCateg.remove();
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        
        return ret1;
        
    }

    @Override
    public boolean checkIfApplicabale(AmpReports reportMetadata) {
        if ( reportMetadata.getHierarchies() != null && reportMetadata.getHierarchies().size() > 0 ) {
            for ( AmpReportHierarchy repMes: reportMetadata.getHierarchies() ) {
                AmpColumns column   = repMes.getColumn();
                /* If column Capital / Expenditure is a hierarchy then do the normal check */
                if ( ArConstants.COLUMN_ACTUAL_DISB_CAPITAL_RECURRENT.equals(column.getColumnName())  ) 
                    return super.checkIfApplicabale(reportMetadata);
            }
        }
        return false;
    }

    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {
        // TODO Auto-generated method stub
        return null;
    }

}
