package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

import java.util.List;

public class HardcodedColumn<K extends Cell> extends NiReportColumn<K> {
    protected List<K> cells;
    protected boolean keptInSummaryReports = false;
    
    public HardcodedColumn(String name, HardcodedCells<K> cells, LevelColumn levelColumn, Behaviour<?> behaviour) {
        super(name, levelColumn, behaviour, null);
        this.cells = cells.getCells();
    }
    
    public HardcodedColumn<K> keptInSummaryReports() {
        this.keptInSummaryReports = true;
        return this;
    }
    
    @Override
    public List<K> fetch(NiReportsEngine engine) throws Exception {
        return cells;
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
    
    @Override
    public boolean getKeptInSummaryReports() {
        return this.keptInSummaryReports;
    }
}
