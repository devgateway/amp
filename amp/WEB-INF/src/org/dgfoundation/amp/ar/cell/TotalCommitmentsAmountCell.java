/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;

/**
 * @author mihai
 */
public class TotalCommitmentsAmountCell extends AmountCell {

    /**
     * 
     */
    public TotalCommitmentsAmountCell() {
	super();
	// TODO Auto-generated constructor stub
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
	// TODO Auto-generated constructor stub
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
			if(element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equals(ArConstants.DISBURSEMENT)) continue;
			
			 if(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equals(ArConstants.ACTUAL) || 
			element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equals(ArConstants.PLANNED) )
			ret += element.getAmount();
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
