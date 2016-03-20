package org.dgfoundation.amp.nireports.output.sorting;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataVisitor;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a Visitor which sorts the output
 * @author Dolghier Constantin
 *
 */
public class NiReportSorter implements NiReportDataVisitor<NiReportData> {

	final Map<String, Boolean> hiersSorting;
	final Map<CellColumn, Boolean> colsSorting;
	
	public NiReportSorter(Map<String, Boolean> hiersSorting, Map<CellColumn, Boolean> colsSorting) {
		this.hiersSorting = hiersSorting;
		this.colsSorting = colsSorting;
	}
	
	public static NiReportSorter buildFor(NiReportsEngine engine) {
		List<SortingInfo> sInfo = Optional.ofNullable(engine.spec.getSorters()).orElse(Collections.emptyList());
		Map<String, Boolean> hiersSorting = sInfo.stream().filter(z -> !z.isTotals && z.sortByTuple != null && z.sortByTuple.size() == 1 && engine.actualHierarchies.contains(z.getColumnName(0))).collect(Collectors.toMap(z -> z.getColumnName(0), z -> z.ascending));
		Map<CellColumn, Boolean> colsSorting = sInfo.stream().filter(null).collect(Collectors.toMap(z -> null, z -> true));
		return new NiReportSorter(hiersSorting, colsSorting);
	}

	@Override
	public NiReportData visit(NiColumnReportData crd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NiReportData visit(NiGroupReportData grd) {
		// TODO Auto-generated method stub
		return null;
	}

}
