package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.*;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The behaviour of a trivial measure. <br />
 * A trivial measure is one which obeys the report's time-splitting settings {@link GroupingCriteria}, obeys percentages
 * and does both horizontal and vertical reduction through addition
 * @author Dolghier Constantin
 *
 */
public class TrivialMeasureBehaviour implements Behaviour<NiAmountCell> {
    public static TrivialMeasureBehaviour getInstance() {return instance;}
    public static TrivialMeasureBehaviour getTotalsOnlyInstance() {return totalsOnlyInstance;}
    
    protected final TimeRange timeRange;

    /**
     * Specified whenever cells produced by this behaviour are scalable by units.
     * See also {@link ReportSettings#getUnitsOption()}.
     */
    protected final boolean isScalableByUnits;
    
    /**
     * horizReductionResult = f(engine, doHorizResult)
     */
    protected final BiFunction<NiReportsEngine, BigDecimal, BigDecimal> horizResultPostprocessor; 
    
    private final static TrivialMeasureBehaviour instance = new TrivialMeasureBehaviour();
    private final static TrivialMeasureBehaviour totalsOnlyInstance = new TrivialMeasureBehaviour(TimeRange.NONE);
    
    public TrivialMeasureBehaviour() {
        this(TimeRange.MONTH);
    }
    
    public TrivialMeasureBehaviour(TimeRange timeRange) {
        this(timeRange, null);
    }
    
    public TrivialMeasureBehaviour(TimeRange timeRange,
                                   BiFunction<NiReportsEngine, BigDecimal, BigDecimal> horizResultPostprocessor) {
        this(timeRange, horizResultPostprocessor, true);
    }

    public TrivialMeasureBehaviour(TimeRange timeRange,
                                   BiFunction<NiReportsEngine, BigDecimal, BigDecimal> horizResultPostprocessor,
                                   boolean isScalableByUnits) {
        this.timeRange = timeRange;
        this.horizResultPostprocessor = horizResultPostprocessor;
        this.isScalableByUnits = isScalableByUnits;
    }
        
    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }
    
    @Override
    public NiAmountCell doHorizontalReduce(List<NiCell> cells) {
        NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
        BigDecimal res = precision.adjustPrecision(BigDecimal.ZERO);
        for(NiCell cell:cells) {
            BigDecimal toAdd = cell.getAmount();
            res = res.add(toAdd);
        }
        //System.err.format("reduced %d cells to %.2f: %s\n", cells.size(), res.doubleValue(), cells.toString());
        return new NiAmountCell(res, precision, isScalableByUnits);
    }

    @Override
    public NiAmountCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        NiAmountCell z = doHorizontalReduce(cells);
        if (z != null && horizResultPostprocessor != null) {
            // a postprocessing func has been specified -> run it over the output and return either the result of postprocessing or an enveloped (empty) in case formula fails
            BigDecimal zz = horizResultPostprocessor.apply(context, z.amount);
            if (zz == null)
                return NiFormulaicAmountCell.FORMULAIC_ZERO;
            return new NiAmountCell(zz, z.precisionSetting, isScalableByUnits);
        }
        return z;
    }

    @Override
    public NiAmountCell getZeroCell() {
        return NiAmountCell.ZERO;
    }
    
    @Override
    public NiAmountCell doVerticalReduce(Collection<NiAmountCell> cells) {
        if (cells.isEmpty()) {
            return getZeroCell();
        }
        
        java.util.Iterator<NiAmountCell> it = cells.iterator();
        NiAmountCell first = it.next();
        NiPrecisionSetting precisionSetting = first.getPrecision();
        BigDecimal res = first.getAmount();
        
        while(it.hasNext())
            res = res.add(it.next().amount);
        return new NiAmountCell(res, precisionSetting, isScalableByUnits);
    }

    @Override
    public NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
        throw new RuntimeException("doing hierarchies by numeric values not supported");
    }

    @Override
    public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
        throw new RuntimeException("doing hierarchies by numeric values not supported");
    }

    @Override
    public boolean isKeepingSubreports() {
        return true;
    }

    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        // trivial measures are copied verbatim to totals
        return new ImmutablePair<String, ColumnContents>(entity.name, fetchedContents);
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return NiAmountCell.ZERO;
    }

    @Override
    public boolean hasPercentages() {
        return false;
    }

    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return true;
    }
    
    /**
     * builds a horizResultPostprocessor which divides the result by the report-wide total total
     * @param measureName the measure by whose sum to divide 
     * @return
     */
    public static BiFunction<NiReportsEngine, BigDecimal, BigDecimal> buildMeasureTotalDivider(String measureName) {
        return (engine, rawValue) -> 
            NiFormula.PERCENTAGE(NiFormula.CONSTANT(rawValue), NiFormula.CONSTANT(engine.fetchedMeasures.get(measureName).getSumOfValues()))
                .evaluateOrUndefined(Collections.emptyMap(), null);
    }
    
    @Override
    public boolean canBeSplitByCurrency() {
        return true;
    }
}
