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
}
