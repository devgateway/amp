package org.dgfoundation.amp.nireports;

import java.util.function.Function;

/**
 * an internal consumption Cell, which roughly corresponds to an API ReportCell
 * @author Dolghier Constantin
 *
 */
public abstract class Cell implements Comparable<Cell>/*extends ReportCell */{
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
	
	public static Function<Cell, Long> TO_ACTIVITY_ID = cell -> cell.activityId;
}
