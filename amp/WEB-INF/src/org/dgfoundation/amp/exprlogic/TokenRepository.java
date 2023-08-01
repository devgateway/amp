package org.dgfoundation.amp.exprlogic;

import org.dgfoundation.amp.ar.ArConstants;

import java.util.*;

public class TokenRepository {

//  // internally thread safe repository for tokens:
    public static Map<String, TokenExpression> tokens = Collections.unmodifiableMap(new HashMap<String, TokenExpression>()
        {{
            this.put(TokenNames.TOTAL_COMMITMENTS, buildTotalCommitmentsLogicalToken());
            this.put(TokenNames.PLEDGES_TOTAL, buildTotalPledgedLogicalToken());
            this.put(TokenNames.TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT, buildTotalPledgeActivityCommitmentsLogicalToken());
            
            LogicalToken NOT_REAL_COMMITMENT = new NonDirectedTransactionLogicalToken(ArConstants.ACTUAL_COMMITMENTS, false);
            this.put(TokenNames.ACTUAL_COMMITMENTS, buildSimpleTransactionLogicalToken(ArConstants.COMMITMENT, ArConstants.ACTUAL, NOT_REAL_COMMITMENT));
            //this.put(TokenNames.ACTUAL_COMMITMENTS, buildSimpleTransactionLogicalToken(ArConstants.COMMITMENT, ArConstants.ACTUAL, null));
            
            this.put(TokenNames.PLANED_COMMITMENTS, buildSimpleTransactionLogicalToken(ArConstants.COMMITMENT, ArConstants.PLANNED, null));
            this.put(TokenNames.PLANED_DISBURSEMENT, buildSimpleTransactionLogicalToken(ArConstants.DISBURSEMENT, ArConstants.PLANNED, null));
                
            LogicalToken NOT_REAL_DISBURSEMENT = new NonDirectedTransactionLogicalToken(ArConstants.ACTUAL_DISBURSEMENTS, false);
            this.put(TokenNames.ACTUAL_DISBURSEMENT, buildSimpleTransactionLogicalToken(ArConstants.DISBURSEMENT, ArConstants.ACTUAL, NOT_REAL_DISBURSEMENT));
            this.put(TokenNames.UNCOMMITTED, buildUncommittedLogicalToken());

            this.put(TokenNames.CUMULATED_DISBURSEMENTS, buildCumulatedDisursementsLogicalToken());
            this.put(TokenNames.SELECTED_YEAR_PLANNED_DISBURSEMENTS, buildSelectedYearPlannedDisbursementsLogicalToken());
            this.put(TokenNames.PRIOR_ACTUAL_DISBURSEMENTS, buildPriorActualDisbursementsLogicalToken());
            this.put(TokenNames.CLOSED_MONTH_ACTUAL_DISBURSEMENTS, buildClosedMonthActualDisbursementsLogicalToken());
        }});

    // TOTAL Commitment
    private static TokenExpression buildTotalCommitmentsLogicalToken()
    {
        // Injected funding
        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);

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
        TokenExpression te = new TokenExpression(new LogicalToken[] { and3 });
        return te;
    }
        
    // Pledged Total
    private static TokenExpression buildTotalPledgedLogicalToken()
    {
        PresentLogicalToken totalpledged = new PresentLogicalToken(ArConstants.PLEDGED_TOTAL, false);

        EqualsLogicalToken pledgeCommitmentTrType = new EqualsLogicalToken(ArConstants.PLEDGE, ArConstants.TRANSACTION_TYPE, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { totalpledged, pledgeCommitmentTrType });
        return te;
    }
    
    //Pledge total activity commitments
    private static TokenExpression buildTotalPledgeActivityCommitmentsLogicalToken()
    {
        PresentLogicalToken totalpledgeactcommitments = new PresentLogicalToken(ArConstants.TOTAL_PLEDGE_ACTIVITY_ACTUAL_COMMITMENT, false);

        EqualsLogicalToken pledgeCommitmentTrType = new EqualsLogicalToken(ArConstants.PLEDGES_COMMITMENT, ArConstants.TRANSACTION_TYPE, false);
        EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);
        
        ANDBinaryLogicalToken and0 = new ANDBinaryLogicalToken(pledgeCommitmentTrType, actualAdjType, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { totalpledgeactcommitments, and0 });
        return te;
    }
    
    /**
     * builds a TokenExpression for transaction types of a single trType and adjType only (like, for example, ACTUAL COMMITMENTS or PLANNED DISBURSEMENTS)
     * @param trType
     * @param adjType
     * @return
     */
    private static TokenExpression buildSimpleTransactionLogicalToken(String trType, String adjType, LogicalToken finalAndLogicalToken)
    {
        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);

        EqualsLogicalToken seekedTrType = new EqualsLogicalToken(trType, ArConstants.TRANSACTION_TYPE, false);
        EqualsLogicalToken seekedAdjType = new EqualsLogicalToken(adjType, ArConstants.ADJUSTMENT_TYPE, false);
        
        ANDBinaryLogicalToken and2 = new ANDBinaryLogicalToken(seekedTrType, seekedAdjType, false);
        ANDBinaryLogicalToken and3 = new ANDBinaryLogicalToken(and2, proposedCost, false);

        if (finalAndLogicalToken != null)
            and3 = new ANDBinaryLogicalToken(and3, finalAndLogicalToken, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { and3 });
        
        return te;
    }
    
