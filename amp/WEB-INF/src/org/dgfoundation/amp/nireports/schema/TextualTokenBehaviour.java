package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class TextualTokenBehaviour implements Behaviour {
	
	public final static TextualTokenBehaviour instance = new TextualTokenBehaviour(); 
	private TextualTokenBehaviour(){}

	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	@Override
	public Cell doHorizontalReduce(List<NiCell> cells, HierarchiesTracker hiersTracker) {
		Set<String> v = new TreeSet<>();
		for(NiCell niCell:cells) {
			TextCell cell = (TextCell) niCell.getCell();
			if (!niCell.isUndefinedCell())
				v.add(cell.text);
		}
		String text = v.toString();
		text = text.substring(1, text.length() - 1);
		return new TextCell(text, cells.get(0).getMainId());
	}

	@Override
	public Cell getZeroCell() {
		return new TextCell("", -1);
	}
}
