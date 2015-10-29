package org.dgfoundation.amp.nireports;

import com.google.common.base.Function;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class Cell {
	public final long activityId;
	public final long entityId;
	
	public Cell(long activityId, long entityId) {
		this.activityId = activityId;
		this.entityId = entityId;
	}
	
	public Cell(long activityId) {
		this(activityId, -1);
	}
	
	public static Function<Cell, Long> TO_ACTIVITY_ID = new Function<Cell, Long>() {
		@Override public Long apply(Cell cell) {
			return cell.activityId;
		}
	};
}