//  // PLANNED
//  private static TokenExpression buildPlannedCommitmentsLogicalToken() {
//      TokenExpression te = buildSimpleTransactionLogicalToken(ArConstants.COMMITMENT, ArConstants.PLANNED, null);
//      tokens.put(TokenNames.PLANED_COMMITMENTS, te);
//      return te;
//  }
//
//  private static TokenExpression buildPlannedDisbursementsLogicalToken() {
//      TokenExpression te = buildSimpleTransactionLogicalToken(ArConstants.DISBURSEMENT, ArConstants.PLANNED, null);
//      tokens.put(TokenNames.PLANED_DISBURSEMENT, te);
//      return te;
//  }

//  // ACTUAL
//  private static TokenExpression buildActualCommitmentsLogicalToken() {
//      TokenExpression te = buildSimpleTransactionLogicalToken(ArConstants.COMMITMENT, ArConstants.ACTUAL, null);
//      tokens.put(TokenNames.ACTUAL_COMMITMENTS, te);
//      return te;
//      /**
//       *  [[[[[[Adjustment Type EQUALS Actual] OR NOT [Adjustment Type EQUALS Planned]] AND [[[NOT [Transaction Type EQUALS Disbursements] AND NOT [Transaction Type EQUALS Expenditures]] AND NOT [Transaction Type EQUALS Disbursement Orders]] AND NOT [Transaction Type EQUALS Pledge]]] AND NOT [HASMETA Proposed Cost]] AND NOT [HASMETA Grand Total]]]
//       */
//  }
//
//  private static TokenExpression buildActualDisbursementsLogicalToken() {
//
//      LogicalToken NOT_REAL_DISBURSEMENT = new EstimatedDisbursementLogicalToken(false);
//      TokenExpression te = buildSimpleTransactionLogicalToken(ArConstants.DISBURSEMENT, ArConstants.ACTUAL, NOT_REAL_DISBURSEMENT);
//      tokens.put(TokenNames.ACTUAL_DISBURSEMENT, te);
//      return te;
//  }

