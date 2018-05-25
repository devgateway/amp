package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * the behaviour of a once-per-entity column (like Proposed Project Cost)
 * @author Dolghier Constantin
 *
 */
public class NumericalColumnBehaviour extends TrivialMeasureBehaviour {
    public static NumericalColumnBehaviour getInstance() {return instance;}
    
    private final static NumericalColumnBehaviour instance = new NumericalColumnBehaviour();
    
    protected NumericalColumnBehaviour() {}
    
    @Override
    public TimeRange getTimeRange() {
        return TimeRange.NONE;
    }
    
    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        return null;
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return NiTextCell.EMPTY;
    }
}
