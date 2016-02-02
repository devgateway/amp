package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiUtils;
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
	
	/** null for splitter cells */
	protected final HierarchiesTracker hiersTracker;
	
	public NiCell(Cell cell, NiReportedEntity<?> entity, HierarchiesTracker hiersTracker) {
		NiUtils.failIf(cell == null, "not allowed to have NiCells without contents");
		this.cell = cell;
		this.undefinedCell = cell.entityId <= 0;
		this.entity = entity;
		this.hiersTracker = hiersTracker;
	}

	public NiCell advanceHierarchy(Cell newContents, Cell splitCell) {
		return new NiCell(newContents, entity, hiersTracker.advanceHierarchy(splitCell));
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
