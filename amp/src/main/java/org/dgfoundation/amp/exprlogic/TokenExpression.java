package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class TokenExpression {
    protected LogicalToken[] tokens;
    
    public TokenExpression(LogicalToken[] tokens) {
        this.tokens=tokens;
    }
    
    public double evaluate(CategAmountCell c) {
        for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].evaluate(c)) 
                return tokens[i].getSign()*c.getAmount();
        }
        return 0;
    }
    
    public AmountCell evaluateAsAmountCell(CategAmountCell c) {
        double evaluate = evaluate(c);
        if(evaluate==0) return null;
        AmountCell ac=new AmountCell(c.getOwnerId());
        ac.setCurrencyCode(c.getCurrencyCode());
        ac.setId(c.getId());
        ac.setAmount(evaluate);
        return ac;
    }
    
    //The value stored with no %
    public double evaluateOriginalvalue(CategAmountCell c){
        for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].evaluate(c)) return tokens[i].getSign()*c.getOriginalAmount();
        }
        return 0;   
    }
    
    public String toString()
    {
        StringBuffer res = new StringBuffer("{");
        
        for(int i = 0; i < tokens.length; i++)
            res.append(tokens[i].toString() + ",");
            
        res.append("}");
        return res.toString();
    }
}
