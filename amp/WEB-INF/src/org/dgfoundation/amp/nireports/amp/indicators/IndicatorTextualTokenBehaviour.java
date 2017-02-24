package org.dgfoundation.amp.nireports.amp.indicators;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * This behaviour matches {@link TextualTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorTextualTokenBehaviour extends TextualTokenBehaviour {

    public static final IndicatorTextualTokenBehaviour instance = new IndicatorTextualTokenBehaviour();

    private IndicatorTextualTokenBehaviour() {
    }

    @Override
    public NiTextCell doHorizontalReduce(List<NiCell> cells) {
        StringJoiner text = new StringJoiner(", ");
        Map<Long, String> entityIdsValues = new HashMap<>();
        cells.stream().sorted(IndicatorCellComparator.instance).forEach(niCell -> {
            TextCell cell = (TextCell) niCell.getCell();
            text.add(cell.text);
            entityIdsValues.put(cell.entityId, cell.text);
        });
        return new NiTextCell(text.toString(), any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }
}
