package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.Iterator;

import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.Values;
import org.digijava.module.aim.helper.FormatHelper;

public class ComputedMeasureCell extends AmountCell {

	Values values = null;

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

		MathExpression math = null;
		if (this.getColumn().getExpression() != null) {
			math = MathExpressionRepository.get(this.getColumn().getExpression());
		} else {
			math = MathExpressionRepository.get(this.getColumn().getWorker().getRelatedColumn().getTokenExpression());
		}
		BigDecimal val=math.result(getValues());
		if (val!=null){
			return val.doubleValue();
		}else{
			return 0d;
		}
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

	public Values getValues() {
		if (values == null) {
			values = new Values();
			Iterator<CategAmountCell> i = mergedCells.iterator();
			while (i.hasNext()) {
				values.collectCellVariables((CategAmountCell) i.next());
			}
		}
		return values;
	}
}
