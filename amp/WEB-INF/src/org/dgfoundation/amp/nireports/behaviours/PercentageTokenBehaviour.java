package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.TimeRange;

import java.math.BigDecimal;
import java.util.*;

import static org.dgfoundation.amp.algo.AmpCollections.any;

/**
 * The {@link Behaviour} of a Percentageful texts column. 
 * Since the percentages are only relevant when using the column as a hierarchy, this class is identical to {@link TextualTokenBehaviour}, 
 * save for the fact that it consumes {@link PercentageTextCell} instead of {@link TextCell}. 
 * For example, just as in {@link TextualTokenBehaviour}, multiple {@link org.dgfoundation.amp.nireports.PercentageTextCell} instances are horizontally reduced to a concatenation of the alphabetically-sorted values 
 * @author Dolghier Constantin
 *
 * DUMMY ZOSO
 */
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
        return NiTextCell.EMPTY;
    }

    @Override
    public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
        return new PercentageTextCell("", mainId, entityId, Optional.of(levelColumn), BigDecimal.ONE);
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
        return true;
    }
    
    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return false;
    }
}
