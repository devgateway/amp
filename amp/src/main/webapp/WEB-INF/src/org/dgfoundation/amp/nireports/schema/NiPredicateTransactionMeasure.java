package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;

/**
 * a measure defined as being the result of applying a {@link Predicate} on either filtered or unfiltered cells and outputting the nonnull ones
 * @author Dolghier Constantin
 *
 */
public abstract class NiPredicateTransactionMeasure extends NiReportMeasure<CategAmountCell> {
    
    protected final boolean ignoreFilters;
    
    public NiPredicateTransactionMeasure(String measureName, Behaviour<?> behaviour, String description, boolean ignoreFilters) {
        super(measureName, behaviour, description);
        this.ignoreFilters = ignoreFilters;
    }
    
    public NiPredicateTransactionMeasure(String measureName, String description) {
        this(measureName, TrivialMeasureBehaviour.getInstance(), description, false);
    }
    
    public List<CategAmountCell> fetch(List<CategAmountCell> funding) {
        return funding.stream().map(this::processCell).filter(z -> z != null).collect(Collectors.toList());
    }
    
    protected abstract CategAmountCell processCell(CategAmountCell src);
    
    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) {
        if (ignoreFilters) {
            return fetch(engine.unfilteredFunding);
        } else {
            return fetch(engine.funding);
        }       
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
