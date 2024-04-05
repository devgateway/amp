package org.dgfoundation.amp.nireports.amp.indicators;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.behaviours.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiDateCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.dgfoundation.amp.algo.AmpCollections.any;

/**
 * This behaviour matches {@link DateTokenBehaviour} with the exception of how horizontal reduce is done.
 * For indicators we want to keep all values and display them in a specific order.
 * See more at {@link IndicatorCellComparator}.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorDateTokenBehaviour extends DateTokenBehaviour {

    private final NiDimensionUsage indicatorDimensionUsage;
    private final IndicatorCellComparator indicatorCellComparator;

    public IndicatorDateTokenBehaviour(NiDimensionUsage indicatorDimensionUsage) {
        this.indicatorDimensionUsage = indicatorDimensionUsage;
        indicatorCellComparator = new IndicatorCellComparator(indicatorDimensionUsage);
    }

    @Override
    public NiDateCell doHorizontalReduce(List<NiCell> cells) {
        Map<Long, LocalDate> entityIdsValues = new HashMap<>();
        List<LocalDate> values = new ArrayList<>();
        cells.stream().filter(distinctByIndicator()).sorted(indicatorCellComparator).forEach(niCell -> {
            DateCell cell = (DateCell) niCell.getCell();
            entityIdsValues.put(cell.entityId, cell.date);
            values.add(cell.date);
        });
        return new NiDateCell(values, any(entityIdsValues.keySet(), -1L), entityIdsValues);
    }

    private Predicate<NiCell> distinctByIndicator() {
        return AmpCollections.distinctByKey(c -> c.getCell().coordinates.get(indicatorDimensionUsage));
    }
}
