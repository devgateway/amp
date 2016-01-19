package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class PercentageTokenBehaviour implements Behaviour {

	public final static PercentageTokenBehaviour instance = new PercentageTokenBehaviour();
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private PercentageTokenBehaviour(){}
	
	@Override
	public Cell doHorizontalReduce(List<NiCell> cells) {
		Set<String> v = new TreeSet<>();
		BigDecimal percentage = BigDecimal.ZERO;
		for(NiCell niCell:cells) {
			PercentageTextCell cell = (PercentageTextCell) niCell.getCell();
			v.add(cell.text);
			percentage = percentage.add(cell.percentage);
		}
		return new PercentageTextCell(v.size() == 1 ? v.iterator().next().toString() : v.toString(), cells.get(0).getMainId(), -1, null);
	}

	@Override
	public Cell getZeroCell() {
		return new PercentageTextCell("", -1, -1, null);
	}
	
	@Override
	public Cell filterCell(NiCell oldCell, NiCell splitCell) {
		return oldCell.getCell();
	}
}
