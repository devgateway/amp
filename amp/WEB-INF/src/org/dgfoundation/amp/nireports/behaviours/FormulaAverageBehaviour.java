package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiFormulaicMeasure;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * the behaviour of an entity which has value = formula(tokens) for cells, but outputs average(formula) in the trails (and maybe the original cells).
 * The trail cells get their tokens' values from V-reduction.
 * See {@link FormulaicAmountBehaviour} as the base code - this class is a partial ripoff/copypaste off it
 * @author Dolghier Constantin
 */
public class FormulaAverageBehaviour extends AbstractComputedBehaviour<NiFormulaicAmountCell> {
    
    /**
     * the functions (one for each dependency-variable) used to reduce multiple values to a single one
     */
    final Map<String, Function<List<BigDecimal>, BigDecimal>> reductors;
    
    /**
     * the expression tree which drives the behaviour
     */
    final NiFormula formula;
    
    /**
     * whether to output values in individual cells
     */
    final boolean valuesInCells;

    /**
     * Specified whenever cells produced by this behaviour are scalable by units.
     * See also {@link ReportSettings#getUnitsOption()}.
     */
    private final boolean isScalableByUnits;

    public FormulaAverageBehaviour(TimeRange timeRange, 
            Map<String, Function<List<BigDecimal>, BigDecimal>> reductors,
            boolean valuesInCells,
            NiFormula formula,
            boolean isScalableByUnits) {
        super(timeRange);
        this.reductors = reductors;
        this.formula = formula;
        this.valuesInCells = valuesInCells;
        this.isScalableByUnits = isScalableByUnits;
    }
    
    /**
     * combines multiple elementary cells into one {@link NiFormulaicAmountCell}. It has 3 functions to fulfill: 
     * <ul>
     * <li>group NiCell by the tag of the enclosed cell and separate them into lists</li>
     * <li>call the respective reductor on each of the lists, thus forming the vars-values</li>
     * <li>evaluate the expression tree on the given vars-values and return a cell with the built value</li>
     * </ul>
     */
    @Override
    public NiFormulaicAmountCell doHorizontalReduce(List<NiCell> cells) {
        Map<String, BigDecimal> vals = AmpCollections.remap(
                cells.stream().collect(Collectors.groupingBy(this::extractCellTag)), 
                (entity, list) -> reductors.get(entity).apply(AmpCollections.relist(list, NiCell::getAmount)), 
                null);

        NiPrecisionSetting precision = cells.isEmpty() ? NiPrecisionSetting.IDENTITY_PRECISION_SETTING : ((NumberedCell) cells.get(0).getCell()).getPrecision();
        BigDecimal numericValue = formula.evaluate(vals);
        if (NiFormulaicAmountCell.isDefined(numericValue)) {
            Map<String, BigDecimal> values = new HashMap<>();
            values.put("sum", numericValue);
            values.put("num", BigDecimal.ONE);
            return new NiFormulaicAmountCell(values, valuesInCells ? numericValue : null, precision, isScalableByUnits);
        }
        return new NiFormulaicAmountCell(Collections.emptyMap(), null, precision, isScalableByUnits);
    }
    
    protected String extractCellTag(NiCell cell) {
        CategAmountCell cac = (CategAmountCell) cell.getCell();
        return cac.metaInfo.getMetaInfo(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE).v.toString();
    }
    
    /**
     * computes the average of the values computed in the individual cells
     */
    @Override
    public NiFormulaicAmountCell doVerticalReduce(Collection<NiFormulaicAmountCell> cells) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal num = BigDecimal.ZERO;
        for(NiFormulaicAmountCell cell:cells)
            if (cell != null && !cell.values.isEmpty()) {
                sum = sum.add(cell.values.get("sum"));
                num = num.add(cell.values.get("num"));
            }
        
        Map<String, BigDecimal> z = new HashMap<>();
        z.put("sum", sum);
        z.put("num", num);
        if (num.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal res = sum.divide(num, NiFormula.DIVISION_MC);
            //System.out.format("the average of %d items is %.2f\n", num.intValue(), res.doubleValue());
            return new NiFormulaicAmountCell(z, res, NiPrecisionSetting.IDENTITY_PRECISION_SETTING, isScalableByUnits);
        }
        return getZeroCell();
    }

    @Override
    public NiFormulaicAmountCell getZeroCell() {
        return NiFormulaicAmountCell.FORMULAIC_ZERO;
    }
}
