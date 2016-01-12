package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class TextualTokenBehaviour implements Behaviour {
	
	public final static TextualTokenBehaviour instance = new TextualTokenBehaviour(); 
	private TextualTokenBehaviour(){}

	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	@Override
	public Cell doHorizontalReduce(List<NiCell> cells) {
		Set<String> v = new TreeSet<>();
		for(NiCell niCell:cells) {
			TextCell cell = (TextCell) niCell.getCell();
			v.add(cell.text);
		}
		return new TextCell(v.size() == 1 ? v.iterator().next().toString() : v.toString(), cells.get(0).getMainId(), -1);
	}


	@Override
	public Cell getZeroCell() {
		return new TextCell("", -1, -1);
	}
}
