package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.DoubleCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class DoubleColumn extends AmpDifferentialColumn<DoubleCell> {

    public DoubleColumn(String columnName,
            NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<?> behaviour) {
        super(columnName, levelColumn, viewName, (engine, col) -> "", behaviour);
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }

    @Override
    protected DoubleCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
        Double value = (rs.getObject(2) == null) ? null : rs.getDouble(2);

        long entityId = rs.getLong(levelColumn.isPresent() ? 3 : 1);
        Map<NiDimension.NiDimensionUsage, NiDimension.Coordinate> coos = buildCoordinates(entityId, engine, rs);
        return new DoubleCell(value, rs.getLong(1), entityId, coos, levelColumn);
    }

    @Override
    public boolean getKeptInSummaryReports() {
        return false;
    }
}
