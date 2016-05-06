package org.dgfoundation.amp.nireports;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * @author Dolghier Constantin
 *
 */
public final class DateCell extends Cell {

	public final LocalDate date;
	
	public DateCell(LocalDate date, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
		super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
		Objects.requireNonNull(date);
		this.date = date;
	}

	@Override
	public DateCell changeOwnerId(long newActivityId) {
		return new DateCell(this.date, newActivityId, this.entityId, this.mainLevel);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}

	@Override
	public int compareTo(Object o) {
		DateCell dc = (DateCell) o;
		return date.compareTo(dc.date);
	}

	@Override
	public String toString() {
		String entityStr = this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "";
		return String.format("%s (id: %d%s, coos: %s)", date, this.activityId, entityStr, coordinates);
	}
	
	@Override
	public String getDisplayedValue() {
		return date.toString();
	}

}
