package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class NiColumnReportData extends NiReportData {
	public final Map<CellColumn, Map<Long, Cell>> contents;
	
	public NiColumnReportData(Map<CellColumn, Map<Long, Cell>> contents, Map<CellColumn, NiCell> trailCells, NiCell splitterCell) {
		super(trailCells, contents.values().stream().flatMap(z -> z.keySet().stream()).collect(Collectors.toSet()), splitterCell);
		this.contents = Collections.unmodifiableMap(contents);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public int computeRowSpan(boolean summaryReport) {
		if (summaryReport)
			return 1;
		return getIds().size()/* + 1*/;
	}
}
