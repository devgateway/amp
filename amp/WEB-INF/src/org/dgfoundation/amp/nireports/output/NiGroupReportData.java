package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class NiGroupReportData extends NiReportData {
	public final List<NiReportData> subreports;

	public NiGroupReportData(List<NiReportData> subreports, Map<CellColumn, NiCell> trailCells, NiCell splitter) {
		super(trailCells, subreports.stream().flatMap(z -> z.getIds().stream()).collect(Collectors.toSet()), splitter);
		this.subreports = Collections.unmodifiableList(subreports);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public int computeRowSpan(boolean summaryReport) {
		int sum = /*1*/0;
		for(NiReportData rd:subreports)
			sum += rd.getRowSpan(summaryReport);
		return sum;
	}
}
