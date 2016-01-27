package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	final Map<CellColumn, ColumnContents> contents;
	
	public ColumnReportData(NiReportsEngine context, NiCell splitter, Map<CellColumn, ColumnContents> contents, HierarchiesTracker hierarchies) {
		super(context, splitter, hierarchies);
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

	//TODO: test this one
	protected void add(Map<Long, BigDecimal> map, long key, BigDecimal add) {
		if (add == null)
			map.put(key, add); // anything + null = null
		
		if (!map.containsKey(key))
			map.put(key, add);
		
		if (map.get(key) == null)
			return; // null + anything = null
		
		map.put(key, map.get(key).add(add));
	}
	
	@Override
	public GroupReportData horizSplit(CellColumn z) {
		ColumnContents dataColumn = contents.get(z);
		NiUtils.failIf(dataColumn == null, String.format("could not find leaf %s in %s", z, this));
		Map<Long, Set<Long>> actIds = new HashMap<>(); // Map<entityId, Set<mainIds-which-have-this-value>>
		Map<Long, NiCell> values = new HashMap<>(); // Map<entityId, entity_value>
		Map<Long, Map<Long, NiCell>> percentages = new HashMap<>(); // Map<entityId, Map<activityId, Percentage>>
		for(NiCell splitCell:dataColumn.getLinearData()) {
			Cell cell = splitCell.getCell();
			long entityId = cell.entityId;
			values.putIfAbsent(entityId, splitCell);
			actIds.computeIfAbsent(entityId, zz -> new HashSet<>()).add(cell.activityId);
			percentages.computeIfAbsent(entityId, zz -> new HashMap<>()).put(cell.activityId, splitCell);
			/*Map<Long, BigDecimal> catPercs = percentages.computeIfAbsent(entityId, zz -> new HashMap<>());
			add(catPercs, cell.activityId, cell.getPercentage());*/
		}

		List<Long> orderedCatIds = new ArrayList<>(values.keySet());
		orderedCatIds.sort((catIdA, catIdB) -> values.get(catIdA).compareTo(values.get(catIdB)));

		GroupReportData res = new GroupReportData(context, splitter, hierarchies);
		IdsAcceptorsBuilder bld = context;
		for(long catId:orderedCatIds) {
			NiCell splitCell = values.get(catId);
			Map<NiDimensionUsage, IdsAcceptor> acceptors = AmpCollections.remap(splitCell.cell.getCoordinates(), bld::buildAcceptor, null);
			Map<CellColumn, ColumnContents> subContents = new HashMap<>();
			for(CellColumn cc:contents.keySet()) {
				ColumnContents oldContents = contents.get(cc);
				ColumnContents newContents = cc.getBehaviour().horizSplit(oldContents, splitCell, actIds.get(catId), acceptors);
				subContents.put(cc, newContents);
			}
			ColumnReportData sub = new ColumnReportData(context, splitCell, subContents, hierarchies.advanceHierarchy(percentages.get(catId)));
			res.addSubReport(sub);
		}
		return res;
	}
	
	public Map<CellColumn, ColumnContents> getContents() {
		return contents;
	}

	@Override
	public <K> K accept(ReportDataVisitor<K> visitor) {
		return visitor.visitLeaf(this);
	}
}
