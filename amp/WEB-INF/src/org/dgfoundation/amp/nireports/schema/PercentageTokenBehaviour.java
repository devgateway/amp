package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class PercentageTokenBehaviour implements Behaviour {

	public final static PercentageTokenBehaviour instance = new PercentageTokenBehaviour();
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private PercentageTokenBehaviour(){}
	
	@Override
	/**
	 * hierPercentage is ignored in textual cells
	 */
	public Cell doHorizontalReduce(List<NiCell> cells, HierarchiesTracker hiersTracker) {
		Set<String> v = new TreeSet<>();
		BigDecimal percentage = BigDecimal.ZERO;
		for(NiCell niCell:cells) {
			PercentageTextCell cell = (PercentageTextCell) niCell.getCell();
			if (!niCell.isUndefinedCell())
				v.add(cell.text);
			percentage = percentage.add(cell.percentage);
		}
		String text = v.toString();
		text = text.substring(1, text.length() - 1);
		return new PercentageTextCell(text, cells.get(0).getMainId(), -1, Optional.empty(), null);
	}

	@Override
	public Cell getZeroCell() {
		return new PercentageTextCell("", -1, -1, Optional.empty(), null);
	}
}
