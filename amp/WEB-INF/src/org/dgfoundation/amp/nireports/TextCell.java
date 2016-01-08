package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * @author Dolghier Constantin
 *
 */
public final class TextCell extends Cell {

	public TextCell(String text, long activityId, long entityId) {
		super(text, text, activityId, entityId);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}
}
