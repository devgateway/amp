/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.exprlogic.TokenRepository;
import org.digijava.module.aim.logic.Logic;

/**
 * @author mihai
 *
 */
public class UndisbursedAmountCell extends AmountCell {


	public UndisbursedAmountCell() {
		super();
	}
	
	 	public UndisbursedAmountCell(AmountCell ac) {
	 		super(ac.getOwnerId());
	 		this.mergedCells=ac.getMergedCells();
		}

	/**
	 * @param id
	 */
	public UndisbursedAmountCell(Long id) {
		super(id);
	}

	/**
	 * Overrider of the normal behavior of AmountCell.getAmount. This will take into consideration
	 * only undisbursed related merged cells and will also make the required calculations
	 * @return Returns the amount.
	 */
	public double getAmount() {
		if (id != null)
			return convert();
		double ret = 0;
		Iterator<CategAmountCell> i = mergedCells.iterator();
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();
			ret+=TokenRepository.undisbursedLogicalToken.evaluate(element);	
		}
		return ret;
	}
	
	
	public Cell merge(Cell c) {
		AmountCell ac=(AmountCell) super.merge(c);
		UndisbursedAmountCell uac=new UndisbursedAmountCell(ac);
		return uac;		
	}
	
	public Cell newInstance() {
		return new UndisbursedAmountCell();
	}
	
}
