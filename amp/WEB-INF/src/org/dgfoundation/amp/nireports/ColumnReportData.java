package org.dgfoundation.amp.nireports;

import java.util.HashMap;
import java.util.Map;

public class ColumnReportData extends ReportData {
	
	public final Map<CellColumn, ColumnContents> contents; // null for leafs	
	
	public ColumnReportData(NiReportContext context) {
		super(context);
		this.contents = new HashMap<CellColumn, ColumnContents>();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	
}
