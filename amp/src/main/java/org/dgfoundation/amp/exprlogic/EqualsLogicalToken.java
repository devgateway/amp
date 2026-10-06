package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class EqualsLogicalToken extends LogicalToken {
    protected String value;
    protected String type;
    public EqualsLogicalToken(String value,String type,boolean negation) {
        this.value=value;
        this.negation=negation;
        this.type=type;
    }
    
    @Override
    public boolean evaluate(CategAmountCell c) {
        ret=value.equals(c.getMetaValueString(type));
        return super.evaluate(c);
    }
    
    @Override
    public String toString()
    {
        String result = String.format("[%s %s %s]", this.type.toString(), this.negation ? "NOT_EQUALS " : "EQUALS", this.value.toString());
        return result;

    }
    
}
