package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.TimeRange;

/**
 * the behaviour of an entity which equals max(cell) - min(cell)
 * @author Dolghier Constantin
 *
 */
public class VarianceMeasureBehaviour extends AbstractComputedBehaviour<NiAmountCell> {
    public final static VarianceMeasureBehaviour instance = new VarianceMeasureBehaviour(TimeRange.MONTH);
    
    protected VarianceMeasureBehaviour(TimeRange timeRange) {
        super(timeRange);
    }
    
    @Override
    public NiAmountCell getZeroCell() {
        return NiAmountCell.ZERO;
    }
    
    @Override
    public NiAmountCell doHorizontalReduce(List<NiCell> cells) {
        BigDecimal max = null, min = null;
        for(NiCell cell:cells) {
            BigDecimal value = cell.getAmount();
            if (max == null || max.compareTo(value) < 0) max = value;
            if (min == null || min.compareTo(value) > 0) min = value;
        }
        if (max == null)
            return null;
        
        NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
        return new NiAmountCell(max.subtract(min), precision);
    }
    
    @Override
    public NiOutCell doVerticalReduce(Collection<NiAmountCell> cells) {
        return NiTextCell.EMPTY;
    }
}
