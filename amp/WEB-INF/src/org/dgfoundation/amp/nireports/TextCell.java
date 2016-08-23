package org.dgfoundation.amp.nireports;

import java.util.Objects;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

	public final String text;
	
	public TextCell(String text, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
		super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
		Objects.requireNonNull(text);
		this.text = text == null ? "" : text;
	}
	
	@Override
	public TextCell changeOwnerId(long newActivityId) {
		return new TextCell(this.text, newActivityId, this.entityId, this.mainLevel);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}

	@Override
	public int compareTo(Object o) {
		TextCell tc = (TextCell) o;
		return text.compareTo(tc.text);
	}

	@Override
	public String toString() {
		//String entityStr = this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "";
		String entityStr = String.format(", eid: %d", this.entityId);
		return String.format("%s (id: %d%s%s)", text, this.activityId, entityStr, coordinates.isEmpty() ? "" : String.format(", coos: %s", coordinates));
	}
	
	@Override
	public String getDisplayedValue() {
		return text;
	}
}
