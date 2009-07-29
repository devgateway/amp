/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell;
import org.dgfoundation.amp.ar.cell.UndisbursedAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedCountingAmountColumn extends TotalAmountColumn {

	/**
	 * @param worker
	 */
	public TotalComputedCountingAmountColumn(ColumnWorker worker) {
		super(worker);

	}

	public TotalComputedCountingAmountColumn(String name, boolean filterShowable, int initialCapacity) {
		super(name, filterShowable, initialCapacity);
	}

	/**
	 * @param name
	 */
	public TotalComputedCountingAmountColumn(String name) {
		super(name);

	}

	/**
	 * @param name
	 * @param filterShowable
	 */
	public TotalComputedCountingAmountColumn(String name, boolean filterShowable) {
		super(name, filterShowable);

	}

	/**
	 * @param parent
	 * @param name
	 */
	public TotalComputedCountingAmountColumn(Column parent, String name) {
		super(parent, name);

	}

	/**
	 * @param source
	 */
	public TotalComputedCountingAmountColumn(Column source) {
		super(source);

	}

	/**
	 * Overrides the method for adding cells, to make sure we add only
	 * UndisbursedAmountCellS
	 * 
	 * @param c
	 *            the cell to be added
	 * @see UndisbursedAmountCell
	 */
	public void addCell(Object c) {
		AmountCell ac = (AmountCell) c;
		ComputedCountingAmountCell uac = new ComputedCountingAmountCell(ac.getOwnerId());
		uac.merge(uac, ac);
		super.addCell(uac);
	}

	public Column newInstance() {
		return new TotalComputedCountingAmountColumn(this);
	}

	/**
	 * Apply counting expression
	 */
	public List getTrailCells() {

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

		ComputedCountingAmountCell ac = new ComputedCountingAmountCell();
		ArrayList<ComputedCountingAmountCell> ar = new ArrayList<ComputedCountingAmountCell>();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Object el = i.next();
			ComputedCountingAmountCell element = (ComputedCountingAmountCell) el;
			ac.merge(element, ac);
			// collect values
			element.getAmount();

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
		// return expression result
		HashMap<String, BigDecimal> variables = new HashMap<String, BigDecimal>();
		// TOTALS
		variables.put(ArConstants.TOTAL_COMMITMENTS, trail_total_commitments);
		variables.put(ArConstants.ACTUAL_COMMITMENT, trail_acutal_commitments);
		variables.put(ArConstants.ACTUAL_DISBURSEMENT, trail_acutal_disburments);

		variables.put(ArConstants.PLANNED_COMMITMENT, trail_planned_commitments);
		variables.put(ArConstants.PLANNED_DISBURSEMENT, trail_planned_disburments);

		// Count of projects
		variables.put(ArConstants.COUNT_PROJECTS, new BigDecimal(items.size()));

		// MAX AND MIN VALUES
		variables.put(ArConstants.MAX_ACTUAL_COMMITMENT, max_acutal_commitments);
		variables.put(ArConstants.MIN_ACTUAL_COMMITMENT, min_acutal_commitments);

		variables.put(ArConstants.MAX_ACTUAL_DISBURSEMENT, max_acutal_disburments);
		variables.put(ArConstants.MIN_ACTUAL_DISBURSEMENT, min_acutal_disburments);

		variables.put(ArConstants.MAX_PLANNED_COMMITMENT, max_planned_commitments);
		variables.put(ArConstants.MIN_PLANNED_COMMITMENT, min_planned_commitments);

		variables.put(ArConstants.MAX_PLANNED_DISBURSEMENT, max_planned_disburments);
		variables.put(ArConstants.MIN_PLANNED_DISBURSEMENT, min_planned_disburments);

		MathExpression expression = MathExpressionRepository.get(getWorker().getRelatedColumn().getTokenExpression());
		ac.setComputedVaule(expression.result(variables));
		ac.setColumn(this);
		ar.add(ac);
		return ar;
	}
}
