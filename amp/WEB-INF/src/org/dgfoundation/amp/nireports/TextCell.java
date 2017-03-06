package org.dgfoundation.amp.nireports;

import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * a {@link Cell} which holds a text. Coordinates are either empty or with a single entry, depending on the #mainLevel)
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

	public final String text;
	public final MetaInfoSet metaInfo;

	public TextCell(String text, long activityId, long entityId, Optional<LevelColumn> levelColumn) {
		this(text, activityId, entityId, MetaInfoSet.empty(), levelColumn);
	}

	public TextCell(String text, long activityId, long entityId, MetaInfoSet metaInfo, Optional<LevelColumn> levelColumn) {
		super(activityId, entityId, buildCoordinates(levelColumn, entityId), levelColumn);
		this.text = (text == null) ? "" : text;
		this.metaInfo = metaInfo.freeze();
	}
	
	@Override
	public TextCell changeOwnerId(long newActivityId) {
		return new TextCell(this.text, newActivityId, this.entityId, metaInfo, this.mainLevel);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return metaInfo;
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
