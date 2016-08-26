package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTransactionCountCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * @author Octavian Ciubotaru
 */
public class BigTransactionCountingBehaviour extends AbstractComputedBehaviour<NiTransactionCountCell> {

    public static final BigTransactionCountingBehaviour instance = new BigTransactionCountingBehaviour(TimeRange.MONTH);

    private BigTransactionCountingBehaviour(TimeRange timeRange) {
        super(timeRange);
    }

    @Override
    public NiOutCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        if (cells.isEmpty()) {
            return NiTransactionCountCell.ZERO;
        }
        BigDecimal threshold = context.schemaSpecificScratchpad.getBigTransactionThreshold();
        IdentityHashMap<Cell, Boolean> transactionCells = new IdentityHashMap<>();
        for (NiCell niCell : cells) {
            if (niCell.getAmount().compareTo(threshold) >= 0) {
                transactionCells.put(niCell.getCell(), true);
            }
        }
        return new NiTransactionCountCell(transactionCells);
    }

    @Override
    public NiTransactionCountCell doHorizontalReduce(List<NiCell> cells) {
        throw new UnsupportedOperationException();
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
