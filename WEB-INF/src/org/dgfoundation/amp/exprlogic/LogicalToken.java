package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public abstract class LogicalToken {
	protected boolean negation;
	protected boolean ret;
	
	public boolean evaluate(CategAmountCell c) {
		if(negation) return !ret;
		return ret;
	}
}
