package org.dgfoundation.amp.nireports.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	/**
	 * a read-only view of {@link #rawSubreports}
	 */
	public final List<NiReportData> subreports;
	
	/**
	 * the column of the hierarchy used to generate the children of this GRD
	 */
	public final String splitterColumn;
	
	/**
	 * an in-place modifiable list of children. Should not be accessed outside this class!
	 */
	protected final List<NiReportData> rawSubreports;

	public NiGroupReportData(List<NiReportData> subreports, Map<CellColumn, NiOutCell> trailCells, NiSplitCell splitter) {
		super(trailCells, subreports.stream().flatMap(z -> z.getIds().stream()).collect(Collectors.toSet()), splitter);
		this.rawSubreports = new ArrayList<>(subreports);
		this.subreports = Collections.unmodifiableList(rawSubreports);
		this.splitterColumn = this.rawSubreports.isEmpty() ? null : this.rawSubreports.get(0).getGeneratingColumn();
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

	public void reorder(boolean ascending) {
		Comparator<NiReportData> comparator = Comparator.nullsFirst(Comparator.comparing(NiReportData::getSplitter));
		rawSubreports.sort(ascending ? comparator : comparator.reversed());
	}
	
	@Override
	public <K> K accept(NiReportDataVisitor<K> visitor) {
		return visitor.visit(this);
	}
}
