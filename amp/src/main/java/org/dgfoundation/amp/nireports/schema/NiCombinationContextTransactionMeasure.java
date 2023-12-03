package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Measure defined as a linear combination of context measures (instances of {@link NiCombinationContextTransactionMeasure}). <br />
 * The measure is configured by being given a set of {@link NiTransactionContextMeasure} and corresponding numbers. 
 * The measure works by repeating the steps below for each and every cell in the Funding column: <ol>
 *  <li>find a measure in the map for which {@link NiTransactionContextMeasure#criterion} returns nonnull</li>
 *  <li>if none exists, the given cell is ignored and no further processing happens</li>
 *  <li>if a match exists, the output of the criterion is multiplied by the value given at construction time and the resulting cell goes in the output</li>
 *  <li>if multiple matches exist, the behaviour is undefined. In the current implementation, an arbitrary match will be used for the previous step</li>
 * </ol> 
 * @author acartaleanu
 *
 */
public class NiCombinationContextTransactionMeasure extends NiReportMeasure<CategAmountCell> {
    
    /**
     * the defining precursor measures, as fed to the constructor
     */
    public final Map<NiTransactionContextMeasure, BigDecimal> terms;
    
    
    /**
     * the defining precusor measures, in an array. Array for iteration speed reasons
     */
    protected final NiTransactionContextMeasure[] measures;
    
    /**
     * the defining precursor measures' coefficients, in an array, enumerated in the same order as {@link #measures}. {@link #prods}[i] = {@link #terms}[{@link #measures}[i]]. Put here for iteration speed reasons. 
     */
    protected final BigDecimal[] prods; 
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public NiCombinationContextTransactionMeasure(String name, Map<NiTransactionContextMeasure, BigDecimal> terms, 
            Behaviour<?> behaviour, String description) {
        super(name, behaviour, description);
        this.terms = Collections.unmodifiableMap(new LinkedHashMap<>(terms));
        this.measures =  terms.keySet().toArray(new NiTransactionContextMeasure[0]);
        this.prods = terms.values().toArray(new BigDecimal[0]);
    }

    /**
     * iterates the stored precursor measures in {@link #measures}, searching for a match. Once a match is found, returns the result of multiplying the match with the corresponding factor from {@link #prods}
     * @param src
     * @param contexts
     * @return the cell to be inserted in the measure's output or null if this input cell should be ignored
     */
    public CategAmountCell processCell(CategAmountCell src, Object[] contexts) {
        for(int i = 0; i < measures.length; i++) {
            Boolean res = (Boolean) measures[i].criterion.apply(contexts[i], src);
            if (Boolean.TRUE.equals(res)) {
                CategAmountCell c = src.multiply(prods[i]);
                if (c != null)
                    return c;
                break;
            }
        }
        return null;
    }
    
    
    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
        Object[] contexts = new Object[measures.length];
        for (int i = 0; i < measures.length; i++ ) {
            contexts[i] = measures[i].contextBuilder.apply(engine);
        }
        return engine.funding.stream().map(cell -> this.processCell(cell, contexts)).filter(z -> z != null).collect(Collectors.toList());
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }

}
