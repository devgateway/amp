package org.digijava.module.aim.logic.boliviaimpl;

import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.logic.AmountCalculator;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class BoliviaUnDisbursmentCalculator implements AmountCalculator {

    public double calculateAmount(Set<CategAmountCell> mergedCells) {
        double ret = 0;
        Iterator<CategAmountCell> i = mergedCells.iterator();
        while (i.hasNext()) {
            CategAmountCell element = (CategAmountCell) i.next();
            if(!element.isCummulativeShow() && element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equals(ArConstants.DISBURSEMENT)) continue;

            if (ArConstants.COMMITMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ))
                                        ret += element.getAmount();

            if( ArConstants.DISBURSEMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ) && 
                ArConstants.ACTUAL.equals( element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE) )){
                ret -= element.getAmount();
            }

        }
        return ret;
    }

}
