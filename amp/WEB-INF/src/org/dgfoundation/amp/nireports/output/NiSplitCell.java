package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * a cell which holds a text
 * @author Dolghier Constantin
 *
 */
public class NiSplitCell implements NiOutCell {
	public final String text;
	public final Set<Long> entityIds;
	public final boolean undefined;
	public final NiReportColumn<?> entity;
	
	public NiSplitCell(NiReportColumn<?> entity, String text, Set<Long> entityIds, boolean undefined) {
		this.entity = entity;
		this.text = text;
		this.entityIds = Collections.unmodifiableSet(new HashSet<>(entityIds));
		this.undefined = undefined;
	}
	
	public LevelColumn getLevelColumn() {
		return entity.levelColumn.get(); // will crash if somehow we have tried to create a splitcell on a cell without a main entity
	}
	
	@Override
	public int compareTo(Object o) {
		NiSplitCell ntc = (NiSplitCell) o;
		if (undefined && ntc.undefined)
			return 0;
		
		if (undefined ^ ntc.undefined) {
			if (undefined)
				return 1;
			return -1;
		}

		return this.text.compareTo(ntc.text);
	}

	@Override
	public String toString() {
		return getDisplayedValue();
	}
	
	@Override
	public String getDisplayedValue() {
		return text;
	}

	@Override
	public <K> K accept(CellVisitor<K> visitor) {
		return visitor.visit(this);
	}

}
