package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toSet;
import static org.dgfoundation.amp.algo.AmpCollections.any;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

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
		Map<Long, String> entityIdsValues = new HashMap<>();
		for(NiCell niCell:cells) {
			PercentageTextCell cell = (PercentageTextCell) niCell.getCell();
			if (!niCell.isUndefinedCell())
				v.add(cell.text);
			entityIdsValues.put(cell.entityId, cell.text);
		}
		String text = v.toString();
		text = text.substring(1, text.length() - 1);
		return new NiTextCell(text, any(entityIdsValues.keySet(), -1l), entityIdsValues);
	}

	@Override
	public NiTextCell getZeroCell() {
		return new NiTextCell("", -1, null);
	}

	@Override
	public NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
		return new NiSplitCell((NiReportColumn<?>) splitterCells.get(0).getEntity(), 
				splitterCells.get(0).getDisplayedValue(), 
				splitterCells.stream().map(z -> z.getCell().entityId).collect(toSet()), 
				splitterCells.stream().anyMatch(z -> z.isUndefinedCell()));
	}

	@Override
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
		return new PercentageTextCell("", mainId, entityId, Optional.of(levelColumn), BigDecimal.ONE);
	}

	@Override
	public boolean isKeepingSubreports() {
		return false;
	}
}
