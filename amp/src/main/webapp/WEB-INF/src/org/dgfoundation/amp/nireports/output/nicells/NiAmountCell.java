package org.dgfoundation.amp.nireports.output.nicells;

import java.math.BigDecimal;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a {@link NiOutCell} holding an arbitrary-precision amount
 * @author Dolghier Constantin
 *
 */
public class NiAmountCell extends NiOutCell implements NumberedCell {

    public final BigDecimal amount;
    public final NiPrecisionSetting precisionSetting;
    public final boolean isScalableByUnits;
    
    public NiAmountCell(BigDecimal amount, NiPrecisionSetting precisionSetting) {
        this(amount, precisionSetting, true);
    }

    public NiAmountCell(BigDecimal amount, NiPrecisionSetting precisionSetting, boolean isScalableByUnits) {
        this.amount = amount;
        this.precisionSetting = precisionSetting;
        this.isScalableByUnits = isScalableByUnits;
    }
    
    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public NiPrecisionSetting getPrecision() {
        return precisionSetting;
    }


    @Override
    public String getDisplayedValue() {
        return amount.stripTrailingZeros().toPlainString();
    }

    @Override
    public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
        return visitor.visit(this, niCellColumn);
    }
    
    public final static NiAmountCell ZERO = new NiAmountCell(BigDecimal.ZERO, NiPrecisionSetting.IDENTITY_PRECISION_SETTING);

    @Override
    public boolean isScalableByUnits() {
        return isScalableByUnits;
    }

}
