package org.dgfoundation.amp.nireports.output;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * visitor which makes the final cleanup of data for the NiReports outputs.
 * Now it cleans up non-header-referenced terminals from trail cells and contents.
 * TODO: cleanup hierarchy-levels 
 * @author Dolghier Constantin
 *
 */
public class NiReportOutputCleaner implements NiReportDataVisitor<NiReportData> {

	final Set<CellColumn> kk;
	
	public NiReportOutputCleaner(NiHeaderInfo headers) {
		this.kk = new HashSet<>(headers.leafColumns);
	}
	
	@Override
	public NiReportData visit(NiColumnReportData crd) {
		return new NiColumnReportData(keepKeys(crd.contents, kk), keepKeys(crd.trailCells, kk), crd.splitter);
	}

	@Override
	public NiReportData visit(NiGroupReportData grd) {
		List<NiReportData> subreports = grd.subreports.stream().map(z -> z.accept(this)).collect(Collectors.toList());
		return new NiGroupReportData(subreports, keepKeys(grd.trailCells, kk), grd.splitter);
	}

	protected<K, V> Map<K, V> keepKeys(Map<K, V> in, Set<K> okKeys) {
		return in.entrySet().stream().filter(z -> okKeys.contains(z.getKey())).collect(Collectors.toMap(z -> z.getKey(), z -> z.getValue()));
	}
}
