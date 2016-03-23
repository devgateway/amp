package org.dgfoundation.amp.nireports.schema;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.output.NiDateCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

public class DateTokenBehaviour implements Behaviour<NiDateCell> {

	public final static DateTokenBehaviour instance = new DateTokenBehaviour();
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private DateTokenBehaviour(){}
	
	@Override
	public NiDateCell doHorizontalReduce(List<NiCell> cells) {
		Map<Long, LocalDate> entityIdsValues = new HashMap<>();
		for(NiCell niCell:cells) {
			DateCell cell = (DateCell) niCell.getCell();
			entityIdsValues.put(cell.entityId, cell.date);
		}
		return new NiDateCell(any(entityIdsValues.keySet(), -1l), entityIdsValues);
	}

	@Override
	public NiDateCell getZeroCell() {
		return new NiDateCell(-1l, Collections.emptyMap());
	}

	@Override
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
		return new DateCell(null, mainId, entityId, Optional.ofNullable(levelColumn));
	}

	@Override
	public boolean isKeepingSubreports() {
		return false;
	}

	@Override
	public NiOutCell getEmptyCell(ReportSpecification spec) {
		return NiTextCell.EMPTY;
	}
}
