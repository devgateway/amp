package org.dgfoundation.amp.nireports.output;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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
	Map<CellColumn, NiOutCell> buildGroupTrailCells(GroupReportData grd, List<NiReportData> visitedChildren) {
		return headers.leafColumns.stream().collect(toMap(Function.identity(), cellColumn ->
			cellColumn.getBehaviour().buildGroupTrailCell(grd, cellColumn, visitedChildren)));
	}
		
	/**
	 * builds the trail cells for ColumnReportData 
	 */
	Map<CellColumn, NiOutCell> buildTrailCells(ColumnReportData crd, Map<CellColumn, Map<Long, NiOutCell>> mappedContents) {
		return headers.leafColumns.stream().collect(toMap(Function.identity(), cellColumn -> 
			cellColumn.getBehaviour().buildColumnTrailCell(crd, cellColumn, mappedContents)));
	}
		
	@Override
	public NiReportData visitLeaf(ColumnReportData crd) {
		//System.out.format("visiting leaf %s", crd);
		Map<CellColumn, Map<Long, NiOutCell>> contents = AmpCollections.remap(crd.getContents(), (cellColumn, columnContents) -> columnContents.flatten(cellColumn.getBehaviour()), null);
		return new NiColumnReportData(contents, buildTrailCells(crd, contents), crd.splitter);
	}

	@Override
	public NiReportData visitGroup(GroupReportData grd) {
		//System.out.format("visiting grd %s with %d subreports: %s\n", grd, grd.getSubReports().size(), grd.getSubReports());
		List<NiReportData> visitedChildren = grd.getSubReports().stream().map(z -> z.accept(this)).collect(toList());
		return new NiGroupReportData(visitedChildren, buildGroupTrailCells(grd, visitedChildren), grd.splitter);
	}
		
}
