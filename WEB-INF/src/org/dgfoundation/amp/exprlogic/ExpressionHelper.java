package org.dgfoundation.amp.exprlogic;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mondrian.util.Bug;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;

public class ExpressionHelper {

	/**
	 * 
	 * @param items
	 * @return
	 */

	public static HashMap<String, BigDecimal> getGroupVariables(Collection<CategAmountCell> items) {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();
		Iterator<CategAmountCell> i = items.iterator();
		BigDecimal max_acutal_commitments = new BigDecimal(0);
		BigDecimal min_acutal_commitments = new BigDecimal(0);

		BigDecimal max_planned_commitments = new BigDecimal(0);
		BigDecimal min_planned_commitments = new BigDecimal(0);

		BigDecimal max_acutal_disburments = new BigDecimal(0);
		BigDecimal min_acutal_disburments = new BigDecimal(0);

		BigDecimal max_planned_disburments = new BigDecimal(0);
		BigDecimal min_planned_disburments = new BigDecimal(0);

		while (i.hasNext()) {
			ComputedAmountCell element = (ComputedAmountCell) i.next();
			BigDecimal tc = element.getValues().get(ArConstants.TOTAL_COMMITMENTS);

			BigDecimal ac = element.getValues().get(ArConstants.ACTUAL_COMMITMENT);
			BigDecimal ad = element.getValues().get(ArConstants.ACTUAL_DISBURSEMENT);

			BigDecimal pc = element.getValues().get(ArConstants.PLANNED_COMMITMENT);
			BigDecimal pd = element.getValues().get(ArConstants.PLANNED_DISBURSEMENT);

			BigDecimal tmpValue = null;
			BigDecimal tmpMinValue = null;

			// ACTUAL_COMMITMENT
			tmpValue = ac;
			max_acutal_commitments = (max_acutal_commitments.compareTo(tmpValue) < 0) ? tmpValue : max_acutal_commitments;
			if (min_acutal_commitments.intValue() == 0) {
				min_acutal_commitments = max_acutal_commitments;
			}
			if (ac.doubleValue()!=0d){
				min_acutal_commitments = (min_acutal_commitments.compareTo(tmpValue) > 0) ? tmpValue : min_acutal_commitments;
			}
			tmpValue = null;

			// ACTUAL_DISBURSEMENT
			tmpValue = ad;
			max_acutal_disburments = (max_acutal_disburments.compareTo(tmpValue) < 0) ? tmpValue : max_acutal_disburments;
			if (min_acutal_disburments.intValue() == 0) {
				min_acutal_disburments = max_acutal_disburments;
			}
			if (ad.doubleValue()!=0d){
				min_acutal_disburments = (min_acutal_disburments.compareTo(tmpValue) > 0 ) ? tmpValue : min_acutal_disburments;
			}
			tmpValue = null;

			// PLANNED_COMMITMENT
			tmpValue = pc;
			// planned_commitments

			max_planned_commitments = (max_planned_commitments.compareTo(tmpValue) < 0) ? tmpValue : max_planned_commitments;

			if (min_planned_commitments.intValue() == 0) {
				min_planned_commitments = max_planned_commitments;
			}
			if (pc.doubleValue()!=0d){
				min_planned_commitments = (min_planned_commitments.compareTo(tmpValue) > 0) ? tmpValue : min_planned_commitments;
			}
			tmpValue = null;

			// PLANNED_DISBURSEMENT
			tmpValue = pd;

			// max_acutal_commitments compare to value if -1 max is less if 1
			// max is greater
			max_planned_disburments = (max_planned_disburments.compareTo(tmpValue) < 0) ? tmpValue : max_planned_disburments;
			// min_acutal_commitments compare to value if -1 min is less, if 1
			// min is greater
			if (min_planned_disburments.intValue() == 0) {
				min_planned_disburments = max_planned_disburments;
			}
			if(pd.doubleValue()!=0){
				min_planned_disburments = (min_planned_disburments.compareTo(tmpValue) > 0) ? tmpValue : min_planned_disburments;
			}
		}

		values.put(ArConstants.MAX_ACTUAL_COMMITMENT, max_acutal_commitments);
		values.put(ArConstants.MIN_ACTUAL_COMMITMENT, min_acutal_commitments);

		values.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, max_acutal_disburments);
		values.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, min_acutal_disburments);

		values.put(ArConstants.MAX_PLANNED_COMMITMENT, max_planned_commitments);
		values.put(ArConstants.MIN_PLANNED_COMMITMENT, min_planned_commitments);

		values.put(ArConstants.MAX_PLANNED_DISBURSEMENT, max_planned_disburments);
		values.put(ArConstants.MIN_PLANNED_DISBURSEMENT, min_planned_disburments);
		return values;
	}

	public static HashMap<String, BigDecimal> getRowVariables(Collection<CategAmountCell> items) {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();
		Iterator<CategAmountCell> i = items.iterator();

		// total
		BigDecimal totalCommitments = new BigDecimal(0);
		// planed
		BigDecimal actualCommitments = new BigDecimal(0);
		BigDecimal actualDisburments = new BigDecimal(0);

		// actual
		BigDecimal plannedCommitments = new BigDecimal(0);
		BigDecimal plannedDisburments = new BigDecimal(0);

		BigDecimal countActualCommitments = new BigDecimal(0);
		BigDecimal countActualDisburments = new BigDecimal(0);
		BigDecimal countPlanedCommitments = new BigDecimal(0);
		BigDecimal countPlanedDisburments = new BigDecimal(0);
		BigDecimal proposedProjectCost = new BigDecimal(0);
		BigDecimal grandTotalCost = new BigDecimal(0);

		// for each element get each funding type
		while (i.hasNext()) {
			ComputedAmountCell element = (ComputedAmountCell) i.next();
			// using the logicExpression we will get the result of each funding
			// type

			BigDecimal tc = new BigDecimal(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element));

			BigDecimal ac = new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element));
			BigDecimal ad = new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element));

			BigDecimal pc = new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element));
			BigDecimal pd = new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element));

			totalCommitments = totalCommitments.add(tc);
			actualCommitments = actualCommitments.add(ac);

			actualDisburments = actualDisburments.add(ad);
			plannedCommitments = plannedCommitments.add(pc);
			plannedDisburments = plannedDisburments.add(pd);

			if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE) != null) {
				if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.ACTUAL)) {
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
						countActualCommitments = countActualCommitments.add(new BigDecimal(1));
					}
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
						countActualDisburments = countActualDisburments.add(new BigDecimal(1));
					}

				}

				if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.PLANNED)) {
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
						countPlanedCommitments = countPlanedCommitments.add(new BigDecimal(1));
					}
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
						countPlanedDisburments = countPlanedDisburments.add(new BigDecimal(1));
					}
				}
			} else if (element.existsMetaString(ArConstants.PROPOSED_COST)) {
				proposedProjectCost = proposedProjectCost.add(new BigDecimal(TokenRepository.buildUncommittedLogicalToken().evaluate(element)));
			} else if (element.existsMetaString(ArConstants.COSTING_GRAND_TOTAL)) {
				grandTotalCost = grandTotalCost.add(new BigDecimal(TokenRepository.buildCostingGrandTotalToken().evaluate(element)));
			}

		}
		// crate variable values map
		values.put(ArConstants.TOTAL_COMMITMENTS, totalCommitments);
		values.put(ArConstants.ACTUAL_COMMITMENT, actualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT, actualDisburments);
		// values.put(ArConstants.ACTUAL_EXPENDITURET, total_commitments);
		values.put(ArConstants.PLANNED_COMMITMENT, plannedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT, plannedDisburments);
		// values.put(ArConstants.PLANNED_EXPENDITURE, total_commitments);
		values.put(ArConstants.ACTUAL_COMMITMENT_COUNT, countActualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, countActualDisburments);

		values.put(ArConstants.PLANNED_COMMITMENT_COUNT, countPlanedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, countPlanedDisburments);

		values.put(ArConstants.PROPOSED_COST, proposedProjectCost);
		values.put(ArConstants.COSTING_GRAND_TOTAL, grandTotalCost);

		return values;
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	public static HashMap<String, BigDecimal> getMeasuresVariables(Collection<ComputedAmountCell> items) {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();
		Iterator<ComputedAmountCell> i = items.iterator();

		// total
		BigDecimal totalCommitmentsFiltered = new BigDecimal(0);
		BigDecimal plannedCommitmentsFiltered = new BigDecimal(0);
		BigDecimal plannedDisburmentsFiltered = new BigDecimal(0);
		BigDecimal actualCommitmentsFiltered = new BigDecimal(0);
		BigDecimal actualDisburmentsFiltered = new BigDecimal(0);

		BigDecimal totalActualCommitments = new BigDecimal(0);
		BigDecimal totalActualDisburments = new BigDecimal(0);
		BigDecimal totalPlannedCommitments = new BigDecimal(0);
		BigDecimal totalPlannedDisburments = new BigDecimal(0);

		BigDecimal actualCommitments = new BigDecimal(0);
		BigDecimal actualDisburments = new BigDecimal(0);
		BigDecimal plannedCommitments = new BigDecimal(0);
		BigDecimal plannedDisburments = new BigDecimal(0);

		BigDecimal countActualCommitments = new BigDecimal(0);
		BigDecimal countActualDisburments = new BigDecimal(0);
		BigDecimal countPlanedCommitments = new BigDecimal(0);
		BigDecimal countPlanedDisburments = new BigDecimal(0);

		BigDecimal proposedProjectCost = new BigDecimal(0);
		BigDecimal grandTotalCost = new BigDecimal(0);
		// for each element get each funding type
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();

			// NO CUMULATIVE VALUES
			if (element.isShow()) {
				totalCommitmentsFiltered = totalCommitmentsFiltered.add(new BigDecimal(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element)));
				actualCommitmentsFiltered = actualCommitmentsFiltered.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element)));
				actualDisburmentsFiltered = actualDisburmentsFiltered.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element)));
				plannedCommitmentsFiltered = plannedCommitmentsFiltered.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element)));
				plannedDisburmentsFiltered = plannedDisburmentsFiltered.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element)));
			}

			totalActualCommitments = totalActualCommitments.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			totalActualDisburments = totalActualDisburments.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluateOriginalvalue(element)));
			totalPlannedCommitments = totalPlannedCommitments.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			totalPlannedDisburments = totalPlannedDisburments.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluateOriginalvalue(element)));

			actualCommitments = actualCommitments.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element)));
			actualDisburments = actualDisburments.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element)));
			plannedCommitments = plannedCommitments.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element)));
			plannedDisburments = plannedDisburments.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element)));

			if (element.existsMetaString(ArConstants.ADJUSTMENT_TYPE)) {

				if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.ACTUAL)) {
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
						countActualCommitments = countActualCommitments.add(new BigDecimal(1));
					}
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
						countActualDisburments = countActualDisburments.add(new BigDecimal(1));
					}

				}

				if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.PLANNED)) {
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
						countPlanedCommitments = countPlanedCommitments.add(new BigDecimal(1));
					}
					if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
						countPlanedDisburments = countPlanedDisburments.add(new BigDecimal(1));
					}
				}
			} else if (element.existsMetaString(ArConstants.PROPOSED_COST)) {
				proposedProjectCost = proposedProjectCost.add(new BigDecimal(TokenRepository.buildUncommittedLogicalToken().evaluate(element)));
			} else if (element.existsMetaString(ArConstants.COSTING_GRAND_TOTAL)) {
				grandTotalCost = grandTotalCost.add(new BigDecimal(TokenRepository.buildCostingGrandTotalToken().evaluate(element)));
			}

		}
		// NO CUMULATIVE VALUES (affected by filters an %)
		values.put(ArConstants.ACTUAL_COMMITMENT_FILTERED, actualCommitmentsFiltered);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_FILTERED, actualDisburmentsFiltered);
		values.put(ArConstants.PLANNED_COMMITMENT_FILTERED, plannedCommitmentsFiltered);
		values.put(ArConstants.PLANNED_DISBURSEMENT_FILTERED, plannedDisburmentsFiltered);
		// cumulative values all transactions and original values

		values.put(ArConstants.TOTAL_ACTUAL_COMMITMENT, totalActualCommitments);
		values.put(ArConstants.TOTAL_ACTUAL_DISBURSEMENT, totalActualDisburments);
		values.put(ArConstants.TOTAL_PLANNED_COMMITMENT, totalPlannedCommitments);
		values.put(ArConstants.TOTAL_PLANNED_DISBURSEMENT, totalPlannedDisburments);

		values.put(ArConstants.ACTUAL_COMMITMENT, actualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT, actualDisburments);
		values.put(ArConstants.PLANNED_COMMITMENT, plannedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT, plannedDisburments);

		values.put(ArConstants.ACTUAL_COMMITMENT_COUNT, countActualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, countActualDisburments);

		values.put(ArConstants.PLANNED_COMMITMENT_COUNT, countPlanedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, countPlanedDisburments);
		values.put(ArConstants.PROPOSED_COST, proposedProjectCost);
		values.put(ArConstants.COSTING_GRAND_TOTAL, grandTotalCost);

		return values;
	}

	public static HashMap<String, BigDecimal> mergeGroupVaules(ComputedAmountCell c1, ComputedAmountCell c2) {
		HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

		values.put(ArConstants.MAX_ACTUAL_COMMITMENT, c1.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT));
		if (c1.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT)==null){
			values.put(ArConstants.MAX_ACTUAL_COMMITMENT, c2.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT));
		}else if (c1.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT).compareTo(c2.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT)) < 0) {
			values.put(ArConstants.MAX_ACTUAL_COMMITMENT, c2.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT));
		}

		values.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, c1.getValues().get(ArConstants.MAX_ACTUAL_DISBURSEMENT));
		
		if (c1.getValues().get(ArConstants.MAX_ACTUAL_DISBURSEMENT)==null){
			values.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, c2.getValues().get(ArConstants.MAX_ACTUAL_DISBURSEMENT));
		}else if (c1.getValues().get(ArConstants.MAX_ACTUAL_COMMITMENT).compareTo(c2.getValues().get(ArConstants.MAX_ACTUAL_DISBURSEMENT)) < 0) {
			values.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, c2.getValues().get(ArConstants.MAX_ACTUAL_DISBURSEMENT));
		}
		values.put(ArConstants.MAX_PLANNED_COMMITMENT, c1.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT));
		if (c1.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT)==null){
			values.put(ArConstants.MAX_PLANNED_COMMITMENT, c2.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT));
		}else if (c1.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT).compareTo(c2.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT)) < 0) {
			values.put(ArConstants.MAX_PLANNED_COMMITMENT, c2.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT));
		}

		values.put(ArConstants.MAX_PLANNED_DISBURSEMENT, c1.getValues().get(ArConstants.MAX_PLANNED_DISBURSEMENT));
		if (c1.getValues().get(ArConstants.MAX_PLANNED_DISBURSEMENT)==null){
			values.put(ArConstants.MAX_PLANNED_DISBURSEMENT, c2.getValues().get(ArConstants.MAX_PLANNED_DISBURSEMENT));
		}else if (c1.getValues().get(ArConstants.MAX_PLANNED_COMMITMENT).compareTo(c2.getValues().get(ArConstants.MAX_PLANNED_DISBURSEMENT)) < 0) {
			values.put(ArConstants.MAX_PLANNED_DISBURSEMENT, c2.getValues().get(ArConstants.MAX_PLANNED_DISBURSEMENT));
		}

		values.put(ArConstants.MIN_ACTUAL_COMMITMENT, c1.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT));
		if (c1.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT)==null){
			values.put(ArConstants.MIN_ACTUAL_COMMITMENT, c2.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT));
		}else if (c1.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT).compareTo(c2.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT)) > 0) {
			values.put(ArConstants.MIN_ACTUAL_COMMITMENT, c2.getValues().get(ArConstants.MIN_ACTUAL_COMMITMENT));
		}

		values.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, c1.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT));
		if (c1.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT)==null){
			values.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, c2.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT));
		}else if (c1.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT).compareTo(c2.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT)) > 0) {
			values.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, c2.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT));
		}

		values.put(ArConstants.MIN_PLANNED_COMMITMENT, c1.getValues().get(ArConstants.MIN_PLANNED_COMMITMENT));
		if (c1.getValues().get(ArConstants.MIN_PLANNED_COMMITMENT)==null){
			values.put(ArConstants.MIN_PLANNED_COMMITMENT, c2.getValues().get(ArConstants.MIN_PLANNED_COMMITMENT));
		}else if (c1.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT).compareTo(c2.getValues().get(ArConstants.MIN_PLANNED_COMMITMENT)) > 0) {
			values.put(ArConstants.MIN_PLANNED_COMMITMENT, c2.getValues().get(ArConstants.MIN_PLANNED_COMMITMENT));
		}

		values.put(ArConstants.MIN_PLANNED_DISBURSEMENT, c1.getValues().get(ArConstants.MIN_PLANNED_DISBURSEMENT));
		if (c1.getValues().get(ArConstants.MIN_PLANNED_DISBURSEMENT)==null){
			values.put(ArConstants.MIN_PLANNED_DISBURSEMENT, c2.getValues().get(ArConstants.MIN_PLANNED_DISBURSEMENT));
		}else if (c1.getValues().get(ArConstants.MIN_ACTUAL_DISBURSEMENT).compareTo(c2.getValues().get(ArConstants.MIN_PLANNED_DISBURSEMENT)) > 0) {
			values.put(ArConstants.MIN_PLANNED_DISBURSEMENT, c2.getValues().get(ArConstants.MIN_PLANNED_DISBURSEMENT));
		}
	if (c1.getValues().get(ArConstants.COUNT_PROJECTS)==null){
		values.put(ArConstants.COUNT_PROJECTS, c2.getValues().get(ArConstants.COUNT_PROJECTS));
	}else{
		values.put(ArConstants.COUNT_PROJECTS, c1.getValues().get(ArConstants.COUNT_PROJECTS).add(c2.getValues().get(ArConstants.COUNT_PROJECTS)));
	}
	return values;
	}
}
