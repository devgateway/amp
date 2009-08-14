package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.exprlogic.ExpressionHelper;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.TokenRepository;
import org.digijava.module.aim.helper.FormatHelper;

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

		values.putAll(ExpressionHelper.getMeasuresVariables(mergedCells));

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

	public String toString() {
		return FormatHelper.formatNumberUsingCustomFormat(getAmount());
	}
}
