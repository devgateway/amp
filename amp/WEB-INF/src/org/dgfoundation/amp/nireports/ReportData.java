package org.dgfoundation.amp.nireports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.runtime.CellColumn;


/**
 * this class is a hybrid between the old-reports-engine ColumnReportData, GroupReportData and will in the end implement the ReportsAPI {@link ReportArea}
 * @author Dolghier Constantin
 *
 */
public abstract class ReportData {
	public final Map<CellColumn<?>, Cell> trailCells;
	public final NiReportsEngine context;	
		
	public ReportData(NiReportsEngine context) {
		this.context = context;
		this.trailCells = new HashMap<>();
	}
	
	public abstract Set<Long> getIds();
	public abstract boolean isLeaf();
}
