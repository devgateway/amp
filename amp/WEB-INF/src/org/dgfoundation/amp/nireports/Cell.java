package org.dgfoundation.amp.nireports;

import java.util.function.Function;

import org.dgfoundation.amp.newreports.ReportCell;

/**
 * an internal consumption Cell, which roughly corresponds to an API ReportCell
 * @author Dolghier Constantin
 *
 */
public abstract class Cell extends ReportCell {
	public final long activityId;
	public final long entityId;
	
	public Cell(Comparable<?> value, String displayedValue, long activityId, long entityId) {
		super(value, displayedValue);
		this.activityId = activityId;
		this.entityId = entityId;
	}
	
	public Cell(Comparable<?> value, String displayedValue, long activityId) {
		this(value, displayedValue, activityId, -1);
	}
	
	public static Function<Cell, Long> TO_ACTIVITY_ID = cell -> cell.activityId;
}
