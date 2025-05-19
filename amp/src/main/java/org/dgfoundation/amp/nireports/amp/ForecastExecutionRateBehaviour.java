package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.behaviours.AbstractComputedBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiFormulaicMeasure;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * the Forecast Execution Rate behaviour
 * @author Dolghier Constantin
 *
 */
public class ForecastExecutionRateBehaviour extends AbstractComputedBehaviour<NiFormulaicAmountCell> {
    
    public final static ForecastExecutionRateBehaviour instance = new ForecastExecutionRateBehaviour(TimeRange.NONE);

    /**
     * Specified whenever cells produced by this behaviour are scalable by units.
     * See also {@link ReportSettings#getUnitsOption()}.
     */
    private final boolean isScalableByUnits = false;

    public ForecastExecutionRateBehaviour(TimeRange timeRange) {
        super(timeRange);
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
        Map<String, BigDecimal> vals = buildVals(cells);
        NiPrecisionSetting precision = cells.isEmpty() ? NiPrecisionSetting.IDENTITY_PRECISION_SETTING : ((NumberedCell) cells.get(0).getCell()).getPrecision();
        return buildCell(vals, precision);
    }

    protected Map<String, BigDecimal> buildVals(List<NiCell> cells) {
        Map<String, List<NiCell>> distr = cells.stream().collect(Collectors.groupingBy(this::extractCellTag));
        Map<String, BigDecimal> res = AmpCollections.remap(distr, this::sumCells, null);
        return res;
    }
    
    protected BigDecimal sumCells(List<NiCell> l) {
        return REDUCE_SUM(l.stream().map(NiCell::getAmount).collect(Collectors.toList()));
    }
    
    public final static NiFormula CALCULATOR = NiFormula.PERCENTAGE(NiFormula.VARIABLE("actdisb"), NiFormula.VARIABLE("mtef"));
    
    public NiFormulaicAmountCell buildCell(Map<String, BigDecimal> vals, NiPrecisionSetting precision) {
        Map<Integer, BigDecimal> pipe = new HashMap<>(), proj = new HashMap<>();
        ValueWrapper<BigDecimal> actDisb = new ValueWrapper<>(BigDecimal.ZERO);
        
        vals.forEach((tag, val) -> {
            if (tag.equals(MeasureConstants.ACTUAL_DISBURSEMENTS))
                actDisb.set(actDisb.value.add(val));
            else {
                String prefix = tag.substring(0, 4);
                int year = Integer.valueOf(tag.substring(4));
                Map<Integer, BigDecimal> map = prefix.equals("pipe") ? pipe : proj;
                map.put(year, map.getOrDefault(year, BigDecimal.ZERO).add(val));
            }
        });
        
        BigDecimal totalMtef = BigDecimal.ZERO;
        for(int year:AmpCollections.union(pipe.keySet(), proj.keySet())) {
            totalMtef = totalMtef.add(AmpCollections.firstOf(pipe.get(year), proj.get(year)));
        }
        Map<String, BigDecimal> vv = new HashMap<>();
        vv.put("mtef", totalMtef);
        vv.put("actdisb", actDisb.value);
        
        BigDecimal numericValue = CALCULATOR.evaluateOrUndefined(vv, null);
        if (numericValue != null)
            return new NiFormulaicAmountCell(vals, numericValue, precision, isScalableByUnits);
        else
            return buildNoValueCell(vals);
    }
    
    protected String extractCellTag(NiCell cell) {
        CategAmountCell cac = (CategAmountCell) cell.getCell();
        return cac.metaInfo.getMetaInfo(NiFormulaicMeasure.METAINFO_KEY_UNDERLYING_MEASURE).v.toString();
    }
    
    /**
     * does vertical reduction based on a formula. This is a 3-step process: 
     * <ul>
     * <li>collect the respective values from cells, grouping them by tag</li>
     * <li>call the respective reductor on each of the lists, thus forming the vars-values</li>
     * <li>evaluate the expression tree on the given vars-values and return a cell with the built value</li>
     * </ul>
     * You might notice that steps 2-3 are identical to the ones of {@link #doHorizontalReduce(List)}
     */
    @Override
    public NiFormulaicAmountCell doVerticalReduce(Collection<NiFormulaicAmountCell> cells) {
        Map<String, List<BigDecimal>> valsA = new HashMap<>();
        for(NiFormulaicAmountCell cell:cells)
            if (cell != null) {
                cell.values.forEach((cat, val) -> {
                    valsA.computeIfAbsent(cat, z -> new ArrayList<>()).add(val);
                });
            }
        Map<String, BigDecimal> vals = AmpCollections.remap(valsA, (entity, list) -> REDUCE_SUM(list), null);
        NiFormulaicAmountCell res = buildCell(vals, NiPrecisionSetting.IDENTITY_PRECISION_SETTING);
        return res == null ? getZeroCell() : res;
    }

    @Override
    public NiFormulaicAmountCell getZeroCell() {
        return NiFormulaicAmountCell.FORMULAIC_ZERO;
    }
    
    public NiFormulaicAmountCell buildNoValueCell(Map<String, BigDecimal> vals) {
        return new NiFormulaicAmountCell(vals, null, NiPrecisionSetting.IDENTITY_PRECISION_SETTING, isScalableByUnits);
    }

}
