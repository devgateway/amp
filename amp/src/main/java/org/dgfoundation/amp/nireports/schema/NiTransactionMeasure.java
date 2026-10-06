package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * a trivial measure defined as a filter 
 * @author Dolghier Constantin
 *
 */
public class NiTransactionMeasure extends NiPredicateTransactionMeasure {
    
    public final Predicate<CategAmountCell> criterion;
    public final Map<String, Boolean> precursors;
    
    public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion, String description) {
        this(measureName, criterion, TrivialMeasureBehaviour.getInstance(), description, false);
    }
    
    public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion, Behaviour<?> behaviour, String description, boolean ignoreFilters) {
        this(measureName, criterion, behaviour, description, ignoreFilters, Collections.emptyMap());
    }
    
    public NiTransactionMeasure(String measureName, Predicate<CategAmountCell> criterion, Behaviour<?> behaviour, String description, boolean ignoreFilters, Map<String, Boolean> precursors) {
        super(measureName, behaviour, description, ignoreFilters);
        this.criterion = criterion;
        this.precursors = Collections.unmodifiableMap(new HashMap<>(precursors));
    }

    @Override
    public CategAmountCell processCell(CategAmountCell src) {
        return criterion.test(src) ? src : null;
    }
    
    @Override
    public Map<String, Boolean> getPrecursorMeasures() {
        return precursors;
    }
}
