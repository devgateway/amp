package org.dgfoundation.amp.nireports.output;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.NiHeaderInfo;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.ReportData;
import org.dgfoundation.amp.nireports.runtime.ReportDataVisitor;

/**
 * a visitor which does vertical reductions on {@link ReportData}, thus converting NiReports-internal structures into NiReports-output structures
 * @author Dolghier Constantin, Chihai Viorel
 *
 */
public class NiReportDataOutputter implements ReportDataVisitor<NiReportData> {
		
	final NiHeaderInfo headers;
	public NiReportDataOutputter(NiHeaderInfo headers) {
		this.headers = headers;
	}
		
	/**
	 * builds the trail cells for GroupReportData 
	 */
	Map<CellColumn, NiOutCell> buildGroupTrailCells(List<NiReportData> visitedChildren) {
		return headers.leafColumns.stream().collect(Collectors.toMap(cellColumn -> cellColumn, cellColumn -> 
			cellColumn.getBehaviour().doVerticalReduce(visitedChildren.stream().map(child -> child.trailCells.get(cellColumn)).collect(Collectors.toList()))));
	}
		
	/**
	 * builds the trail cells for ColumnReportData 
	 */
	Map<CellColumn, NiOutCell> buildTrailCells(Map<CellColumn, Map<Long, NiOutCell>> contents) {
		return headers.leafColumns.stream().collect(Collectors.toMap(cellColumn -> cellColumn, cellColumn -> 
			cellColumn.getBehaviour().doVerticalReduce(contents.get(cellColumn).values())));
	}
		
	@Override
	public NiReportData visitLeaf(ColumnReportData crd) {
		Map<CellColumn, Map<Long, NiOutCell>> contents = AmpCollections.remap(crd.getContents(), (cellColumn, columnContents) -> columnContents.flatten(cellColumn.getBehaviour()), null);
		return new NiColumnReportData(contents, buildTrailCells(contents), crd.splitter);
	}

	@Override
	public NiReportData visitGroup(GroupReportData grd) {
		List<NiReportData> visitedChildren = grd.getSubReports().stream().map(z -> z.accept(this)).collect(toList());
		return new NiGroupReportData(visitedChildren, buildGroupTrailCells(visitedChildren), grd.splitter);
	}
		
}
