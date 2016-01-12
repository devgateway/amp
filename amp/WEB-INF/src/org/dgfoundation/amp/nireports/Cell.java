package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;

/**
 * an internal consumption Cell, which roughly corresponds to an API ReportCell.
 * When subclassing, make sure you subclass {@link #buildCopy()}
 * @author Dolghier Constantin
 *
 */
public abstract class Cell implements Comparable, CategCell {
	public final long activityId;
	public final long entityId;
		
	public Cell(long activityId, long entityId) {
		this.activityId = activityId;
		this.entityId = entityId;
	}
	
	public Cell(long activityId) {
		this(activityId, -1);
	}
	
	public abstract String getDisplayedValue();
}
