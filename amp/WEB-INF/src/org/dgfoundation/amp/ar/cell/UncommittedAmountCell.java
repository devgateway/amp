/**
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.Iterator;

import mondrian.rolap.BitKey.Big;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.exprlogic.TokenRepository;
import org.digijava.module.aim.logic.Logic;

/**
 * @author mihai
 *
 */
public class UncommittedAmountCell extends AmountCell {


	public UncommittedAmountCell() {
		super();
	}
	
	 	public UncommittedAmountCell(AmountCell ac) {
	 		super(ac.getOwnerId());
	 		this.mergedCells=ac.getMergedCells();
		}

	/**
	 * @param id
	 */
	public UncommittedAmountCell(Long id) {
		super(id);
	}

	/**
	 * Overider of the normal behaviour of AmountCell.getAmount. This will take into consideration
	 * only undisbursed related merged cells and will also make the required calculations
	 * @return Returns the amount.
	 */
	public BigDecimal getAmount() {
		if (id != null)
			return convert();
		BigDecimal ret = new BigDecimal(0);
		Iterator<CategAmountCell> i = mergedCells.iterator();
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();
			ret=ret.add(TokenRepository.uncommittedLogicalToken.evaluate(element)) ;	
		}
		return ret;
	}
	
	
	public Cell merge(Cell c) {
		AmountCell ac=(AmountCell) super.merge(c);
		UncommittedAmountCell uac=new UncommittedAmountCell(ac);
		return uac;		
	}
	
	public Cell newInstance() {
		return new UncommittedAmountCell();
	}
	
}
