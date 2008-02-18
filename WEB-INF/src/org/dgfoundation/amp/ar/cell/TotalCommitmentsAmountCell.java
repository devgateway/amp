/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Iterator;

import org.dgfoundation.amp.exprlogic.ExampleLogicalToken;
import org.dgfoundation.amp.exprlogic.LogicalToken;

/**
 * @author mihai
 */
public class TotalCommitmentsAmountCell extends AmountCell {


    public TotalCommitmentsAmountCell() {
    	super();
    }

    public TotalCommitmentsAmountCell(AmountCell ac) {
		super(ac.getOwnerId());
		this.mergedCells = ac.getMergedCells();
    }

    /**
         * @param id
         */
    public TotalCommitmentsAmountCell(Long id) {
    	super(id);
    }

    /**
         * Ovverider of the normal behaviour of AmountCell.getAmount. This will take into consideration only undisbursed
         * related merged cells and will also make the required calculations
         * 
         * @return Returns the amount.
         */
    public double getAmount() {
    	double ret = 0;
		if (id != null)
			return convert();
	
		Iterator i = mergedCells.iterator();
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();
		/*	if( ArConstants.DISBURSEMENT.equals(element.getMetaValueString(ArConstants.TRANSACTION_TYPE)) ) continue;
			
			 if( ArConstants.ACTUAL.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) || 
					 ArConstants.PLANNED.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) )
		*/
			ret+=ExampleLogicalToken.totalCommitmentsLogicalToken.evaluate(element);
		
		}
		return ret;

	}

    public Cell merge(Cell c) {
		AmountCell ac = (AmountCell) super.merge(c);		
		TotalCommitmentsAmountCell uac = new TotalCommitmentsAmountCell(ac);
		return uac;
    }

    public Cell newInstance() {
    	return new TotalCommitmentsAmountCell();
    }

}
