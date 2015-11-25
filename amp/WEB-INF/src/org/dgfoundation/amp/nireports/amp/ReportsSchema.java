package org.dgfoundation.amp.nireports.amp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class ReportsSchema implements NiReportsSchema {
	protected Map<String, NiReportColumn> columns = new HashMap<>();
	protected Map<String, NiReportMeasure> measures = new HashMap<>();
	
	@Override
	public Map<String, NiReportColumn> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	@Override
	public Map<String, NiReportMeasure> getMeasures() {
		return Collections.unmodifiableMap(measures);
	}
		
	public ReportsSchema addColumn(NiReportColumn col) {
		if (columns.containsKey(col.name))
			throw new RuntimeException("double definition of column with name " + col.name);
		columns.put(col.name, col);
		return this;
	}
	
	public ReportsSchema addMeasure(NiReportMeasure meas) {
		if (measures.containsKey(meas.name))
			throw new RuntimeException("double definition of measure with name " + meas.name);
		measures.put(meas.name, meas);
		return this;
	}

}
