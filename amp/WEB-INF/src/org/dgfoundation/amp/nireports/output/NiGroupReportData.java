package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * a flattened non-leaf node of a report output (see {@link GroupReportData})
 * @author Dolghier Constantin
 *
 */
public class NiGroupReportData extends NiReportData {
	public final List<NiReportData> subreports;

	public NiGroupReportData(List<NiReportData> subreports, Map<CellColumn, NiOutCell> trailCells, NiSplitCell splitter) {
		super(trailCells, subreports.stream().flatMap(z -> z.getIds().stream()).collect(Collectors.toSet()), splitter);
		this.subreports = Collections.unmodifiableList(subreports);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public int computeRowSpan(boolean summaryReport) {
		int sum = 1;
		for(NiReportData rd:subreports)
			sum += rd.getRowSpan(summaryReport);
		return sum;
	}

	@Override
	public <K> K accept(NiReportDataVisitor<K> visitor) {
		return visitor.visit(this);
	}
}
