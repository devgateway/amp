package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DoubleCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.dgfoundation.amp.algo.AmpCollections.any;

/**
 * This behaviour matches {@link TextualTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorTextualTokenBehaviour extends TextualTokenBehaviour {

    private final BiFunction<NiReportsEngine, Cell, String> formatter;
    private final NiDimensionUsage indicatorDimensionUsage;
    private final boolean removeDuplicates;
    private final IndicatorCellComparator indicatorCellComparator;

    public static IndicatorTextualTokenBehaviour forText(NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        return new IndicatorTextualTokenBehaviour((engine, cell) -> ((TextCell) cell).text, indicatorDimensionUsage, removeDuplicates);
    }

    public static IndicatorTextualTokenBehaviour forDouble(NiDimensionUsage indicatorDimensionUsage) {
        return new IndicatorTextualTokenBehaviour((engine, cell) -> formatDouble(engine, ((DoubleCell) cell).value), indicatorDimensionUsage, true);
    }

    private IndicatorTextualTokenBehaviour(BiFunction<NiReportsEngine, Cell, String> formatter,
            NiDimensionUsage indicatorDimensionUsage, boolean removeDuplicates) {
        this.formatter = formatter;
        this.indicatorDimensionUsage = indicatorDimensionUsage;
        this.removeDuplicates = removeDuplicates;
        indicatorCellComparator = new IndicatorCellComparator(indicatorDimensionUsage);
    }

    @Override
    public NiTextCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        StringJoiner reducedText = new StringJoiner(", ");
        Map<Long, String> entityIdsValues = new HashMap<>();
        cells.stream().filter(cellFilter()).sorted(indicatorCellComparator).forEach(niCell -> {
            Cell cell = niCell.getCell();
            String text = formatter.apply(context, cell);
            reducedText.add(text);
            entityIdsValues.put(cell.entityId, text);
        });
        return new NiTextCell(reducedText.toString(), any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }

    private Predicate<NiCell> cellFilter() {
        if (removeDuplicates) {
            return distinctByIndicator();
        } else {
            return c -> true;
        }
    }

    private Predicate<NiCell> distinctByIndicator() {
        return AmpCollections.distinctByKey(c -> c.getCell().coordinates.get(indicatorDimensionUsage));
    }

    private static String formatDouble(NiReportsEngine engine, Double value) {
        if (value == null) {
            return "";
        }
        return ReportsUtil.getDecimalFormatOrDefault(engine.spec).format(value);
    }
}
