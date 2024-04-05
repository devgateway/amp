package org.digijava.module.aim.logic.boliviaimpl;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.logic.AmountCalculator;

import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaCommitmentCalculator implements AmountCalculator{

    public double calculateAmount(Set<CategAmountCell> mergedCells) {
        double ret = 0;
        Iterator<CategAmountCell> i = mergedCells.iterator();
        while (i.hasNext()) {
            CategAmountCell element = (CategAmountCell) i.next();
            
            if(!element.isShow())
                continue;
            
            if( ArConstants.DISBURSEMENT.equals(element.getMetaValueString(ArConstants.TRANSACTION_TYPE)) ) continue;
            
             if( ArConstants.ACTUAL.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) || 
                     ArConstants.PLANNED.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) )
            ret += element.getAmount();
        }
        return ret;
        
    }


}
