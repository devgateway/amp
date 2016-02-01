package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toSet;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;

public class PercentageTokenBehaviour implements Behaviour<NiTextCell> {

	public final static PercentageTokenBehaviour instance = new PercentageTokenBehaviour();
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private PercentageTokenBehaviour(){}
	
	@Override
	public NiTextCell doHorizontalReduce(List<NiCell> cells) {
		Set<String> v = new TreeSet<>();
		for(NiCell niCell:cells) {
			PercentageTextCell cell = (PercentageTextCell) niCell.getCell();
			if (!niCell.isUndefinedCell())
				v.add(cell.text);
		}
		String text = v.toString();
		text = text.substring(1, text.length() - 1);
		return new NiTextCell(text, cells.get(0).getMainId());
	}

	@Override
	public NiTextCell getZeroCell() {
		return new NiTextCell("", -1);
	}

	@Override
	public NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
		return new NiSplitCell((NiReportColumn<?>) splitterCells.get(0).getEntity(), 
				splitterCells.get(0).getDisplayedValue(), 
				splitterCells.stream().map(z -> z.getCell().entityId).collect(toSet()), 
				splitterCells.stream().anyMatch(z -> z.isUndefinedCell()));
	}
}
