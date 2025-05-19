package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

import java.util.Optional;

/**
 * a {@link Cell} holding a long value. Coordinates are empty unless it has a mainLevel (in which case it will have exactly one coordinate, as per the general contract of {@link Cell})
 * @author Dolghier Constantin
 */
public final class IntCell extends Cell {

    /** the payload */
    public final long value;
    
    public IntCell(long value, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
        super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
        this.value = value;
    }

    @Override
    public IntCell changeOwnerId(long newActivityId) {
        return new IntCell(this.value, newActivityId, this.entityId, this.mainLevel);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return MetaInfoSet.empty();
    }

    @Override
    public int compareTo(Object o) {
        IntCell dc = (IntCell) o;
        return Long.compare(this.value, dc.value);
    }

    @Override
    public String toString() {
        String entityStr = this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "";
        return String.format("%d (id: %d%s, coos: %s)", value, this.activityId, entityStr, coordinates);
    }
    
    @Override
    public String getDisplayedValue() {
        return Long.toString(value);
    }

}
