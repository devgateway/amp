package org.dgfoundation.amp.exprlogic;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;

public class ExpressionHelper {

	/**
	 * 
	 * @param items
	 * @param ac
	 * @param rowExpression
	 * @return
	 */
	public static HashMap<String, BigDecimal> getGroupVariable(Collection<ComputedAmountCell> items, ComputedAmountCell ac, String rowExpression) {

		BigDecimal sumOfResults = new BigDecimal(0);

		BigDecimal trail_total_commitments = new BigDecimal(0);
		BigDecimal trail_acutal_commitments = new BigDecimal(0);
		BigDecimal trail_planned_commitments = new BigDecimal(0);
		BigDecimal trail_acutal_disburments = new BigDecimal(0);
		BigDecimal trail_planned_disburments = new BigDecimal(0);

		BigDecimal max_acutal_commitments = new BigDecimal(0);
		BigDecimal min_acutal_commitments = new BigDecimal(0);

		BigDecimal max_planned_commitments = new BigDecimal(0);
		BigDecimal min_planned_commitments = new BigDecimal(0);

		BigDecimal max_acutal_disburments = new BigDecimal(0);
		BigDecimal min_acutal_disburments = new BigDecimal(0);

		BigDecimal max_planned_disburments = new BigDecimal(0);
		BigDecimal min_planned_disburments = new BigDecimal(0);

		Iterator i = items.iterator();

		while (i.hasNext()) {
			Object el = i.next();
			ComputedAmountCell element = (ComputedAmountCell) el;
			ac.merge(element, ac);
			// collect values
			element.getAmount();

			if (rowExpression != null) {
				sumOfResults = sumOfResults.add(MathExpressionRepository.get(rowExpression).result(element.getValues()));
			}

			BigDecimal tmpValue = element.getValues().get(ArConstants.TOTAL_COMMITMENTS);
			BigDecimal tmpMinValue;
			trail_total_commitments = trail_total_commitments.add(tmpValue);
			tmpValue = null;

			// ACTUAL_COMMITMENT
			tmpValue = element.getValues().get(ArConstants.ACTUAL_COMMITMENT);
			trail_acutal_commitments = trail_acutal_commitments.add(tmpValue);
			max_acutal_commitments = (max_acutal_commitments.compareTo(tmpValue) < 0) ? tmpValue : max_acutal_commitments;
			if (min_acutal_commitments.intValue() == 0) {
				min_acutal_commitments = max_acutal_commitments;
			}
			min_acutal_commitments = (min_acutal_commitments.compareTo(tmpValue) > 0) ? tmpValue : min_acutal_commitments;
			tmpValue = null;

			// ACTUAL_DISBURSEMENT
			tmpValue = element.getValues().get(ArConstants.ACTUAL_DISBURSEMENT);
			trail_acutal_disburments = trail_acutal_disburments.add(tmpValue);
			max_acutal_disburments = (max_acutal_disburments.compareTo(tmpValue) < 0) ? tmpValue : max_acutal_disburments;
			if (min_acutal_disburments.intValue() == 0) {
				min_acutal_disburments = max_acutal_disburments;
			}
			min_acutal_disburments = (min_acutal_disburments.compareTo(tmpValue) > 0) ? tmpValue : min_acutal_disburments;
			tmpValue = null;

			// PLANNED_COMMITMENT
			tmpValue = element.getValues().get(ArConstants.PLANNED_COMMITMENT);
			// planned_commitments
			trail_planned_commitments = trail_planned_commitments.add(tmpValue);
			max_planned_commitments = (max_planned_commitments.compareTo(tmpValue) < 0) ? tmpValue : max_planned_commitments;

			if (min_planned_commitments.intValue() == 0) {
				min_planned_commitments = max_planned_commitments;
			}
			min_planned_commitments = (min_planned_commitments.compareTo(tmpValue) > 0) ? tmpValue : min_planned_commitments;
			tmpValue = null;

			// PLANNED_DISBURSEMENT
			tmpValue = element.getValues().get(ArConstants.PLANNED_DISBURSEMENT);
			trail_planned_disburments = trail_planned_disburments.add(tmpValue);
			// max_acutal_commitments compare to value if -1 max is less if 1
			// max is greater
			max_planned_disburments = (max_planned_disburments.compareTo(tmpValue) < 0) ? tmpValue : max_planned_disburments;
			// min_acutal_commitments compare to value if -1 min is less, if 1
			// min is greater
			if (min_planned_disburments.intValue() == 0) {
				min_planned_disburments = max_planned_disburments;
			}
			min_planned_disburments = (min_planned_disburments.compareTo(tmpValue) > 0) ? tmpValue : min_planned_disburments;

		}

		HashMap<String, BigDecimal> variables = new HashMap<String, BigDecimal>();

		variables.put(ArConstants.TOTAL_COMMITMENTS, trail_total_commitments);
		variables.put(ArConstants.ACTUAL_COMMITMENT, trail_acutal_commitments);
		variables.put(ArConstants.ACTUAL_DISBURSEMENT, trail_acutal_disburments);

		variables.put(ArConstants.PLANNED_COMMITMENT, trail_planned_commitments);
		variables.put(ArConstants.PLANNED_DISBURSEMENT, trail_planned_disburments);

		variables.put(ArConstants.COUNT_PROJECTS, new BigDecimal(items.size()));

		variables.put(ArConstants.MAX_ACTUAL_COMMITMENT, max_acutal_commitments);
		variables.put(ArConstants.MIN_ACTUAL_COMMITMENT, min_acutal_commitments);

		variables.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, max_acutal_disburments);
		variables.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, min_acutal_disburments);

		variables.put(ArConstants.MAX_PLANNED_COMMITMENT, max_planned_commitments);
		variables.put(ArConstants.MIN_PLANNED_COMMITMENT, min_planned_commitments);

		variables.put(ArConstants.MAX_PLANNED_DISBURSEMENT, max_planned_disburments);
		variables.put(ArConstants.MIN_PLANNED_DISBURSEMENT, min_planned_disburments);

		variables.put(ArConstants.SUM_OFF_RESULTS, sumOfResults);

		return variables;
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
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
		// for each element get each funding type
		while (i.hasNext()) {
			ComputedAmountCell element = (ComputedAmountCell) i.next();
			// using the logicExpression we will get the result of each funding
			// type
			totalCommitments = totalCommitments.add(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element));
			actualCommitments = actualCommitments.add(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element));
			actualDisburments = actualDisburments.add(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element));
			plannedCommitments = plannedCommitments.add(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element));
			plannedDisburments = plannedDisburments.add(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element));
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
				proposedProjectCost = proposedProjectCost.add(TokenRepository.buildUncommittedLogicalToken().evaluate(element));
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

		// for each element get each funding type
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();

			// NO CUMULATIVE VALUES
			if (element.isShow()) {
				totalCommitmentsFiltered = totalCommitmentsFiltered.add(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element));
				actualCommitmentsFiltered = actualCommitmentsFiltered.add(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element));
				actualDisburmentsFiltered = actualDisburmentsFiltered.add(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element));
				plannedCommitmentsFiltered = plannedCommitmentsFiltered.add(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element));
				plannedDisburmentsFiltered = plannedDisburmentsFiltered.add(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element));
			}

			totalActualCommitments = totalActualCommitments.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			totalActualDisburments = totalActualDisburments.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluateOriginalvalue(element)));
			totalPlannedCommitments = totalPlannedCommitments.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			totalPlannedDisburments = totalPlannedDisburments.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluateOriginalvalue(element)));

			actualCommitments = actualCommitments.add(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element));
			actualDisburments = actualDisburments.add(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element));
			plannedCommitments = plannedCommitments.add(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element));
			plannedDisburments = plannedDisburments.add(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element));

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
				proposedProjectCost = proposedProjectCost.add(TokenRepository.buildUncommittedLogicalToken().evaluate(element));
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
		return values;
	}
}
