package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class TokenExpression {
	protected LogicalToken[] tokens;
	
	public TokenExpression(LogicalToken[] tokens) {
		this.tokens=tokens;
	}
	
	public double evaluate(CategAmountCell c) {
		for (int i = 0; i < tokens.length; i++) {
			if(tokens[i].evaluate(c)) return tokens[i].getSign()*c.getAmount();
		}
		return 0;
	}
}
