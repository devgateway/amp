package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

	public final String text;
	
	public TextCell(String text, long activityId, long entityId) {
		super(activityId, entityId);
		this.text = text == null ? "" : text;
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
	public String getDisplayedValue() {
		return text;
	}
}
