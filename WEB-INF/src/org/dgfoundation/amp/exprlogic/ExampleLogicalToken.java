package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class ExampleLogicalToken {
	
	public static TokenExpression totalCommitmentsLogicalToken=buildTotalCommitmentsLogicalToken();
	public static TokenExpression undisbursedLogicalToken=buildUndisbursedLogicalToken();
	public static TokenExpression uncommittedLogicalToken=buildUncommittedLogicalToken();	
	
	public static TokenExpression buildTotalCommitmentsLogicalToken() {
		
		EqualsLogicalToken NotDisbursementTrType=new EqualsLogicalToken(ArConstants.DISBURSEMENT,ArConstants.TRANSACTION_TYPE,true );
		EqualsLogicalToken actualAdjType=new EqualsLogicalToken(ArConstants.ACTUAL,ArConstants.ADJUSTMENT_TYPE,false );		
		EqualsLogicalToken plannedAdjType=new EqualsLogicalToken(ArConstants.PLANNED,ArConstants.ADJUSTMENT_TYPE,false );		
		ORBinaryLogicalToken or1=new ORBinaryLogicalToken(actualAdjType,plannedAdjType,false);	
		ANDBinaryLogicalToken and1=new ANDBinaryLogicalToken(NotDisbursementTrType,or1,false);
		
		TokenExpression te=new TokenExpression(new LogicalToken[]{and1});
		
		return te;
		
	}
	
	
	public static TokenExpression buildUndisbursedLogicalToken() {
		EqualsLogicalToken commitmentTrType=new EqualsLogicalToken(ArConstants.COMMITMENT,ArConstants.TRANSACTION_TYPE,false );
		
		EqualsLogicalToken disbursement=new EqualsLogicalToken(ArConstants.DISBURSEMENT,ArConstants.TRANSACTION_TYPE,false );
		EqualsLogicalToken actualAdjType=new EqualsLogicalToken(ArConstants.ACTUAL,ArConstants.ADJUSTMENT_TYPE,false );	
		ANDBinaryLogicalToken and1=new ANDBinaryLogicalToken(disbursement,actualAdjType,false);
		and1.setSign(-1);
		TokenExpression te=new TokenExpression(new LogicalToken[]{commitmentTrType,and1});
		return te;
	}
	
	public static TokenExpression buildUncommittedLogicalToken() {
	    	PresentLogicalToken proposedCost=new PresentLogicalToken(ArConstants.PROPOSED_COST,false);
	    	
		EqualsLogicalToken commitmentTrType=new EqualsLogicalToken(ArConstants.COMMITMENT,ArConstants.TRANSACTION_TYPE,false );
		EqualsLogicalToken actualAdjType=new EqualsLogicalToken(ArConstants.ACTUAL,ArConstants.ADJUSTMENT_TYPE,false );
		
			
		ANDBinaryLogicalToken and1=new ANDBinaryLogicalToken(commitmentTrType,actualAdjType,false);
		and1.setSign(-1);
		TokenExpression te=new TokenExpression(new LogicalToken[]{proposedCost,and1});
		return te;
	}
	
	
}
