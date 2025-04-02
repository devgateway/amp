package org.dgfoundation.amp.nireports.amp;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * This class holds all columns that can be used as hierarchies are at sub activity level. In other words one activity
 * can have many values for this column.
 *
 * @author Octavian Ciubotaru
 */
public class SubDimensions {

    private Map<String, String> columnIdNames;
    private Map<String, NiDimension.LevelColumn> levelColumns;

    public SubDimensions(Map<String, String> columnIdNames) {
        this.columnIdNames = columnIdNames;
    }

    public void initialize(Map<String, NiReportColumn<? extends Cell>> cols) {
        Map<String, NiDimension.LevelColumn> res = new HashMap<>();
        columnIdNames.forEach((colName, viewColName) -> {
            failIf(!cols.get(colName).levelColumn.isPresent(),
                    String.format("%s does not have a level column.", colName));
            res.put(viewColName, cols.get(colName).levelColumn.get());
        });
        levelColumns = Collections.unmodifiableMap(res);
    }

    public Map<String, String> getColumnIdNames() {
        return columnIdNames;
    }

    public Map<String, NiDimension.LevelColumn> getLevelColumns() {
        return levelColumns;
    }
}
