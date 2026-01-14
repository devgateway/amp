package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.diff.TextColumnKeyBuilder;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * a simple text column which fetches its input from a view which contains 3 or more columns: <br />
 *  1. amp_activity_id (or pledge_id)
 *  2. payload (text)
 *  3. entity_id (e.g. sector_id, donor_id etc)
 *  
 *  All the extra columns are ignored
 * @author Dolghier Constantin
 *
 */
public class SimpleTextColumn extends AmpDifferentialColumn<TextCell> {

    private static final int ENTITY_ID_INDEX_3 = 3;

    protected Function<String, String> postprocessor = Function.identity();

    private boolean allowNulls;

    public SimpleTextColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName) {
        this(columnName, levelColumn, viewName, TextualTokenBehaviour.instance);
    }

    public SimpleTextColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<NiTextCell> behaviour) {
        this(columnName, levelColumn, viewName, behaviour, false);
    }

    public SimpleTextColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<NiTextCell> behaviour, boolean sscEnabledColumn) {
        super(columnName, levelColumn, viewName, TextColumnKeyBuilder.instance, behaviour, sscEnabledColumn);
    }

    @Override
    protected TextCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
        String text = postprocessor.apply(rs.getString(2));
        Long entityId = rs.getLong(withoutEntity ? 1 : ENTITY_ID_INDEX_3);
        
        if (!allowNulls && text == null && entityId != ColumnReportData.UNALLOCATED_ID) {
            return null;
        }

        Map<NiDimensionUsage, Coordinate> coos = buildCoordinates(entityId, engine, rs);
        return new TextCell(text, rs.getLong(1), entityId, coos, this.levelColumn);
    }

    public static SimpleTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn, Behaviour<NiTextCell> behaviour) {
        return new SimpleTextColumn(columnName, levelColumn, viewName, behaviour);
    }
    
    public static SimpleTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn) {
        return new SimpleTextColumn(columnName, levelColumn, viewName, TextualTokenBehaviour.instance);
    }

    public static SimpleTextColumn fromView(String columnName, String viewName, NiDimension.LevelColumn levelColumn,
            boolean sscEnabledColumn) {
        return new SimpleTextColumn(columnName, levelColumn, viewName, TextualTokenBehaviour.instance,
                sscEnabledColumn);
    }

    public static SimpleTextColumn fromViewWithoutEntity(String columnName, String viewName, Behaviour<NiTextCell> behaviour) {
        return new SimpleTextColumn(columnName, null, viewName, behaviour).withoutEntity();
    }

    public static SimpleTextColumn fromViewWithoutEntity(String columnName, String viewName) {
        return fromViewWithoutEntity(columnName, viewName, false);
    }

    public static SimpleTextColumn fromViewWithoutEntity(String columnName, String viewName, boolean sscEnabledColumn) {
        return new SimpleTextColumn(columnName, null, viewName, TextualTokenBehaviour.instance,
                sscEnabledColumn).withoutEntity();
    }

    private boolean withoutEntity = false;
    
    private SimpleTextColumn withoutEntity() {
        this.withoutEntity = true;
        return this;
    }
        
    public SimpleTextColumn withPostprocessor(Function<String, String> postprocessor) {
            this.postprocessor = postprocessor;
            return this;
    }

    public SimpleTextColumn allowNulls(boolean allowNulls) {
        this.allowNulls = allowNulls;
        return this;
    }

    @Override
    public boolean getKeptInSummaryReports() {
        return false;
    }

    @Override
    public List<ReportRenderWarning> performCheck(){
        return null;
    }
}
