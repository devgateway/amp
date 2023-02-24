package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class NonDirectedTransactionLogicalToken extends LogicalToken
{
    protected final String rawMeasure;
    
    public NonDirectedTransactionLogicalToken(String rawMeasure, boolean negation) {
        this.rawMeasure = rawMeasure;
        this.negation = negation;
    }
    
    @Override
    public boolean evaluate(CategAmountCell c) {
        ret = c.isNonDirectedTransaction(rawMeasure);
        return super.evaluate(c);
    }
    
    @Override
    public String toString()
    {
        return String.format("[%s]", this.negation ? "NOT IS_ESTIMATED_DISB " : "IS_ESTIMATED_DISB"); 
    }
}
