package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import static org.dgfoundation.amp.nireports.amp.MetaCategory.CATEGORY_VALUE_ID;
import static org.dgfoundation.amp.nireports.amp.MetaCategory.CHILD_CATEGORY_VALUE_ID;
import static org.dgfoundation.amp.nireports.amp.MetaCategory.INDICATOR_ID;
import static org.dgfoundation.amp.nireports.amp.MetaCategory.INDICATOR_PROGRAM_ID;

/**
 * Based on the TrivialMeasureBehaviour. <br />
 * We dont want to split indicators by program/theme/msdg automatically (like in fundings).
 */
public class IndicatorMeasureBehaviour implements Behaviour<NiAmountCell> {
    public static IndicatorMeasureBehaviour getInstance() {
        return instance;
    }

    public static IndicatorMeasureBehaviour getTotalsOnlyInstance() {
        return totalsOnlyInstance;
    }

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

    private final static IndicatorMeasureBehaviour instance = new IndicatorMeasureBehaviour();
    private final static IndicatorMeasureBehaviour totalsOnlyInstance = new IndicatorMeasureBehaviour(TimeRange.NONE);

    public IndicatorMeasureBehaviour() {
        this(TimeRange.MONTH);
    }

    public IndicatorMeasureBehaviour(TimeRange timeRange) {
        this(timeRange, null);
    }

    public IndicatorMeasureBehaviour(TimeRange timeRange,
                                     BiFunction<NiReportsEngine, BigDecimal, BigDecimal> horizResultPostprocessor) {
        this(timeRange, horizResultPostprocessor, true);
    }

    public IndicatorMeasureBehaviour(TimeRange timeRange,
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
        return null;
    }

    public NiAmountCell doHorizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
        BigDecimal res = precision.adjustPrecision(BigDecimal.ZERO);
        for (NiCell cell : cells) {
            AtomicReference<BigDecimal> percentage = new AtomicReference<>(new BigDecimal(0));

            Map<NiDimensionUsage, HierarchiesTracker.SplitCellPercentage> hierarchyPercentages =
                    ((HierarchiesTracker) cell.getHiersTracker()).getPercentages();

            if (hierarchyPercentages.isEmpty()) {
                // No active hierarchy split — include at full value (e.g. totals row)
                percentage.set(new BigDecimal(1));
            } else {
                // Walk every dimension in the tracker; require ALL of them to match
                // so that combined hierarchies (e.g. program + disaggregation) work correctly.
                boolean allMatch = true;
                for (Map.Entry<NiDimensionUsage, HierarchiesTracker.SplitCellPercentage> entry
                        : hierarchyPercentages.entrySet()) {
                    NiDimensionUsage dimUsage = entry.getKey();
                    long splitId = entry.getValue().getSplitCellPercentageId();

                    MetaCategory metaKey = resolveMetaCategory(dimUsage);
                    MetaInfo metaInfo = cell.getCell().getMetaInfo().getMetaInfo(metaKey.category);
                    Object metadataId = metaInfo != null ? metaInfo.getValue() : null;

                    if (metadataId == null) {
                        // The funding cell has no value for this dimension (e.g. child_category_value_id
                        // is NULL because this indicator has no Level 1 disaggregation category).
                        // It should only match the "unallocated" hierarchy row (UNALLOCATED_ID).
                        // If the current hierarchy row is a specific category, this cell does not belong there.
                        if (splitId != ColumnReportData.UNALLOCATED_ID) {
                            allMatch = false;
                            break;
                        }
                        // splitId == UNALLOCATED_ID → this IS the "no value" bucket → matches
                    } else if (((Long) metadataId) != splitId) {
                        allMatch = false;
                        break;
                    }
                }
                if (allMatch) {
                    percentage.set(new BigDecimal(1));
                }
            }

            BigDecimal toAdd = ((NumberedCell) cell.getCell()).getAmount().multiply(percentage.get());
            res = res.add(toAdd);
        }
        return new NiAmountCell(res, precision, isScalableByUnits);
    }

    /**
     * Determines which {@link MetaCategory} to compare against for a given {@link NiDimensionUsage}
     * when computing indicator measure percentages.
     * <ul>
     *   <li>Splits by {@link AmpReportsSchema#INDICATOR_DISAGG_DIM_USG} (Level 0, parent category,
     *       backed by amp_category_value.id via parent_category_id) are matched against
     *       {@link MetaCategory#CATEGORY_VALUE_ID}.</li>
     *   <li>Splits by {@link AmpReportsSchema#INDICATOR_DISAGG_LEVEL_1_DIM_USG} (Level 1, child
     *       category, backed by amp_category_value.id via child_category_id) are matched against
     *       {@link MetaCategory#CHILD_CATEGORY_VALUE_ID}.</li>
     *   <li>Splits by {@link AmpReportsSchema#INDICATOR_DIM_USG} (indicator identity) are matched
     *       against {@link MetaCategory#INDICATOR_ID}.</li>
     *   <li>All other indicator splits (program / theme hierarchies) are matched against
     *       {@link MetaCategory#INDICATOR_PROGRAM_ID}.</li>
     * </ul>
     */
    private MetaCategory resolveMetaCategory(NiDimensionUsage dimUsage) {
        if (dimUsage == AmpReportsSchema.INDICATOR_DISAGG_DIM_USG) {
            return CATEGORY_VALUE_ID;
        } else if (dimUsage == AmpReportsSchema.INDICATOR_DISAGG_LEVEL_1_DIM_USG) {
            // Level 1 (child) disaggregation category — stored separately from Level 0
            // so that both can be used as hierarchies simultaneously without conflicts.
            return CHILD_CATEGORY_VALUE_ID;
        } else if (dimUsage == AmpReportsSchema.INDICATOR_DIM_USG) {
            return INDICATOR_ID;
        }
        return INDICATOR_PROGRAM_ID;
    }

    @Override
    public NiAmountCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        NiAmountCell z = doHorizontalReduce(cells, context);
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

        while (it.hasNext())
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
     *
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
