package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.TokenRepository;

public class ComputedMeasureCell extends AmountCell {

	HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

	public ComputedMeasureCell() {
		super();
	}

	public ComputedMeasureCell(AmountCell ac) {
		super(ac.getOwnerId());
		this.mergedCells = ac.getMergedCells();
	}

	/**
	 * @param id
	 */
	public ComputedMeasureCell(Long id) {
		super(id);
	}

	public void collectValues() {

		Iterator<CategAmountCell> i = mergedCells.iterator();
		// total
		BigDecimal totalCommitments = new BigDecimal(0);
		BigDecimal totalCommitmentsNF = new BigDecimal(0);
		// planed
		BigDecimal actualCommitments = new BigDecimal(0);
		BigDecimal actualCommitmentsNF = new BigDecimal(0);
		BigDecimal actualDisburments = new BigDecimal(0);
		BigDecimal actualDisburmentsNF = new BigDecimal(0);
		// actual
		BigDecimal plannedCommitmentsNF = new BigDecimal(0);
		BigDecimal plannedCommitments = new BigDecimal(0);
		BigDecimal plannedDisburments = new BigDecimal(0);
		BigDecimal plannedDisburmentsNF = new BigDecimal(0);

		BigDecimal countActualCommitments = new BigDecimal(0);
		BigDecimal countActualDisburments = new BigDecimal(0);
		BigDecimal countPlanedCommitments = new BigDecimal(0);
		BigDecimal countPlanedDisburments = new BigDecimal(0);

		// for each element get each funding type
		while (i.hasNext()) {
			CategAmountCell element = (CategAmountCell) i.next();

			// NO CUMULATIVE VALUES
			if (element.isShow()) {
				totalCommitments = totalCommitments.add(new BigDecimal(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element)));
				actualCommitments = actualCommitments.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element)));
				actualDisburments = actualDisburments.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element)));
				plannedCommitments = plannedCommitments.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element)));
				plannedDisburments = plannedDisburments.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element)));
			}
			totalCommitmentsNF = totalCommitmentsNF.add(new BigDecimal(TokenRepository.buildTotalCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			actualCommitmentsNF = actualCommitmentsNF.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			actualDisburmentsNF = actualDisburmentsNF.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluateOriginalvalue(element)));
			plannedCommitmentsNF = plannedCommitmentsNF.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluateOriginalvalue(element)));
			plannedDisburmentsNF = plannedDisburmentsNF.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluateOriginalvalue(element)));

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

		}

		// NO CUMULATIVE VALUES (affected by filters an %)
		values.put(ArConstants.TOTAL_COMMITMENTS, totalCommitments);
		values.put(ArConstants.ACTUAL_COMMITMENT, actualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT, actualDisburments);
		values.put(ArConstants.PLANNED_COMMITMENT, plannedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT, plannedDisburments);

		// cumulative values all transactions and original values
		values.put(ArConstants.ACTUAL_COMMITMENT_NF, actualCommitmentsNF);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_NF, actualDisburmentsNF);
		
		values.put(ArConstants.PLANNED_COMMITMENT_NF, plannedCommitmentsNF);
		values.put(ArConstants.PLANNED_DISBURSEMENT_NF, plannedDisburmentsNF);

		values.put(ArConstants.ACTUAL_COMMITMENT_COUNT, countActualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, countActualDisburments);

		values.put(ArConstants.PLANNED_COMMITMENT_COUNT, countPlanedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, countPlanedDisburments);
	}

	/**
	 * Overrider of the normal behavior of AmountCell.getAmount. This will take
	 * into consideration only undisbursed related merged cells and will also
	 * make the required calculations
	 * 
	 * @return Returns the amount.
	 */
	public double getAmount() {
		BigDecimal ret = new BigDecimal(0);
		if (id != null)
			return (convert() * (getPercentage() / 100));
		collectValues();

		MathExpression math = null;

		if (this.getColumn().getExpression() != null) {
			math = MathExpressionRepository.get(this.getColumn().getExpression());
		} else {
			math = MathExpressionRepository.get(this.getColumn().getWorker().getRelatedColumn().getTokenExpression());
		}
		return math.result(values).doubleValue();

	}

	public Cell merge(Cell c) {
		AmountCell ac = (AmountCell) super.merge(c);
		ComputedMeasureCell uac = new ComputedMeasureCell(ac);
		return uac;
	}

	public Cell newInstance() {
		return new ComputedMeasureCell();
	}

}
