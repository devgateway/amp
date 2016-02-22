package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import static org.dgfoundation.amp.algo.AmpCollections.remap;
import static org.dgfoundation.amp.algo.AmpCollections.keepEntries;
/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	final Map<CellColumn, ColumnContents> contents;
	
	public ColumnReportData(NiReportsEngine context, NiSplitCell splitter, Map<CellColumn, ColumnContents> contents) {
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

//	//TODO: test this one
//	protected void add(Map<Long, BigDecimal> map, long key, BigDecimal add) {
//		if (add == null)
//			map.put(key, add); // anything + null = null
//		
//		if (!map.containsKey(key))
//			map.put(key, add);
//		
//		if (map.get(key) == null)
//			return; // null + anything = null
//		
//		map.put(key, map.get(key).add(add));
//	}
	
	class SplitDigest {
		final CellColumn cellColumn;
		final NiReportColumn<?> schemaColumn;
		final ColumnContents contents;
		
		Map<Long, Set<Long>> actIds = new HashMap<>(); // Map<entityId, Set<mainIds-which-have-this-value>>
		Map<Long, List<NiCell>> splitterArrays = new HashMap<>(); // Map<entityId, entity_value>
		Map<Long, Map<Long, NiCell>> percentages = new HashMap<>(); // Map<entityId, Map<activityId, Percentage>>
		
		public SplitDigest(CellColumn cellColumn, ColumnContents contents, Supplier<Set<Long>> allIds) {
			this.contents = contents;
			this.schemaColumn = (NiReportColumn<?>) cellColumn.entity;
			this.cellColumn = cellColumn;
			
			for(NiCell splitCell:contents.getLinearData()) {
				processCell(splitCell);
			}
			
//			actIds.put(UNALLOCATED_ID, new HashSet<>(ALL_IDS_NOT_ANYWHERE_ELSE));
//			splitters.put(UNALLOCATED_ID, DUMMY_UNALLOCATED_CELL);
//			percentages.put(UNALLOCATED_ID, new HashMap<ALL_IDS_NOT_ANYWHERE_ELSE, DUMMY_UNALLOCATED_CELL>());

			long UNALLOCATED_ID = -999999999;
			Set<Long> missingActIdsInSplitterColumn = new HashSet<>(allIds.get());
			missingActIdsInSplitterColumn.removeAll(cellColumn.contents.data.keySet());
			//missingActIdsInSplitterColumn.clear();
			
			for(long missingActId:missingActIdsInSplitterColumn) {
				Cell unallocatedCell = cellColumn.getBehaviour().buildUnallocatedCell(missingActId, UNALLOCATED_ID, schemaColumn.levelColumn.get()); //TODO: will crash if making hierarchies on a non-same-level column 
				NiCell unallocatedSplitterCell = new NiCell(unallocatedCell, cellColumn.entity, null);
				processCell(unallocatedSplitterCell);
			}
		}
		
		public void processCell(NiCell splitCell) {
			Cell cell = splitCell.cell;
			long entityId = cell.entityId;
			splitterArrays.computeIfAbsent(entityId, zz-> new ArrayList<>()).add(splitCell);
			actIds.computeIfAbsent(entityId, zz -> new HashSet<>()).add(cell.activityId);
			percentages.computeIfAbsent(entityId, zz -> new HashMap<>()).put(cell.activityId, splitCell);
		}
		
		public Map<Long, NiSplitCell> buildSplitters() {
			return splitterArrays.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry-> cellColumn.behaviour.mergeSplitterCells(entry.getValue())));			
		}
	}
	
	@Override
	public GroupReportData horizSplit(CellColumn z) {
		ColumnContents dataColumn = contents.get(z);
		NiUtils.failIf(dataColumn == null, () -> String.format("could not find leaf %s in %s", z, this));
				
		SplitDigest splitDigest = new SplitDigest(z, dataColumn, this::getIds);
		
		Map<Long, NiSplitCell> splitters = splitDigest.buildSplitters();
				
		List<Long> orderedCatIds = new ArrayList<>(splitters.keySet());
		orderedCatIds.sort((catIdA, catIdB) -> splitters.get(catIdA).compareTo(splitters.get(catIdB)));

		IdsAcceptorsBuilder bld = context;
		List<ColumnReportData> newChildren = new ArrayList<>();
		boolean keepEmptyFundingRows = context.spec.isDisplayEmptyFundingRows();
		
		for(long catId:orderedCatIds) {
			//NiCell splitCell = splitters.get(catId).get(0); // choose any, because they all have the same coordinates
			NiSplitCell splitCell = splitters.get(catId);
			Map<NiDimensionUsage, IdsAcceptor> acceptors = new HashMap<>(); //AmpCollections.remap(splitCell.cell.getCoordinates(), bld::buildAcceptor, null);
			IdsAcceptor acceptor = bld.buildAcceptor(splitCell.getLevelColumn().dimensionUsage, splitCell.getLevelColumn().getCoordinate(catId));
			acceptors.put(splitCell.getLevelColumn().dimensionUsage, acceptor);
			
//			boolean keepSubreport = context.spec.isDisplayEmptyFundingRows();
			Set<Long> entitiesWithFunding = new HashSet<>();
			
			Map<CellColumn, ColumnContents> subContents = new HashMap<>();
			for(CellColumn cc:contents.keySet()) {
				ColumnContents oldContents = contents.get(cc);
				ColumnContents newContents = cc.getBehaviour().horizSplit(oldContents, splitDigest.percentages.get(catId), splitDigest.actIds.get(catId), acceptors);
//				if (cc.getHierName().equals("RAW / Funding / 2006 / Actual Commitments"))
//					System.err.format("splitting %s.%s by %s.%s; %d cells became %d: %s\n", this, cc.getHierName(), z.getHierName(), splitCell.toString(), oldContents.countCells(), newContents.countCells(), newContents);
				subContents.put(cc, newContents);
				
				if (cc.getBehaviour().isKeepingSubreports())
					entitiesWithFunding.addAll(newContents.data.keySet());
			}
			if (keepEmptyFundingRows || (!entitiesWithFunding.isEmpty())) {
				ColumnReportData sub = new ColumnReportData(context, splitCell, 
					keepEmptyFundingRows ? 
						subContents : 
						remap(subContents, zz -> new ColumnContents(keepEntries(zz.data, entitiesWithFunding)), null));
				newChildren.add(sub);
			}
		}
		return this.clone(newChildren);
	}
	
	public Map<CellColumn, ColumnContents> getContents() {
		return contents;
	}

	@Override
	public <K> K accept(ReportDataVisitor<K> visitor) {
		return visitor.visitLeaf(this);
	}
	
}
