package org.dgfoundation.amp.nireports.behaviours;

import java.math.BigDecimal;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * the abstract behaviour of a computed measure/column. Defines all the common behaviour so that subclasses can focus on implementing the business logic
 * @author Dolghier Constantin
 *
 */
public abstract class AbstractComputedBehaviour<V extends NiAmountCell> implements Behaviour<V> {
    
    protected final TimeRange timeRange;
    
    protected AbstractComputedBehaviour(TimeRange timeRange) {
        this.timeRange = timeRange;
    }
    
    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }
    
    @Override
    public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
        throw new RuntimeException("doing hierarchies by measures not supported");
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return NiTextCell.EMPTY;
    }

    @Override
    public boolean isKeepingSubreports() {
        return true;
    }

    @Override
    public boolean hasPercentages() {
        return false;
    }
    
    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        // trivial measures are copied verbatim to totals
        return new ImmutablePair<String, ColumnContents>(entity.name, fetchedContents);
    }
    
    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return true;
    }
    
    /**
     * a reductor which sums
     * @param l
     * @return
     */
    public static BigDecimal REDUCE_SUM(List<BigDecimal> l) {
        if (l.isEmpty()) return BigDecimal.ZERO;
        
        if (l.size() == 1) return l.get(0);
        
        BigDecimal res = BigDecimal.ZERO;
        for(BigDecimal term:l) {
            res = res.add(term);
        }
        return res;
    }
    
    /**
     * a reductor which multiplies
     * @param l
     * @return
     */
    public static BigDecimal REDUCE_MUL(List<BigDecimal> l) {
        if (l.isEmpty()) return BigDecimal.ONE;
        
        if (l.size() == 1) return l.get(0);
        
        BigDecimal res = BigDecimal.ONE;
        for(BigDecimal term:l) {
            res = res.multiply(term);
        }
        return res;
    }
}
