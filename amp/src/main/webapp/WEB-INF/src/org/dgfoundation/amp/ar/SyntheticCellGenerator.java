package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;

public abstract class SyntheticCellGenerator {
    private final String metaDataName;
    private final String measureName;
    private final String originalMeasureName;
    
    public SyntheticCellGenerator(String metaDataName, String measureName, String originalMeasureName) {
        super();
        this.metaDataName = metaDataName;
        this.measureName = measureName;
        this.originalMeasureName = originalMeasureName;
    }
    
//  /**
//   * should only be used by super-constructors
//   */
//  protected SyntheticCellGenerator(){
//      
//  }

    public Collection<CategAmountCell> generate(Collection<CategAmountCell> cells, int order) {
        ArrayList<CategAmountCell> retCells = new ArrayList<CategAmountCell>();
        if ( cells != null ) {
            for (CategAmountCell categAmountCell : cells) {
                String fundingType      = categAmountCell.getMetaValueString(ArConstants.FUNDING_TYPE);
                
                if ( originalMeasureName.equals(fundingType) ) {
                    
                    try {
                        CategAmountCell newCell = (CategAmountCell) categAmountCell.clone();
                        newCell.setAmount( this.computeAmount(newCell.getInitialAmount(), categAmountCell.getMetaData()) );
                        newCell.setMetaData(new MetaInfoSet());
                        for(MetaInfo tempMi:categAmountCell.getMetaData())
                        {
                            if ( ArConstants.FUNDING_TYPE.equals(tempMi.getCategory()) ) {
                                    MetaInfo<FundingTypeSortedString> newMi = 
                                        new MetaInfo<FundingTypeSortedString>(ArConstants.FUNDING_TYPE, new FundingTypeSortedString(measureName, order));
                                    newCell.getMetaData().add(newMi);
                                }
                                else {
                                    MetaInfo newMi  = tempMi; // MetaInfo is immutable, so it is ok to just copy
                                    newCell.getMetaData().add(newMi);
                                }
                        }
                        
                        
                        Collection<MetaInfo> mInfos = this.syntheticMetaInfo();
                        if ( mInfos != null && mInfos.size() > 0  ) {
                            for (MetaInfo metaInfo : mInfos) {
                                newCell.getMetaData().add(metaInfo);
                            }
                        }
                        
                        retCells.add(newCell);
                        
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return retCells;
    }

    public boolean checkIfApplicabale(AmpReports reportMetadata) {
        for ( AmpReportMeasures repMes: reportMetadata.getMeasures() ) {
            AmpMeasures measure = repMes.getMeasure();
            if ( this.measureName.equals(measure.getMeasureName())  ) 
                return true;
        }
        return false;
    }
    
    public abstract double computeAmount (double originalAmount, MetaInfoSet metaData); 
    
    public abstract Collection<MetaInfo> syntheticMetaInfo ();
    
    public void setSession ( HttpSession session) {}
    

    protected String getMetaDataName() {
        return metaDataName;
    }

    /**
     * @return the measureName
     */
    public String getMeasureName() {
        return measureName;
    }

    /**
     * @return the originalMeasureName
     */
    protected String getOriginalMeasureName() {
        return originalMeasureName;
    }

        
    @Override
    public String toString()
    {
        return String.format("SyntheticCellGenerator: %s[%s] -> %s", originalMeasureName, metaDataName, measureName);
    }
}
