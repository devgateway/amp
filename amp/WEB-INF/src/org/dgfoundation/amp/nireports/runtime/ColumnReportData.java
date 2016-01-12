package org.dgfoundation.amp.nireports.runtime;

import java.util.Set;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.ReportData;

/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	
	public final GroupColumn contents;	
	
	public ColumnReportData(NiReportsEngine context, GroupColumn rawData) {
		super(context);
		this.contents = rawData;
//		for(CellColumn leafColumn: this.context.headers.getLeafColumns())
//			trailCells.put(leafColumn, new NiCell(leafColumn.getBehaviour().generateEmptyCell(), null));
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Set<Long> getIds() {
		return contents.getIds();
	}
	
}
