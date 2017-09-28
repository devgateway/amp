package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class ORBinaryLogicalToken extends BinaryLogicalToken {

    public ORBinaryLogicalToken(LogicalToken left,
            LogicalToken right, boolean negation) {
        super(left, right, negation);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean evaluate(CategAmountCell c) {
        ret=left.evaluate(c) || right.evaluate(c);
        return super.evaluate(c);
    }
    
    @Override
    public String toString()
    {
        String result = String.format("[%s[%s OR %s]]", this.negation ? "NOT " : "", this.left.toString(), this.right.toString());
        if (this.negation)
            result = result.substring(1, result.length() - 1);
        return result;

    }
}