//  // Undisbursed
//  public static TokenExpression buildUndisbursedLogicalToken() {
//
//      EqualsLogicalToken commitmentTrType = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, false);
//
//      EqualsLogicalToken disbursement = new EqualsLogicalToken(ArConstants.DISBURSEMENT, ArConstants.TRANSACTION_TYPE, false);
//      EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);
//      ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(disbursement, actualAdjType, false);
//      and1.setSign(-1);
//      TokenExpression te = new TokenExpression(new LogicalToken[] { commitmentTrType, and1 });
//
//      if (tokens == null)
//          tokens = new Hashtable<String, TokenExpression>();
//      tokens.put(TokenNames.UNDISBURSED, te);
//
//      return te;
//  }

    // Uncommitted
    private static TokenExpression buildUncommittedLogicalToken() {
        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, false);

        EqualsLogicalToken commitmentTrType = new EqualsLogicalToken(ArConstants.COMMITMENT, ArConstants.TRANSACTION_TYPE, false);
        EqualsLogicalToken actualAdjType = new EqualsLogicalToken(ArConstants.ACTUAL, ArConstants.ADJUSTMENT_TYPE, false);

        ANDBinaryLogicalToken and1 = new ANDBinaryLogicalToken(commitmentTrType, actualAdjType, false);
        and1.setSign(-1);
        TokenExpression te = new TokenExpression(new LogicalToken[] { proposedCost, and1 });
        return te;
    }

    public static TokenExpression buildMtefColumnToken(String columnName, int year) {
        GregorianCalendar c1=new GregorianCalendar();
        c1.set(Calendar.YEAR, year);
        c1.set(Calendar.DAY_OF_MONTH,1);
        c1.set(Calendar.MONTH, 0);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE,-1);
        
        GregorianCalendar c2=new GregorianCalendar();
        c2.set(Calendar.YEAR, year);
        c2.set(Calendar.DAY_OF_MONTH,31);
        c2.set(Calendar.MONTH, 11);
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        
        PresentLogicalToken mtefPresentToken    = new PresentLogicalToken( columnName , false);
        DateRangeLogicalToken drLogicalToken    = new DateRangeLogicalToken(c1.getTime(), c2.getTime(), ArConstants.TRANSACTION_DATE );
        ANDBinaryLogicalToken andLogicalToken   = new ANDBinaryLogicalToken(mtefPresentToken, drLogicalToken, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { andLogicalToken });
        return te;
    }
    
    private static TokenExpression buildCumulatedDisursementsLogicalToken() {

        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);

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

        //YearLogicalToken year = new YearLogicalToken();
        PresentLogicalToken year=new PresentLogicalToken(ArConstants.COMPUTE_ON_YEAR,false);
        ANDBinaryLogicalToken and5 = new ANDBinaryLogicalToken(and3, year, false);

        GregorianCalendar c2=new GregorianCalendar();
        c2.set(Calendar.DAY_OF_MONTH,1);
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.AM_PM,Calendar.PM);
        c2.add(Calendar.DAY_OF_MONTH,-1);
        //exclude no closed period
        DateRangeLogicalToken computedOnlyNoClosedPeriod=new DateRangeLogicalToken(null,c2.getTime(),ArConstants.TRANSACTION_DATE);
        ANDBinaryLogicalToken and6 = new ANDBinaryLogicalToken(and5, computedOnlyNoClosedPeriod, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { and6 });
        return te;
    }

    private static TokenExpression buildSelectedYearPlannedDisbursementsLogicalToken() {
        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);

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

        //YearLogicalToken year = new YearLogicalToken();
        PresentLogicalToken year=new PresentLogicalToken(ArConstants.COMPUTE_ON_YEAR,false);
        ANDBinaryLogicalToken and5 = new ANDBinaryLogicalToken(and3, year, false);

        TokenExpression te = new TokenExpression(new LogicalToken[] { and5 });
        return te;
    }
    
    /**
     * 
     * 
     * Current month should be understood as the current month that has been already close.
     * @return
     */
    private static TokenExpression buildPriorActualDisbursementsLogicalToken() {
        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);
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

        // if today =  09/2009  period should be 01/2009 to /08/2009
        GregorianCalendar c1=new GregorianCalendar(); 
        c1.set(Calendar.DAY_OF_MONTH,1);
        c1.set(Calendar.MONTH,Calendar.JANUARY);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.AM_PM,Calendar.AM);
        c1.set(Calendar.MILLISECOND, 0); //01.01.2009 00:00:00
        Date d1=c1.getTime();
        
        GregorianCalendar c2=new GregorianCalendar();
        c2.set(Calendar.DAY_OF_MONTH,1);
        c2.add(Calendar.MONTH,-1); //01.08.2009
        c2.add(Calendar.DAY_OF_MONTH,-1);//31/07/2009
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.AM_PM,Calendar.PM);
        Date d2=c2.getTime();
        
        DateRangeLogicalToken token=new DateRangeLogicalToken(d1,d2,ArConstants.TRANSACTION_DATE);
        
        ANDBinaryLogicalToken and5 = new ANDBinaryLogicalToken(and3, token, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { and5 });

        return te;
    }
    
    /**
     * Last Closed Month
     * @return
     */
    private static TokenExpression buildClosedMonthActualDisbursementsLogicalToken() {

        PresentLogicalToken proposedCost = new PresentLogicalToken(ArConstants.PROPOSED_COST, true);

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

        GregorianCalendar c1=new GregorianCalendar();
        c1.set(Calendar.DAY_OF_MONTH,1);
        c1.add(Calendar.MONTH,-1);
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        c1.set(Calendar.AM_PM,Calendar.AM);
        Date d1=c1.getTime();
        
        GregorianCalendar c2=new GregorianCalendar();
        c2.set(Calendar.DAY_OF_MONTH,1);
        c2.add(Calendar.DAY_OF_MONTH,-1);
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.AM_PM,Calendar.PM);
        Date d2=c2.getTime();
        
        DateRangeLogicalToken token=new DateRangeLogicalToken(d1,d2,ArConstants.TRANSACTION_DATE);
        
        ANDBinaryLogicalToken and5 = new ANDBinaryLogicalToken(and3, token, false);
        
        TokenExpression te = new TokenExpression(new LogicalToken[] { and5 });
        return te;
    }
}
