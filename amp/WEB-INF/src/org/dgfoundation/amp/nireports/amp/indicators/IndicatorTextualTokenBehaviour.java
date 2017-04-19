package org.dgfoundation.amp.nireports.amp.indicators;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiFunction;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DoubleCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;

/**
 * This behaviour matches {@link TextualTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorTextualTokenBehaviour extends TextualTokenBehaviour {

    public static final IndicatorTextualTokenBehaviour textInstance =
            new IndicatorTextualTokenBehaviour((engine, cell) -> ((TextCell) cell).text);

    public static final IndicatorTextualTokenBehaviour doubleInstance =
            new IndicatorTextualTokenBehaviour((engine, cell) -> formatDouble(engine, ((DoubleCell) cell).value));

    private BiFunction<NiReportsEngine, Cell, String> formatter;

    private IndicatorTextualTokenBehaviour(BiFunction<NiReportsEngine, Cell, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public NiTextCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        StringJoiner reducedText = new StringJoiner(", ");
        Map<Long, String> entityIdsValues = new HashMap<>();
        cells.stream().sorted(IndicatorCellComparator.instance).forEach(niCell -> {
            Cell cell = niCell.getCell();
            String text = formatter.apply(context, cell);
            reducedText.add(text);
            entityIdsValues.put(cell.entityId, text);
        });
        return new NiTextCell(reducedText.toString(), any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }

    private static String formatDouble(NiReportsEngine engine, Double value) {
        if (value == null) {
            return "";
        }
        return ReportsUtil.getDecimalFormatOrDefault(engine.spec).format(value);
    }
}
