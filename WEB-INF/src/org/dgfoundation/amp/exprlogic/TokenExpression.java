package org.dgfoundation.amp.exprlogic;

import java.math.BigDecimal;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class TokenExpression {
	protected LogicalToken[] tokens;
	
	public TokenExpression(LogicalToken[] tokens) {
		this.tokens=tokens;
	}
	
	public BigDecimal evaluate(CategAmountCell c) {
		for (int i = 0; i < tokens.length; i++) {
			if(tokens[i].evaluate(c)) return c.getAmount().multiply(new BigDecimal(tokens[i].getSign()));
		}
		return new BigDecimal(0);
	}
	
	public AmountCell evaluateAsAmountCell(CategAmountCell c) {
		BigDecimal evaluate = evaluate(c);
		if(evaluate.doubleValue()==0d) return null;
		AmountCell ac=new AmountCell(c.getOwnerId());
		ac.setCurrencyCode(c.getCurrencyCode());
		ac.setId(c.getId());
		ac.setAmount(evaluate);
		return ac;
	}
}
