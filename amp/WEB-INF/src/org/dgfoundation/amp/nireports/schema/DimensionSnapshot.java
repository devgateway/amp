package org.dgfoundation.amp.nireports.schema;

import java.util.List;

/**
 * a snapshot of a {@link NiDimension} at a given moment
 * @author Dolghier Constantin
 *
 */
public class DimensionSnapshot {
	public final List<DimensionLevel> data;
	
	public DimensionSnapshot(List<DimensionLevel> data) {
		this.data = data;
	}
}
