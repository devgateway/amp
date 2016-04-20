package org.dgfoundation.amp.nireports;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractReportsSchema implements NiReportsSchema {
	protected Map<String, NiReportColumn<?>> columns = new HashMap<>();
	protected Map<String, NiReportMeasure<?>> measures = new HashMap<>();
	
	@Override
	public Map<String, NiReportColumn<?>> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	@Override
	public Map<String, NiReportMeasure<?>> getMeasures() {
		return Collections.unmodifiableMap(measures);
	}
		
	public AbstractReportsSchema addColumn(NiReportColumn<?> col) {
		failIf(columns.containsKey(col.name), "double definition of column with name " + col.name);
		failIf(col.getBehaviour() == null, "no behaviour specified for column with name " + col.name);
		columns.put(col.name, col);
		return this;
	}
	
	public AbstractReportsSchema addMeasure(NiReportMeasure<?> meas) {
		failIf(measures.containsKey(meas.name), "double definition of measure with name " + meas.name);
		failIf(meas.getBehaviour() == null, "no behaviour specified for measure with name " + meas.name);
		measures.put(meas.name, meas);
		return this;
	}

}
