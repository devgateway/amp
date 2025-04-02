package org.dgfoundation.amp.nireports;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * @author Octavian Ciubotaru
 */
public class DoubleCell extends Cell {

    private static final Comparator<Double> comparator = Comparator.nullsFirst(Comparator.naturalOrder());

    public final Double value;

    public DoubleCell(Double value, long activityId, long entityId, Map<NiDimensionUsage, Coordinate> coordinates,
            Optional<NiDimension.LevelColumn> levelColumn) {
        super(activityId, entityId, coordinates, levelColumn);
        this.value = value;
    }

    @Override
    public String getDisplayedValue() {
        return value == null ? "null" : value.toString();
    }

    @Override
    public Cell changeOwnerId(long newActivityId) {
        return new DoubleCell(value, newActivityId, entityId, coordinates, mainLevel);
    }

    @Override
    public int compareTo(Object o) {
        return comparator.compare(value, ((DoubleCell) o).value);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return MetaInfoSet.empty();
    }
}
