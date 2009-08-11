package org.dgfoundation.amp.exprlogic;

import java.util.Hashtable;

import org.dgfoundation.amp.ar.ArConstants;

public class TokenRepository {

	public static final class TokenNames {
		public static final String TOTAL_COMMITMENTS = "totalCommitmentsLogicalToken";
		public static final String UNDISBURSED = "undisbursedLogicalToken";
		public static final String UNCOMMITTED = "uncommittedLogicalToken";

		public static final String COSTING_GRAND_TOTAL = "grandTotalToken";

		public static final String PLANED_COMMITMENTS = "plannedCommitmentsLogicalToken";
		public static final String ACTUAL_COMMITMENTS = "actualCommitmentsLogicalToken";

		public static final String PLANED_DISBURSEMENT = "plannedDisbusementLogicalToken";
		public static final String ACTUAL_DISBURSEMENT = "actualCommitmentsLogicalToken";

	};

	// internally thread safe repository for tokens:
	public static Hashtable<String, TokenExpression> tokens;

	public static TokenExpression totalCommitmentsLogicalToken = buildTotalCommitmentsLogicalToken();
	public static TokenExpression undisbursedLogicalToken = buildUndisbursedLogicalToken();
	public static TokenExpression uncommittedLogicalToken = buildUncommittedLogicalToken();
	public static TokenExpression actualCommitmentsLogicalToken = buildActualCommitmentsLogicalToken();

	// TOTAL Commitment
	public static TokenExpression buildTotalCommitmentsLogicalToken() {
		// Injected funding
		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
		PresentLogicalToken grandTotaldCost = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, true);

		// Filter no Commitments funding
		EqualsLogicalToken NotDisbursementTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotExpenditureTrType = new EqualsLogicalToken(ArConstants.EXPENDITURE, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotDisbursedOrderTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT_ORDERS, ArConstants.TRANSACTION_TYPE, true);

