package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker.SplitCellPercentage;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * a class whose instances as a per-cell tag by the NiReportsEngine. This class should be totally opaque for clients!
 * @author Dolghier Constantin
 *
 */
public class NiCell implements Comparable<NiCell> {
	protected final NiReportedEntity<?> entity;
	protected final Cell cell;
	protected final boolean undefinedCell;
	
	protected final HierarchiesTracker hiersTracker;
	
	protected final Map<NiDimensionUsage, List<IdsAcceptor>> passedFilters;
	
	public NiCell(Cell cell, NiReportedEntity<?> entity, HierarchiesTracker hiersTracker) {
		this(cell, entity, hiersTracker, Collections.emptyMap());
	}
	
	public NiCell(Cell cell, NiReportedEntity<?> entity, HierarchiesTracker hiersTracker, Map<NiDimensionUsage, List<IdsAcceptor>> passedFilters) {
		NiUtils.failIf(cell == null, "not allowed to have NiCells without contents");
		this.cell = cell;
		this.undefinedCell = cell.entityId <= 0;
		this.entity = entity;
		this.hiersTracker = hiersTracker;
		this.passedFilters = Collections.unmodifiableMap(passedFilters);
	}

	public NiCell advanceHierarchy(Cell newContents, Cell splitCell, Map<NiDimensionUsage, IdsAcceptor> acceptors) {
		return new NiCell(newContents, entity, hiersTracker.advanceHierarchy(splitCell), mergeAcceptors(acceptors));
	}
	
	public Map<NiDimensionUsage, List<IdsAcceptor>> mergeAcceptors(Map<NiDimensionUsage, IdsAcceptor> acceptors) {
		if (acceptors == null || acceptors.isEmpty())
			return passedFilters;
		
		Map<NiDimensionUsage, List<IdsAcceptor>> res = new HashMap<>();
		passedFilters.forEach((dimUsg, oldList) -> {
			List<IdsAcceptor> resList = acceptors.containsKey(dimUsg) ? AmpCollections.cat(oldList, acceptors.get(dimUsg)) : oldList;
			res.put(dimUsg, resList);
		});
		for(NiDimensionUsage dimUsg:acceptors.keySet()) {
			if (!res.containsKey(dimUsg)) // else the element has been added in the list above
				res.put(dimUsg, Arrays.asList(acceptors.get(dimUsg)));
		}		
		return res;
	}
	
	public NiReportedEntity<?> getEntity() {
		return entity;
	}

	public Cell getCell() {
		return cell;
	}
	
	public long getMainId() {
		return cell.activityId;
	}
	
	public boolean isUndefinedCell() {
		return this.undefinedCell;
	}

	public Map<NiDimensionUsage, List<IdsAcceptor>> getPassedFilters() {
		return this.passedFilters;
	}
	
	public boolean passesFilters(Cell splitCell) {
//		boolean debug = this.cell instanceof TextCell; 
//		if (debug) {
//			System.err.format("testing passesFilters for id %d, <%s>, passedFilters <%s>...", System.identityHashCode(this), this.cell, this.passedFilters);
//		}
		for(Map.Entry<NiDimensionUsage, Coordinate> splitCellCoo:splitCell.getCoordinates().entrySet()) {
			if (this.passedFilters.containsKey(splitCellCoo.getKey())) {
				for(IdsAcceptor acceptor:this.passedFilters.get(splitCellCoo.getKey())) {
					if (!acceptor.isAcceptable(splitCellCoo.getValue())) {
						//if (debug) System.err.format("FAILED\n", 1);
						return acceptor.isAcceptable(splitCellCoo.getValue());
					}
				}
			}
		}
		//if (debug) System.err.format("OK\n", 1);
		return true;
	}
	
	public BigDecimal calculatePercentage() {
		return hiersTracker.calculatePercentage(getEntity().getBehaviour().getHierarchiesListener()).setScale(6, RoundingMode.HALF_EVEN); //TODO: maybe use the per-report precision setting
	}
	
	@Override
	public int compareTo(NiCell o) {
		if (undefinedCell && o.undefinedCell)
			return 0;
		if (undefinedCell ^ o.undefinedCell) {
			if (undefinedCell)
				return 1;
			return -1;
		}
		
		// gone till here -> neither of the cells is undefined
		return cell.compareTo(o.cell);
	}
	
	public String getDisplayedValue() {
		return cell.getDisplayedValue();
	}
	
	public HierarchiesTracker getHiersTracker() {
		return this.hiersTracker;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", cell.getDisplayedValue(), hiersTracker == null ? "" : hiersTracker.toString());
	}
}
