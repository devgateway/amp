package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

/**
 * 
* PresentLogicalToken.java
* Checks if a category is present inside a metainfo. does not care about its value ...
* @author mihai
* @package org.dgfoundation.amp.exprlogic
* @since 12.05.2008
* @see org.dgfoundation.amp.exprlogic.TokenRepository#buildUncommittedLogicalToken()
*/
public class PresentLogicalToken extends LogicalToken {
    protected String type;
    public PresentLogicalToken(String type,boolean negation) {
        this.negation=negation;
        this.type=type;
    }
    
    @Override
    public boolean evaluate(CategAmountCell c) {
        ret=c.existsMetaString(type);
        return super.evaluate(c);
    }
    
    @Override
    public String toString()
    {
        return String.format("[%s %s]", this.negation ? "NOT HAS_META " : "HAS_META", this.type); 
    }
}
