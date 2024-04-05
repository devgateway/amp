package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * the generator which will generate Commitment Gap cells
 * each Actual Pledge ap cell will get a companion Commitment Gap cell equaling
 * (total_actual_pledged(pledge) - total_committed(pledge)) * (ap(pledge) / total_actual_pledged(pledge))
 * @author Dolghier Constantin
 *
 */
public class CommitmentGapCellGenerator extends SyntheticCellGenerator{

    //public MetaInfo<String> COMMITMENT_GAP_METAINFO = new MetaInfo<>(ArConstants.TRANSACTION_TYPE, ArConstants.TRANSACTION_TYPE_COMMITMENT_GAP);
    public final static MetaInfo<FundingTypeSortedString> COMMITMENT_GAP_FUNDING_TYPE = new MetaInfo<>(ArConstants.FUNDING_TYPE, 
            new FundingTypeSortedString(ArConstants.FUNDING_TYPE_COMMITMENT_GAP, 999)); // the last one - always

    public CommitmentGapCellGenerator() {
        //String metaDataName, String measureName, String originalMeasureName
        super("", "Commitment Gap", "");
    }

    @Override
    public double computeAmount(double originalAmount, MetaInfoSet metaData) {return 0;} // not used
    
    /**
     * generate a new column where "commitment gap" = 1 * PLEDGE + (-1) * ACTUAL COMMITMENT
     */
    @Override
    public Collection<CategAmountCell> generate(Collection<CategAmountCell> cells, int order){
        List<CategAmountCell> res = new ArrayList<>();
        for(CategAmountCell cell:cells){
            CategAmountCell newCell = null;
            if (ArConstants.PLEDGE.equals(cell.getMetaValueString(ArConstants.TRANSACTION_TYPE))){
                newCell = createMirrorCell(cell, 1);
            }
            if (ArConstants.COMMITMENT.equals(cell.getMetaValueString(ArConstants.TRANSACTION_TYPE)) &&
                ArConstants.ACTUAL.equals(cell.getMetaValueString(ArConstants.ADJUSTMENT_TYPE))){
                // actual commitments go by "-1"
                newCell = createMirrorCell(cell, -1);
            }
            if (newCell != null)
                res.add(newCell);
        }
        return res;
    }
    
    protected CategAmountCell createMirrorCell(CategAmountCell src, double multiplier){
        try{
            CategAmountCell newCell = (CategAmountCell) src.clone();
            newCell.setAmount(src.getInitialAmount() * multiplier);
            newCell.setMetaData(new MetaInfoSet(src.getMetaData()));
            newCell.getMetaData().removeItemsByCategory(Arrays.asList(COMMITMENT_GAP_FUNDING_TYPE.category));
            newCell.getMetaData().add(COMMITMENT_GAP_FUNDING_TYPE);

            return newCell;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Collection<MetaInfo> syntheticMetaInfo() {return null;} // not used
}
