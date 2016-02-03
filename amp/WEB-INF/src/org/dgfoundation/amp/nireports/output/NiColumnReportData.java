package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * a flattened leaf region of a report output (see {@link ColumnReportData})
 * @author Dolghier Constantin
 *
 */
public class NiColumnReportData extends NiReportData {
	public final Map<CellColumn, Map<Long, NiOutCell>> contents;
	
	public NiColumnReportData(Map<CellColumn, Map<Long, NiOutCell>> contents, Map<CellColumn, NiOutCell> trailCells, NiSplitCell splitterCell) {
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
		return getIds().size() + 1;
	}

	@Override
	public <K> K accept(NiReportDataVisitor<K> visitor) {
		return visitor.visit(this);
	}
}
