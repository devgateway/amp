package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.ReportData;

/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	final Map<CellColumn, ColumnContents> contents;
	
	public ColumnReportData(NiReportsEngine context, NiCell splitter, Map<CellColumn, ColumnContents> contents) {
		super(context, splitter);
		this.contents = Collections.unmodifiableMap(contents);
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Set<Long> getIds() {
		Set<Long> res = new HashSet<>();
		for(ColumnContents col:contents.values())
			res.addAll(col.data.keySet());
		return res;
	}

	@Override
	public GroupReportData horizSplit(CellColumn z) {
		ColumnContents dataColumn = contents.get(z);
		NiUtils.failIf(dataColumn == null, String.format("could not find leaf %s in %s", z, this));
		Map<Long, Set<Long>> actIds = new HashMap<>(); // Map<entityId, Set<mainIds-which-have-this-value>>
		Map<Long, NiCell> values = new HashMap<>(); // Map<entityId, entity_value>
		for(NiCell splitCell:dataColumn.getLinearData()) {
			Cell cell = splitCell.getCell();
			long entityId = cell.entityId;
			values.putIfAbsent(entityId, splitCell);
			actIds.computeIfAbsent(entityId, zz -> new HashSet<>()).add(cell.activityId);
		}

		List<Long> orderedCatIds = new ArrayList<>(values.keySet());
		orderedCatIds.sort((catIdA, catIdB) -> values.get(catIdA).compareTo(values.get(catIdB)));

		GroupReportData res = new GroupReportData(context, splitter);
		for(long catId:orderedCatIds) {
			NiCell splitCell = values.get(catId);
			Map<CellColumn, ColumnContents> subContents = new HashMap<>();
			for(CellColumn cc:contents.keySet()) {
				ColumnContents oldContents = contents.get(cc);
				ColumnContents newContents = cc.getBehaviour().horizSplit(oldContents, splitCell, actIds.get(catId));
				subContents.put(cc, newContents);
			}
			ColumnReportData sub = new ColumnReportData(context, splitCell, subContents);
			res.addSubReport(sub);
		}
		return res;
	}
	
	public Map<CellColumn, ColumnContents> getContents() {
		return contents;
	}

	@Override
	public int computeRowSpan(boolean summaryReport) {
		if (summaryReport)
			return 1;
		return getIds().size()/* + 1*/;
	}
	
}
