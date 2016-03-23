package org.dgfoundation.amp.nireports.schema;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

public class TextualTokenBehaviour implements Behaviour<NiTextCell> {
	
	public final static TextualTokenBehaviour instance = new TextualTokenBehaviour(); 
	TextualTokenBehaviour(){}

	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	@Override
	public NiTextCell doHorizontalReduce(List<NiCell> cells) {
		Set<String> v = new TreeSet<>();
		Map<Long, String> entityIdsValues = new HashMap<>();
		for(NiCell niCell:cells) {
			TextCell cell = (TextCell) niCell.getCell();
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
		return NiTextCell.EMPTY;
	}

	@Override
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
		return new TextCell("", mainId, entityId, Optional.of(levelColumn));
	}

	@Override
	public boolean isKeepingSubreports() {
		return false;
	}

	@Override
	public NiOutCell getEmptyCell(ReportSpecification spec) {
		return null;
	}
}
