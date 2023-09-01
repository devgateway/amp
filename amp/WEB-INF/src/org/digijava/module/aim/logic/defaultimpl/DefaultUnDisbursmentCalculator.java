package org.digijava.module.aim.logic.defaultimpl;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.logic.AmountCalculator;

import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class DefaultUnDisbursmentCalculator implements AmountCalculator {

    public double calculateAmount(Set<CategAmountCell> mergedCells) {
        double ret = 0;
        if (mergedCells == null)
            return ret;
        Iterator<CategAmountCell> i = mergedCells.iterator();
        while (i.hasNext()) {
            CategAmountCell element = (CategAmountCell) i.next();
            if(!element.isCummulativeShow() && element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equals(ArConstants.DISBURSEMENT)) continue;
            if(!ArConstants.ACTUAL.equals( element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE) )) continue;
            if (ArConstants.COMMITMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ))
                                        ret += element.getAmount();
            //
            if (ArConstants.DISBURSEMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ))
                ret -= element.getAmount();

        }
        return ret;
    }

}
