package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * the behaviour of an entity which has value = formula(tokens). The trail cells get their tokem from V-reduction
 * @author Dolghier Constantin
 *
 */
public class FormulaicAmountBehaviour extends AbstractComputedBehaviour<NiFormulaicAmountCell> {
	
	final Map<String, Function<List<BigDecimal>, BigDecimal>> reductors;
	final NiFormula formula;
	
	public FormulaicAmountBehaviour(TimeRange timeRange, 
			Map<String, Function<List<BigDecimal>, BigDecimal>> reductors,
			NiFormula formula) {
		super(timeRange);
		this.reductors = reductors;
		this.formula = formula;
	}
	
	@Override
	public NiFormulaicAmountCell doHorizontalReduce(List<NiCell> cells) {
		Map<String, BigDecimal> vals = AmpCollections.remap(cells.stream().collect(Collectors.groupingBy(
				cell -> cell.getEntity().name)), 
				(entity, list) -> reductors.get(entity).apply(AmpCollections.relist(list, NiCell::getAmount)), 
				null);

		NiPrecisionSetting precision = cells.isEmpty() ? NiPrecisionSetting.IDENTITY_PRECISION_SETTING : ((NumberedCell) cells.get(0).getCell()).getPrecision();
		return new NiFormulaicAmountCell(vals, formula.evaluate(vals), precision);
	}
	
	@Override
	public NiFormulaicAmountCell doVerticalReduce(Collection<NiFormulaicAmountCell> cells) {
		return getZeroCell();
	}

	@Override
	public NiFormulaicAmountCell getZeroCell() {
		return NiFormulaicAmountCell.FORMULAIC_ZERO;
	}
	
	/**
	 * a reductor which sums up the values
	 * @param l
	 * @return
	 */
	public static BigDecimal REDUCE_SUM(List<BigDecimal> l) {
		if (l.isEmpty()) return BigDecimal.ZERO;
		
		if (l.size() == 1) return l.get(0);
		
		BigDecimal res = BigDecimal.ZERO;
		for(BigDecimal term:l) {
			res = res.add(term);
		}
		return res;
	}
}
