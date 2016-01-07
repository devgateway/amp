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
	
	public final static String ROOT_COLUMN_NAME = "ROOT";
	
	public final GroupColumn contents;	
	
	public ColumnReportData(NiReportsEngine context) {
		super(context);
		this.contents = new GroupColumn(ROOT_COLUMN_NAME, null, null);
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
