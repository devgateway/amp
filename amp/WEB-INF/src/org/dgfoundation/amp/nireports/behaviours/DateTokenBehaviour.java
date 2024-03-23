package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.output.nicells.NiDateCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.time.LocalDate;
import java.util.*;

import static org.dgfoundation.amp.algo.AmpCollections.any;
import static org.dgfoundation.amp.algo.AmpCollections.sorted;

/**
 * the behaviour of a {@link DateCell}-populated column.
 * These columns have no trail cells. 
 * One notable thing about their behaviour is that, upon horizontally reducing multiple {@link DateCell} instances into a {@link NiDateCell} one, 
 * the dates are displayed in <i>chronological order</i>.
 * @author Dolghier Constantin
 *
 */
public class DateTokenBehaviour implements Behaviour<NiDateCell> {

    public final static DateTokenBehaviour instance = new DateTokenBehaviour();
    
    @Override
    public TimeRange getTimeRange() {
        return TimeRange.NONE;
    }
    
    protected DateTokenBehaviour(){}
    
    @Override
    public NiDateCell doHorizontalReduce(List<NiCell> cells) {
        Map<Long, LocalDate> entityIdsValues = new HashMap<>();
        for(NiCell niCell:cells) {
            DateCell cell = (DateCell) niCell.getCell();
            entityIdsValues.put(cell.entityId, cell.date);
        }
        return new NiDateCell(sorted(entityIdsValues.values()), any(entityIdsValues.keySet(), -1l), entityIdsValues);
    }

    @Override
    public NiDateCell getZeroCell() {
        return new NiDateCell(Collections.emptyList(), -1l, Collections.emptyMap());
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

    @Override
    public boolean hasPercentages() {
        return false;
    }
    
    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return false;
    }

}
