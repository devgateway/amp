package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;

import java.util.function.BiFunction;

/**
 * Token behaviour for "Indicator Disaggregation Level 1" (child disaggregation category).
 *
 * <p>In the AMP indicator disaggregation model, each {@code amp_indicator_disaggregation_values}
 * row can have both a parent ({@code parent_category_id}) and a child ({@code child_category_id})
 * {@code amp_category_value}. This class handles the <em>child</em> (Level 1) side:</p>
 * <ul>
 *   <li>Level 0 ({@link IndicatorDisaggregationTextualTokenBehaviour}) → {@code parent_category_id}</li>
 *   <li>Level 1 (this class) → {@code child_category_id} — may be {@code NULL} when no child
 *       disaggregation category is assigned.</li>
 * </ul>
 *
 * <p>The funding amounts in {@code v_ni_indicator_funding} expose {@code child_category_value_id}
 * (mapped from {@code aidv.child_category_id}), which is tagged with
 * {@code INDICATOR_DISAGG_LEVEL_1_DIM_USG} via {@code SubDimensions}. This allows the NiReports
 * engine to filter and split funding rows by Level 1 disaggregation category independently of
 * Level 0, so both columns can be used simultaneously without cross-contamination.</p>
 *
 * <p>The filtering and horizontal-reduce logic is inherited unchanged from
 * {@link IndicatorDisaggregationTextualTokenBehaviour}; only the dimension instance name differs.</p>
 *
 * @see IndicatorDisaggregationTextualTokenBehaviour
 * @see org.dgfoundation.amp.nireports.amp.AmpReportsSchema#INDICATOR_DISAGG_LEVEL_1_DIM_USG
 */
public class IndicatorDisaggregationLevel1TextualTokenBehaviour extends IndicatorDisaggregationTextualTokenBehaviour {

    /**
     * @param formatter               converts a raw {@link Cell} to its display string
     * @param indicatorDimensionUsage the indicator-level dimension usage used to order and
     *                                deduplicate cells by indicator ID
     * @param removeDuplicates        whether to collapse duplicate values for the same indicator
     */
    public IndicatorDisaggregationLevel1TextualTokenBehaviour(
            BiFunction<NiReportsEngine, Cell, String> formatter,
            NiDimension.NiDimensionUsage indicatorDimensionUsage,
            boolean removeDuplicates) {
        super(formatter, indicatorDimensionUsage, removeDuplicates);
    }

    /**
     * Factory method that creates a text-formatting instance for Level 1 disaggregation columns.
     *
     * @param indicatorDimensionUsage the indicator-level dimension usage (typically
     *                                {@code AmpReportsSchema.INDICATOR_DIM_USG}) used to order
     *                                cells by their indicator coordinate
     * @param removeDuplicates        whether duplicate category values for the same indicator
     *                                should be collapsed into a single entry
     * @return a new {@code IndicatorDisaggregationLevel1TextualTokenBehaviour} instance
     */
    public static IndicatorDisaggregationLevel1TextualTokenBehaviour forText(
            NiDimension.NiDimensionUsage indicatorDimensionUsage,
            boolean removeDuplicates) {
        return new IndicatorDisaggregationLevel1TextualTokenBehaviour(
                (engine, cell) -> ((TextCell) cell).text,
                indicatorDimensionUsage,
                removeDuplicates);
    }
}
