package org.digijava.module.aim.logic.defaultimpl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.digijava.module.aim.logic.AmountCalculator;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class DefaultUnDisbursmentCalculator implements AmountCalculator {

	public BigDecimal calculateAmount(Set<CategAmountCell> mergedCells) {
		BigDecimal ret = new BigDecimal(0);
		Iterator<CategAmountCell> i = mergedCells.iterator();
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();
			if(!element.isCummulativeShow() && element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equals(ArConstants.DISBURSEMENT)) continue;
			if(!ArConstants.ACTUAL.equals( element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE) )) continue;
			if (ArConstants.COMMITMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ))
										ret = ret.add(element.getAmount());
			//
			if (ArConstants.DISBURSEMENT.equals( element.getMetaValueString(ArConstants.TRANSACTION_TYPE) ))
				ret =ret.subtract(element.getAmount()) ;

		}
		return ret;
	}

}