		// Only Actual
		EqualsLogicalToken NotPlannedAdjType = new EqualsLogicalToken(ArConstants.PLANNED, ArConstants.ADJUSTMENT_TYPE, true);
		EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);

		ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(NotDisbursementTrType, NotExpenditureTrType, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(and0, NotDisbursedOrderTrType, false);

		ORBinaryLogicalToken or1 = new ORBinaryLogicalToken(actualAdjType, NotPlannedAdjType, false);
		ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(or1, and1, false);
		ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);
		ANDBinaryLogicalToken and4 = new ANDBinaryLogicalToken(and3, grandTotaldCost, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { and4 });

		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.TOTAL_COMMITMENTS, te);
		return te;
	}

	// PLANNED
	public static TokenExpression buildPLannedCommitmentsLogicalToken() {
		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
		PresentLogicalToken grandTotaldCost = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, true);

		// Filter no Commitments funding
		EqualsLogicalToken NotDisbursementTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotExpenditureTrType = new EqualsLogicalToken(ArConstants.EXPENDITURE, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotDisbursedOrderTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT_ORDERS, ArConstants.TRANSACTION_TYPE, true);

		// Only Planned
		EqualsLogicalToken plannedAdjType = new EqualsLogicalToken(ArConstants.PLANNED, ArConstants.ADJUSTMENT_TYPE, false);
		EqualsLogicalToken NotactualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, true);

		ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(NotDisbursementTrType, NotExpenditureTrType, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(NotDisbursedOrderTrType, and0, false);
		ORBinaryLogicalToken or1 = new ORBinaryLogicalToken(NotactualAdjType, plannedAdjType, false);
		ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(or1, and1, false);
		ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);
		ANDBinaryLogicalToken and4 = new ANDBinaryLogicalToken(and3, grandTotaldCost, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { and4 });

		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.PLANED_COMMITMENTS, te);
		return te;
	}

	public static TokenExpression buildPLannedDisbursementsLogicalToken() {
		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
		PresentLogicalToken grandTotaldCost = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, true);

		// Filter no Disbursement funding
		EqualsLogicalToken NotCommitments = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotExpenditureTrType = new EqualsLogicalToken(ArConstants.EXPENDITURE, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotDisbursedOrderTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT_ORDERS, ArConstants.TRANSACTION_TYPE, true);

		// Only Planned
		EqualsLogicalToken plannedAdjType = new EqualsLogicalToken(ArConstants.PLANNED, ArConstants.ADJUSTMENT_TYPE, false);
		EqualsLogicalToken NotactualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, true);

		ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(NotCommitments, NotExpenditureTrType, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(NotDisbursedOrderTrType, and0, false);
		ORBinaryLogicalToken or1 = new ORBinaryLogicalToken(NotactualAdjType, plannedAdjType, false);
		ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(or1, and1, false);
		ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);
		ANDBinaryLogicalToken and4 = new ANDBinaryLogicalToken(and3, grandTotaldCost, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { and4 });

		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.PLANED_DISBURSEMENT, te);
		return te;
	}

	// ACTUAL
	public static TokenExpression buildActualCommitmentsLogicalToken() {
		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
		PresentLogicalToken grandTotaldCost = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, true);

		// Filter no Commitments funding
		EqualsLogicalToken NotDisbursementTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotExpenditureTrType = new EqualsLogicalToken(ArConstants.EXPENDITURE, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotDisbursedOrderTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT_ORDERS, ArConstants.TRANSACTION_TYPE, true);

		// Only Actual
		EqualsLogicalToken NotPlannedAdjType = new EqualsLogicalToken(ArConstants.PLANNED, ArConstants.ADJUSTMENT_TYPE, true);
		EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);

		ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(NotDisbursementTrType, NotExpenditureTrType, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(and0, NotDisbursedOrderTrType, false);

		ORBinaryLogicalToken or1 = new ORBinaryLogicalToken(actualAdjType, NotPlannedAdjType, false);
		ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(or1, and1, false);
		ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);
		ANDBinaryLogicalToken and4 = new ANDBinaryLogicalToken(and3, grandTotaldCost, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { and4 });

		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.ACTUAL_COMMITMENTS, te);
		return te;
	}

	public static TokenExpression buildActualDisbursementsLogicalToken() {

		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
		PresentLogicalToken grandTotaldCost = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, true);

		EqualsLogicalToken NotCommitments = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotExpenditureTrType = new EqualsLogicalToken(ArConstants.EXPENDITURE, ArConstants.TRANSACTION_TYPE, true);
		EqualsLogicalToken NotDisbursedOrderTrType = new EqualsLogicalToken(ArConstants.DISBURSEMENT_ORDERS, ArConstants.TRANSACTION_TYPE, true);

		// only actual
		EqualsLogicalToken NotPlannedAdjType = new EqualsLogicalToken(ArConstants.PLANNED, ArConstants.ADJUSTMENT_TYPE, true);
		EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);

		ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(NotCommitments, NotExpenditureTrType, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(NotDisbursedOrderTrType, and0, false);
		ORBinaryLogicalToken or1 = new ORBinaryLogicalToken(actualAdjType, NotPlannedAdjType, false);
		ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(or1, and1, false);

		ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);

		ANDBinaryLogicalToken and4 = new ANDBinaryLogicalToken(and3, grandTotaldCost, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { and4 });
		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.ACTUAL_DISBURSEMENT, te);
		return te;
	}

	// Undisbursed
	public static TokenExpression buildUndisbursedLogicalToken() {

		EqualsLogicalToken commitmentTrType = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, false);

		EqualsLogicalToken disbursement = new EqualsLogicalToken(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_TYPE, false);
		EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);
		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(disbursement, actualAdjType, false);
		and1.setSign(-1);
		TokenExpression te = new TokenExpression(new LogicalToken[] { commitmentTrType, and1 });

		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.UNDISBURSED, te);

		return te;
	}

	// Uncommitted
	public static TokenExpression buildUncommittedLogicalToken() {
		PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, false);

		EqualsLogicalToken commitmentTrType = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, false);
		EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);

		ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(commitmentTrType, actualAdjType, false);
		and1.setSign(-1);
		TokenExpression te = new TokenExpression(new LogicalToken[] { proposedCost, and1 });
		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.UNCOMMITTED, te);
		return te;
	}

	public static TokenExpression buildCostingGrandTotalToken() {
		PresentLogicalToken grandTotal = new PresentLogicalToken(ArConstants.COSTING_GRAND_TOTAL, false);
		TokenExpression te = new TokenExpression(new LogicalToken[] { grandTotal });
		if (tokens == null)
			tokens = new Hashtable<String, TokenExpression>();
		tokens.put(TokenNames.COSTING_GRAND_TOTAL, te);
		return te;
	}

}
