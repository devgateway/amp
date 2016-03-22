package org.dgfoundation.amp.nireports.output.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataVisitor;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import static java.util.stream.Collectors.toMap;

/**
 * a Visitor which sorts the output
 * @author Dolghier Constantin
 *
 */
public class NiReportSorter implements NiReportDataVisitor<NiReportData> {

	final LinkedHashMap<String, Boolean> hiersSorting;
	final LinkedHashMap<CellColumn, Boolean> colsSorting;
	
	public NiReportSorter(LinkedHashMap<String, Boolean> hiersSorting, LinkedHashMap<CellColumn, Boolean> colsSorting) {
		this.hiersSorting = hiersSorting;
		this.colsSorting = colsSorting;
		//System.err.format("sorting report by hiersSorting: %s, colsSorting: %s\n", hiersSorting, AmpCollections.remap(colsSorting, CellColumn::getHierName, z -> z, true));
	}
	
	public static NiReportSorter buildFor(NiReportsEngine engine) {
		List<SortingInfo> sInfo = Optional.ofNullable(engine.spec.getSorters()).orElse(Collections.emptyList());
		LinkedHashMap<String, Boolean> hiersSorting = new LinkedHashMap<>();
		LinkedHashMap<CellColumn, Boolean> colsSorting = new LinkedHashMap<>();

		Map<String, CellColumn> headerToHierName = engine.headers.leafColumns.stream().collect(toMap(cc -> cc.getHierName().replace(String.format("%s / ", NiReportsEngine.ROOT_COLUMN_NAME), ""), cc -> cc));
		
		for(SortingInfo sortItem:sInfo) {
			if (sortItem.isHierarchySorter(engine.actualHierarchies)) {
				hiersSorting.put(sortItem.hierPath.get(0), sortItem.ascending);
			} else {
				String pathName = sortItem.buildPath(" / ", NiReportsEngine.FUNDING_COLUMN_NAME, NiReportsEngine.TOTALS_COLUMN_NAME);
				CellColumn header = headerToHierName.get(pathName);
				if (header != null)
					colsSorting.put(header, sortItem.ascending);
			}
		}
		return new NiReportSorter(hiersSorting, colsSorting);
	}
	
	@Override
	public NiReportData visit(NiColumnReportData crd) {
		IdsComparator comp = new IdsComparator(colsSorting, crd);
		crd.reorder(comp);
		return crd;
	}

	@Override
	public NiReportData visit(NiGroupReportData grd) {
		if (grd.splitterColumn != null && hiersSorting.containsKey(grd.splitterColumn)) {
			Boolean sortOrder = hiersSorting.get(grd.splitterColumn);
			if (sortOrder != null)
				grd.reorder(sortOrder);
		}
		for(NiReportData child:grd.subreports)
			child.accept(this);
		return grd;
	}

}
