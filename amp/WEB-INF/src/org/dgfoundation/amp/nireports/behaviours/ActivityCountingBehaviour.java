package org.dgfoundation.amp.nireports.behaviours;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTransactionCountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * @author Octavian Ciubotaru
 */
public class ActivityCountingBehaviour extends AbstractComputedBehaviour<NiTransactionCountCell> {

    public static final ActivityCountingBehaviour instance = new ActivityCountingBehaviour(TimeRange.MONTH);

    private ActivityCountingBehaviour(TimeRange timeRange) {
        super(timeRange);
    }

    @Override
    public NiTransactionCountCell doHorizontalReduce(List<NiCell> cells) {
        if (cells.isEmpty()) {
            return NiTransactionCountCell.ZERO;
        }
        IdentityHashMap<Cell, Boolean> transactionCells = new IdentityHashMap<>();
        for (NiCell niCell : cells) {
            transactionCells.put(niCell.getCell(), true);
        }
        return new NiTransactionCountCell(transactionCells);
    }

    @Override
    public NiTransactionCountCell getZeroCell() {
        return NiTransactionCountCell.ZERO;
    }

    @Override
    public NiOutCell doVerticalReduce(Collection<NiTransactionCountCell> cells) {
        if (cells.isEmpty()) {
            return NiTransactionCountCell.ZERO;
        }
        IdentityHashMap<Cell, Boolean> transactionCells = new IdentityHashMap<>();
        for (NiTransactionCountCell cell : cells) {
            transactionCells.putAll(cell.getTransactionCells());
        }
        return new NiTransactionCountCell(transactionCells);
    }
}
