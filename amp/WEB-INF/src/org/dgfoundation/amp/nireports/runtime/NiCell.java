package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * a class whose instances as a per-cell tag by the NiReportsEngine. This class should be totally opaque for clients!
 * @author Dolghier Constantin
 *
 */
public class NiCell implements Comparable<NiCell> {
	/** null for trail cells */
	protected final NiReportedEntity<?> entity;
	protected final Cell cell;
	protected final boolean undefinedCell;
	
	public NiCell(Cell cell, NiReportedEntity<?> entity) {
		this.cell = cell;
		this.undefinedCell = cell.entityId <= 0;
		this.entity = entity;
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
	
	@Override
	public String toString() {
		return cell.getDisplayedValue();
	}
}
