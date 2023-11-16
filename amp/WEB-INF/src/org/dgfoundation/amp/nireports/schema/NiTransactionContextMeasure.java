package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * a measure defined as a transaction which is filtered based on context
 * @author Dolghier Constantin
 *
 */
public class NiTransactionContextMeasure<K> extends NiReportMeasure<CategAmountCell> {
    
    public final BiFunction<K, CategAmountCell, Boolean> criterion;
    public final Function<NiReportsEngine, K> contextBuilder;
        
    public NiTransactionContextMeasure(String measureName, Function<NiReportsEngine, K> contextBuilder, BiFunction<K, CategAmountCell, Boolean> criterion, Behaviour<?> behaviour, String description) {
        super(measureName, behaviour, description);
        this.criterion = criterion;
        this.contextBuilder = contextBuilder;
    }
    
    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) {
        K context = contextBuilder.apply(engine);
        return engine.funding.stream().filter(cell -> criterion.apply(context, cell)).collect(Collectors.toList());
    } 
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
