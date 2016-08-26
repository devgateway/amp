package org.dgfoundation.amp.nireports.output.nicells;

import java.math.BigDecimal;
import java.util.IdentityHashMap;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;

/**
 * @author Octavian Ciubotaru
 */
public class NiTransactionCountCell extends NiAmountCell {

    public static final NiTransactionCountCell ZERO = new NiTransactionCountCell(new IdentityHashMap<>());

    private IdentityHashMap<Cell, Boolean> transactionCells;

    public NiTransactionCountCell(IdentityHashMap<Cell, Boolean> transactionCells) {
        super(BigDecimal.valueOf(transactionCells.size()), NiPrecisionSetting.IDENTITY_PRECISION_SETTING);
        this.transactionCells = transactionCells;
    }

    public IdentityHashMap<Cell, Boolean> getTransactionCells() {
        return transactionCells;
    }
}
