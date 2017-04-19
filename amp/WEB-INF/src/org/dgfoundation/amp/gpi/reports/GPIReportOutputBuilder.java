package org.dgfoundation.amp.gpi.reports;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * A utility class to transform a GeneratedReport to GPI Report Output (headers, report data)
 * 
 * @author Viorel Chihai
 *
 */
public abstract class GPIReportOutputBuilder  {
	
	protected Map<String, GPIReportOutputColumn> columns = new HashMap<>();
	
	public GPIReportOutputBuilder() {};
	
	protected Map<String, GPIReportOutputColumn> getColumns() {
		return Collections.unmodifiableMap(columns);
	}
	
	protected void addColumn(GPIReportOutputColumn col) {
		columns.put(col.columnName, col);
	}

	public abstract GPIReportOutput build(GeneratedReport generatedReport);
}
