package org.dgfoundation.amp.nireports.amp.indicators;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.behaviours.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiDateCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * This behaviour matches {@link DateTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorDateTokenBehaviour extends DateTokenBehaviour {

    public static final IndicatorDateTokenBehaviour instance = new IndicatorDateTokenBehaviour();

    private IndicatorDateTokenBehaviour() {
    }

    @Override
    public NiDateCell doHorizontalReduce(List<NiCell> cells) {
        Map<Long, LocalDate> entityIdsValues = new HashMap<>();
        List<LocalDate> values = new ArrayList<>();
        cells.stream().sorted(IndicatorCellComparator.instance).forEach(niCell -> {
            DateCell cell = (DateCell) niCell.getCell();
            entityIdsValues.put(cell.entityId, cell.date);
            values.add(cell.date);
        });
        return new NiDateCell(values, any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }
}
