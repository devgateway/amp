package org.dgfoundation.amp.nireports;

import java.util.Comparator;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.jetbrains.annotations.NotNull;

/**
 * @author Octavian Ciubotaru
 */
public class DoubleCell extends Cell {

    private static final Comparator<Double> comparator = Comparator.nullsFirst(Comparator.naturalOrder());

    public final Double value;
    public final MetaInfoSet metaInfo;

    public DoubleCell(Double value, long activityId, long entityId, MetaInfoSet metaInfo,
            Optional<NiDimension.LevelColumn> levelColumn) {
        super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
        this.value = value;
        this.metaInfo = metaInfo.freeze();
    }

    @Override
    public String getDisplayedValue() {
        return value == null ? "null" : value.toString();
    }

    @Override
    public Cell changeOwnerId(long newActivityId) {
        return new DoubleCell(value, newActivityId, entityId, metaInfo, mainLevel);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return comparator.compare(value, ((DoubleCell) o).value);
    }

    @Override
    public MetaInfoSet getMetaInfo() {
        return metaInfo;
    }
}
