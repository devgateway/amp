package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class IndicatorDisaggregationColumn extends SimpleTextColumn {
    public IndicatorDisaggregationColumn(String name, String view, LevelColumn level, Behaviour<NiTextCell> behaviour) {
        super(name, level, view, behaviour);
    }

    @Override
    protected Map<NiDimensionUsage, Coordinate> buildCoordinates(long entityId, NiReportsEngine engine, ResultSet rs)
            throws SQLException {
        Map<NiDimensionUsage, Coordinate> coordinates = super.buildCoordinates(entityId, engine, rs);
        addCoordinateIfLongExists(coordinates, rs, "parent_category_value_id",
                AmpReportsSchema.INDICATOR_DISAGG_LEVEL_COLUMN);
        addCoordinateIfLongExists(coordinates, rs, "child_category_value_id",
                AmpReportsSchema.INDICATOR_DISAGG_LEVEL_1_COLUMN);
        addCoordinateIfLongExists(coordinates, rs, "third_category_value_id",
                AmpReportsSchema.INDICATOR_DISAGG_LEVEL_2_COLUMN);
        return coordinates;
    }
}
