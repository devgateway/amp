package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a cell with an amount, with no coordinates or metadata
 * @author Dolghier Constantin
 *
 */
public final class AmountCell extends Cell implements CategCell, NumberedCell {
        
    public final MonetaryAmount amount;
    
    public AmountCell(long activityId, MonetaryAmount amount) {
        super(activityId);
        this.amount = amount;
    }
    
    @Override
    public AmountCell changeOwnerId(long newActivityId) {
        return new AmountCell(newActivityId, amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount.amount;
    }

    @Override
    public String toString() {
        return String.format("(actId: %d, amt: %s)", this.activityId, amount);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return MetaInfoSet.empty();
    }

    @Override
    public int compareTo(Object o) {
        AmountCell cac = (AmountCell) o;
        return amount.compareTo(cac.amount);
    }

    @Override
    public String getDisplayedValue() {
        return amount.getDisplayable();
    }

    @Override
    public Map<NiDimensionUsage, Coordinate> getCoordinates() {
        return Collections.emptyMap();
    }

    @Override
    public NiPrecisionSetting getPrecision() {
        return amount.precisionSetting;
    }

    @Override
    public boolean isScalableByUnits() {
        return true;
    }
}
