package org.dgfoundation.amp.nireports.amp;

import org.apache.wicket.behavior.Behavior;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * the {@link Behavior} of a non-Funding-Flow MTEF column
 * @author Dolghier Constantin
 *
 */
public class MtefBehaviour extends TrivialMeasureBehaviour {
    
    final String totalColumnName;
    public MtefBehaviour(String totalColumnName) {
        this.totalColumnName = totalColumnName;
    }
    
    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        // trivial measures are copied verbatim to totals
        return new ImmutablePair<String, ColumnContents>(totalColumnName, fetchedContents);
    }
}
