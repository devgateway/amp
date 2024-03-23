package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Cell} which holds a text and a fractional number called "percentage". 
 * The MetaInfo is always empty, while the coordinates are either empty or with a single entry, depending on the #mainLevel)
 * @author Dolghier Constantin
 *
 */
public final class PercentageTextCell extends Cell {
    
    /**
     * DUMMY SOZO
     * the non-null fractional number. 0.0 <= percentage <= 1.0
     */
    public final BigDecimal percentage;
    
    /**
     * the non-null text
     */
    public final String text;
    
    /**
     * constructs an instance with the given parameters. Will throw a {@link RuntimeException} (or one of its subclasses) in case it is given an invalid input
     * @param text
     * @param activityId
     * @param entityId
     * @param levelColumn
     * @param percentage
     */
    public PercentageTextCell(String text, long activityId, long entityId, Optional<LevelColumn> levelColumn, BigDecimal percentage) {
        super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
        Objects.requireNonNull(text);
        Objects.requireNonNull(percentage);
        this.percentage = percentage;
        NiUtils.failIf(this.percentage.compareTo(BigDecimal.ZERO) < 0, () -> String.format("percentage should be between 0.0 and 1.0, but is instead %.2f", percentage.doubleValue()));
        NiUtils.failIf(this.percentage.compareTo(BigDecimal.ONE) > 0, () -> String.format("percentage should be between 0.0 and 1.0, but is instead %.2f", percentage.doubleValue()));
        this.text = text;
    }

    @Override
    public PercentageTextCell changeOwnerId(long newActivityId) {
        return new PercentageTextCell(this.text, newActivityId, this.entityId, this.mainLevel, this.percentage);
    }
    
    @Override
    public MetaInfoSet getMetaInfo() {
        return MetaInfoSet.empty();
    }

    @Override
    public BigDecimal getPercentage() {
        return percentage;
    }
    
    @Override
    public int compareTo(Object o) {
        PercentageTextCell ptc = (PercentageTextCell) o;
        int delta = text.compareTo(ptc.text);
        if (delta == 0)
            delta = Long.compare(entityId, ptc.entityId);
        if (delta == 0)
            delta = Long.compare(activityId, ptc.activityId);
        return delta;
    }
    
    @Override
    public String toString() {
        String entityStr = String.format(", eid: %d", this.entityId);
        return String.format("%s (id: %d%s%s, p: %.2f)", text, this.activityId, entityStr, coordinates.isEmpty() ? "" : String.format(", coos: %s", coordinates), this.percentage.doubleValue());
        //return String.format("%s (id, mainId, %%) = (%d, %d, %.2f)", text, this.activityId, this.entityId, this.percentage.doubleValue());
//      String entityStr = this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "";
//      String percentageStr = percentage == null ? "(n/a)" : String.format("%.2f", percentage.doubleValue());
//      return String.format("%s (id: %d%s, coos: %s%s %%)", text, this.activityId, entityStr, coordinates, percentageStr);
    }

    @Override
    public String getDisplayedValue() {
        return text;
    }
}
