package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;

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

}
