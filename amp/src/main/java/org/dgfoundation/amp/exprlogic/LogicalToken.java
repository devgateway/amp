package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public abstract class LogicalToken {
    protected boolean negation;
    protected boolean ret;
    protected int sign=1;

    public void setSign(int sign) {
        this.sign=sign;
    }
    
    public int getSign() {
        return sign;
    }
    
    public boolean evaluate(CategAmountCell c) {
        if(negation) return !ret;
        return ret;
    }
}
