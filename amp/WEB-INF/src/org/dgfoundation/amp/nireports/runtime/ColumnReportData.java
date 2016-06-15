package org.dgfoundation.amp.nireports.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import static org.dgfoundation.amp.algo.AmpCollections.remap;
/**
 * a leaf of a report - the bottom hierarchy, without any subreports
 * @author Dolghier Constantin
 *
 */
public class ColumnReportData extends ReportData {
	final Map<CellColumn, ColumnContents> contents;
	public final static long UNALLOCATED_ID = -999999999l;
	
	public ColumnReportData(NiReportsEngine context, NiSplitCell splitter, Map<CellColumn, ColumnContents> contents) {
		super(context, splitter);
		this.contents = Collections.unmodifiableMap(contents);
	}
	
	public ColumnReportData replaceContents(Map<CellColumn, ColumnContents> contents) {
		return new ColumnReportData(this.context, this.splitter, contents);
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Set<Long> _getIds() {
		Set<Long> res = new HashSet<>();
		for(ColumnContents col:contents.values())
			res.addAll(col.data.keySet());
		return res;
	}
	
	class SplitDigest {
		final Behaviour<?> behaviour;
		final NiReportColumn<?> schemaColumn;
		final ColumnContents contents;
		
		Map<Long, Set<Long>> actIds = new HashMap<>(); // Map<entityId, Set<mainIds-which-have-this-value>>
		Map<Long, List<NiCell>> splitterArrays = new HashMap<>(); // Map<entityId, entity_value>
		Map<Long, Map<Long, Cell>> percentages = new HashMap<>(); // Map<entityId, Map<activityId, Percentage>>
		
		public SplitDigest(NiReportColumn<?> schemaColumn, ColumnContents contents, Behaviour<?> behaviour, ColumnContents wholeColumn, Supplier<Set<Long>> allIds) {
			this.contents = contents;
			this.schemaColumn = schemaColumn;
			this.behaviour = behaviour;
			
			for(NiCell splitCell:contents.getLinearData()) {
				processCell(splitCell);
			}
			
			Set<Long> missingActIdsInSplitterColumn = new HashSet<>(allIds.get());
			if (!context.schema.isTransactionLevelHierarchy(schemaColumn, context))
				missingActIdsInSplitterColumn.removeAll(wholeColumn.data.keySet());
			//missingActIdsInSplitterColumn.clear();
			
			for(long missingActId:missingActIdsInSplitterColumn) {
				try {
				Cell unallocatedCell = behaviour.buildUnallocatedCell(missingActId, UNALLOCATED_ID, schemaColumn.levelColumn.get()); //TODO: will crash if making hierarchies on a non-same-level column
				
				NiCell unallocatedSplitterCell = new NiCell(unallocatedCell, schemaColumn, null);
				processCell(unallocatedSplitterCell);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		
		public void processCell(NiCell splitCell) {
			Cell cell = splitCell.cell;
			long entityId = cell.entityId;
			splitterArrays.computeIfAbsent(entityId, zz-> new ArrayList<>()).add(splitCell);
			actIds.computeIfAbsent(entityId, zz -> new HashSet<>()).add(cell.activityId);
			percentages.computeIfAbsent(entityId, zz -> new HashMap<>()).put(cell.activityId, splitCell.getCell());
		}
		
		public Map<Long, NiSplitCell> buildSplitters() {
			return splitterArrays.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry-> behaviour.mergeSplitterCells(entry.getValue())));			
		}
	}

	@Override
	public GroupReportData horizSplit(CellColumn z) {
		ColumnContents dataColumn = contents.get(z);
		NiUtils.failIf(dataColumn == null, () -> String.format("could not find leaf %s in %s", z, this));
		return horizSplit(dataColumn, z.contents, z.getBehaviour(), (NiReportColumn<?>) z.entity, false); //setting this to true should NOT change the output of the report, but triple-hier would run 7% slower
	}
	
	public GroupReportData horizSplit(ColumnContents dataColumn, ColumnContents wholeColumn, Behaviour<?> behaviour, NiReportColumn<?> schemaColumn, boolean enqueueAcceptors) {
		SplitDigest splitDigest = new SplitDigest(schemaColumn, dataColumn, behaviour, wholeColumn, this::getIds);
		
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
				ColumnContents newContents = cc.getBehaviour().horizSplit(oldContents, splitDigest.percentages.get(catId), splitDigest.actIds.get(catId), acceptors, enqueueAcceptors);
//				if (cc.getHierName().equals("RAW / Funding / 2006 / Actual Commitments"))
//					System.err.format("splitting %s.%s by %s.%s; %d cells became %d: %s\n", this, cc.getHierName(), z.getHierName(), splitCell.toString(), oldContents.countCells(), newContents.countCells(), newContents);
				subContents.put(cc, newContents);
				
				if (cc.getBehaviour().isKeepingSubreports())
					entitiesWithFunding.addAll(newContents.data.keySet());
			}
			if (!keepEmptyFundingRows)
				subContents.values().forEach(cc -> cc.keepEntries(entitiesWithFunding));
			if (keepEmptyFundingRows || (!entitiesWithFunding.isEmpty())) {
				ColumnReportData sub = new ColumnReportData(context, splitCell, subContents);
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
