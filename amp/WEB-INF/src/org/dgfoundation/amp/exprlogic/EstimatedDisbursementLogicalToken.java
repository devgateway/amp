package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class EstimatedDisbursementLogicalToken extends LogicalToken
{
	public EstimatedDisbursementLogicalToken(boolean negation) {
		this.negation = negation;
	}
	
	@Override
	public boolean evaluate(CategAmountCell c) {
		ret = c.isEstimatedDisbursement();
		return super.evaluate(c);
	}
	
	@Override
	public String toString()
	{
		return String.format("[%s]", this.negation ? "NOT IS_ESTIMATED_DISB " : "IS_ESTIMATED_DISB"); 
	}
}
