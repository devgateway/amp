package org.dgfoundation.amp.nireports;

import java.util.HashMap;
import java.util.Map;

/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	
	public final Map<CellColumn, ColumnContents> contents; // null for leafs	
	
	public ColumnReportData(NiReportsEngine context) {
		super(context);
		this.contents = new HashMap<CellColumn, ColumnContents>();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	
}
