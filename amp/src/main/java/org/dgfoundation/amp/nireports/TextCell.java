package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.util.Map;
import java.util.Optional;

/**
 * a {@link Cell} which holds a text. Coordinates are either empty or with a single entry, depending on the #mainLevel)
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

    public final String text;

    public TextCell(String text, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
        this(text, activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
    }

    public TextCell(String text, long activityId, long entityId, Map<NiDimensionUsage, Coordinate> coordinates,
            Optional<LevelColumn> levelColumn) {
        super(activityId, entityId, coordinates, levelColumn);
        this.text = (text == null) ? "" : text;
    }
    
    @Override
    public TextCell changeOwnerId(long newActivityId) {
        return new TextCell(text, newActivityId, entityId, coordinates, mainLevel);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return MetaInfoSet.empty();
    }

    @Override
    public int compareTo(Object o) {
        TextCell tc = (TextCell) o;
        return text.compareTo(tc.text);
    }

    @Override
    public String toString() {
        //String entityStr = this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "";
        String entityStr = String.format(", eid: %d", this.entityId);
        return String.format("%s (id: %d%s%s)", text, this.activityId, entityStr, coordinates.isEmpty() ? "" : String.format(", coos: %s", coordinates));
    }
    
    @Override
    public String getDisplayedValue() {
        return text;
    }
}
