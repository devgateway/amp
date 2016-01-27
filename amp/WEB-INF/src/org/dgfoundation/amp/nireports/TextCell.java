package org.dgfoundation.amp.nireports;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

	public final String text;
	
	public TextCell(String text, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
		super(activityId, entityId, buildCoordinates(levelColumn, entityId));
		Objects.requireNonNull(text);
		this.text = text == null ? "" : text;
	}

	public TextCell(String text, long activityId) {
		this(text, activityId, -1, Optional.empty());
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
		return String.format("%s (id: %d%s)", text, this.activityId, this.entityId > 0 ? String.format(", eid: %d", this.entityId) : "");
	}
	
	@Override
	public String getDisplayedValue() {
		return text;
	}
}
