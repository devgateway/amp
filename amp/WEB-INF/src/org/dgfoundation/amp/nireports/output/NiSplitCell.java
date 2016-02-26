package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

/**
 * a cell which holds info regarding the splitter cell of a hierarchy element
 * @author Dolghier Constantin
 *
 */
public class NiSplitCell extends NiOutCell {
	public final String text;
	public final Set<Long> entityIds;
	public final boolean undefined;
	public final NiReportColumn<?> entity;
	
	public NiSplitCell(NiReportColumn<?> entity, String text, Set<Long> entityIds, boolean undefined) {
		Objects.requireNonNull(entity);
		Objects.requireNonNull(text);
		this.entity = entity;
		this.text = text;
		this.entityIds = Collections.unmodifiableSet(new HashSet<>(entityIds));
		this.undefined = undefined;
	}
	
	public LevelColumn getLevelColumn() {
		return entity.levelColumn.get(); // will crash if somehow we have tried to create a splitcell on a cell without a main entity
	}
	
	/**
	 * merges a number of NiSplitCells into a split cell. #text and #entity are taken from an arbitrary cell of the set (they should all be the same)
	 * @param in
	 * @return
	 */
	public static NiSplitCell merge(Stream<NiSplitCell> in) {
		List<NiSplitCell> cells = in.collect(Collectors.toList());
		String text = cells.get(0).text;
		Set<Long> entityIds = new HashSet<>();
		boolean undefined = false;
		NiReportColumn<?> entity = cells.get(0).entity;
		for(NiSplitCell cell:cells) {
			entityIds.addAll(cell.entityIds);
			undefined |= cell.undefined;
		}
		return new NiSplitCell(entity, text, entityIds, undefined);
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
	public <K> K accept(CellVisitor<K> visitor, CellColumn niCellColumn) {
		return visitor.visit(this, niCellColumn);
	}

}
