/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * @author Alex Gartner
 *
 */
public class CapitalSplitTotalsCellGenerator extends SyntheticCellGenerator {
    
    protected CapitalExpenditureCellGenerator eGenerator;
    protected CapitalCellGenerator cGenerator;
    
    /**
     * @param metaDataName
     * @param measureName
     * @param originalMeasureName
     */
    public CapitalSplitTotalsCellGenerator(String metaDataName,
            String measureName, String originalMeasureName) {
        super(metaDataName, measureName, originalMeasureName);
        
        eGenerator  = new CapitalExpenditureCellGenerator(metaDataName, measureName,originalMeasureName);
        cGenerator  = new CapitalCellGenerator(metaDataName, measureName,originalMeasureName);
        
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.SyntheticCellGenerator#computeAmount(double, java.util.Set)
     */
    @Override
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Collection<CategAmountCell> generate(Collection<CategAmountCell> cells, int order) { 
        
        Collection<CategAmountCell> ret1    = eGenerator.generate(cells, order);
        Collection<CategAmountCell> ret2    = cGenerator.generate(cells, order);
        
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
                if ( ArConstants.COLUMN_CAPITAL_EXPENDITRURE.equals(column.getColumnName())  ) 
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
