package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.util.*;

import static org.dgfoundation.amp.algo.AmpCollections.any;

/**
 * A close relative to {@link PercentageTokenBehaviour}.
 * Textual columns do not have totals, do not split by time, do not keep individual reports - thus the only notable thing is how horizontal reduction is performed:
 * multiple {@link org.dgfoundation.amp.nireports.TextCell} instances are horizontally reduced to a concatenation of the alphabetically-sorted values
 * @author Dolghier Constantin
 *
 */
public class TextualTokenBehaviour implements Behaviour<NiTextCell> {
    
    public final static TextualTokenBehaviour instance = new TextualTokenBehaviour(); 
    protected TextualTokenBehaviour(){}

    
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

    @Override
    public boolean hasPercentages() {
        return false;
    }
    
    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return false;
    }
}
