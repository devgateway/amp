package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class ExampleLogicalToken {

	/*
	if( ArConstants.DISBURSEMENT.equals(element.getMetaValueString(ArConstants.TRANSACTION_TYPE)) ) continue;
	
	 if( ArConstants.ACTUAL.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) || 
			 ArConstants.PLANNED.equals(element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE)) )
	*/
	
	public static LogicalToken testToken=buildLogicalToken();
	
	public static LogicalToken buildLogicalToken() {
		EqualsLogicalToken NotDisbursementTrType=new EqualsLogicalToken(ArConstants.DISBURSEMENT,ArConstants.TRANSACTION_TYPE,true );
		EqualsLogicalToken actualAdjType=new EqualsLogicalToken(ArConstants.ACTUAL,ArConstants.ADJUSTMENT_TYPE,false );		
		EqualsLogicalToken plannedAdjType=new EqualsLogicalToken(ArConstants.PLANNED,ArConstants.ADJUSTMENT_TYPE,false );		
		ORBinaryLogicalToken or1=new ORBinaryLogicalToken(actualAdjType,plannedAdjType,false);	
		ANDBinaryLogicalToken and1=new ANDBinaryLogicalToken(NotDisbursementTrType,or1,false);
		
		return and1;
		
	}
	
	
	public boolean useToken(CategAmountCell c) {
		LogicalToken logicalToken = buildLogicalToken();
		return logicalToken.evaluate(c);
	}
	
}
