package org.dgfoundation.amp.nireports.behaviours;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TimeRange;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * the behaviour of a once-per-entity column (like Proposed Project Cost)
 * @author Dolghier Constantin
 *
 */
public class NumericalColumnBehaviour extends TrivialMeasureBehaviour {
    public static NumericalColumnBehaviour getInstance() {return instance;}
    
    private final static NumericalColumnBehaviour instance = new NumericalColumnBehaviour();
    
    protected NumericalColumnBehaviour() {}
    
    @Override
    public TimeRange getTimeRange() {
        return TimeRange.NONE;
    }
    
    @Override
    public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        return null;
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return NiTextCell.EMPTY;
    }
    
    @Override
    public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
        if (context.spec.isShowOriginalCurrency()) {
            AmpCurrency usedCurrency = context.spec.getSettings() == null 
                    || context.spec.getSettings().getCurrencyCode() == null ? AmpARFilter.getDefaultCurrency() 
                    : CurrencyUtil.getAmpcurrency(context.spec.getSettings().getCurrencyCode());
                    
            VSplitStrategy byCurrency = CurrencyMeasureSplittingStrategy.getInstance(usedCurrency);
            return Arrays.asList(byCurrency);
        }
        
        return Collections.emptyList();
    }
    
}
