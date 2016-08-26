package org.dgfoundation.amp.nireports.output.nicells;

import java.util.IdentityHashMap;

import org.dgfoundation.amp.nireports.Cell;

/**
 * @author Octavian Ciubotaru
 */
public class NiTransactionCountCell extends NiIntCell {

    public static final NiTransactionCountCell ZERO = new NiTransactionCountCell(new IdentityHashMap<>());

    private IdentityHashMap<Cell, Boolean> transactionCells;

    public NiTransactionCountCell(IdentityHashMap<Cell, Boolean> transactionCells) {
        super(transactionCells.size(), -1);
        this.transactionCells = transactionCells;
    }

    public IdentityHashMap<Cell, Boolean> getTransactionCells() {
        return transactionCells;
    }
}
