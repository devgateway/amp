package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.math.MathContext;
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
 * the behaviour of an entity which equals sum(cells) / nr(cells)
 * @author Dolghier Constantin
 *
 */
public class AverageAmountBehaviour extends AbstractComputedBehaviour<NiAmountCell> {
    public final static AverageAmountBehaviour instance = new AverageAmountBehaviour(TimeRange.MONTH);
    
    public AverageAmountBehaviour(TimeRange timeRange) {
        super(timeRange);
    }

    @Override
    public NiAmountCell getZeroCell() {
        return NiAmountCell.ZERO;
    }

    @Override
    public NiAmountCell doHorizontalReduce(List<NiCell> cells) {
        if (cells.isEmpty())
            return NiAmountCell.ZERO;
        NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
        BigDecimal sum = precision.adjustPrecision(BigDecimal.ZERO);
        
        for(NiCell cell:cells) {
            BigDecimal value = cell.getAmount();
            sum = sum.add(value);
        }
        
        BigDecimal res = sum.divide(BigDecimal.valueOf(cells.size()), new MathContext(50));
        return new NiAmountCell(precision.adjustPrecision(res), precision);
    }
    
    @Override
    public NiOutCell doVerticalReduce(Collection<NiAmountCell> cells) {
        return NiTextCell.EMPTY;
    }

}
