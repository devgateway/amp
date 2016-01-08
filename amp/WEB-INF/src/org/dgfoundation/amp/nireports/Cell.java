package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;

/**
 * an internal consumption Cell, which roughly corresponds to an API ReportCell.
 * When subclassing, make sure you subclass {@link #buildCopy()}
 * @author Dolghier Constantin
 *
 */
public abstract class Cell implements Comparable<Cell>, CategCell /*extends ReportCell */{
	public final long activityId;
	public final long entityId;
	public final Comparable value;
	public final String displayedValue;
		
	public Cell(Comparable<?> value, String displayedValue, long activityId, long entityId) {
		this.value = value;
		this.displayedValue = displayedValue;
		this.activityId = activityId;
		this.entityId = entityId;
	}
	
	public Cell(Comparable<?> value, String displayedValue, long activityId) {
		this(value, displayedValue, activityId, -1);
	}

	@Override
	public int compareTo(Cell other) {
		return value.compareTo(other.value);
	}
	
	@Override
	public String toString() {
		return String.format("(actId: %d, <%s>)", activityId, displayedValue);
	}
}
