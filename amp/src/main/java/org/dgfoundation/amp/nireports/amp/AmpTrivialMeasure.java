package org.dgfoundation.amp.nireports.amp;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.output.nicells.NiAmountCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.digijava.module.aim.helper.Constants;

/**
 * a straightforward {@link NiTransactionMeasure} which converts AMP-specific configuration info into Ni-generic predicates
 * @author simple
 *
 */
public class AmpTrivialMeasure extends NiTransactionMeasure {

    /**
     * trivial measure defined by a transaction type (say, Commitments), an adjustment name (say, Actual) and a directed/not directed boolean
     */
    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
        this(measureName, transactionType, adjustmentTypeName, directed, false);
    }
    
    public AmpTrivialMeasure(String measureName, long transactionType, boolean directed) {
        this(measureName, transactionType, null, directed, false);
    }

    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed,
            boolean anySource) {
        this(measureName, transactionType, adjustmentTypeName, directed, anySource, cac -> false, false, null);
    }

    /**
     * trivial measure defined by a transaction type (say, Commitments), an adjustment name (say, Actual), a directed/not directed boolean, an ignore/obey filters boolean and a behaviour.
     * In case behaviour is null, a context-based one will be deducted from the combination of the other parameters
     */
    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters, Behaviour<NiAmountCell> overridingBehaviour) {
        this(measureName, transactionType, adjustmentTypeName, directed, cac -> false, ignoreFilters, overridingBehaviour);
    }

    /**
     * equivalent to {@link #AmpTrivialMeasure(String, long, String, boolean, Predicate, boolean, Behaviour)}, but also specifies the {@link #getPrecursorMeasures()}
     */
    public AmpTrivialMeasure(String measureName, Predicate<CategAmountCell> predicate, boolean unfiltered, Behaviour<NiAmountCell> behaviour, Map<String, Boolean> precursors) {
        super(measureName, predicate, behaviour, AmpReportsSchema.measureDescriptions.get(measureName), unfiltered, precursors);
    }
    
    /**
     * builds a measure dependent on an another measure
     * @param measureName
     * @param baseMeasure
     * @param unfiltered
     * @param behaviour
     */
    public AmpTrivialMeasure(String measureName, AmpTrivialMeasure baseMeasure, boolean unfiltered, Behaviour<NiAmountCell> behaviour) {
        super(measureName, baseMeasure.criterion, behaviour, AmpReportsSchema.measureDescriptions.get(measureName), unfiltered, AbstractReportsSchema.singletonMap(baseMeasure.name, false));
    }
    
    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, 
            Predicate<CategAmountCell> or) {
        this(measureName, transactionType, adjustmentTypeName, directed, or, false, null);      
    }
    
    /**
     * intoruced because the compiler has problems solving the following:
     *  Optional.of(overridingBehaviour).orElseGet(directed ? new DirectedMeasureBehaviour() : TrivialMeasureBehaviour.getInstance()),  
     * would have looked neat, but doesn't work. Extracted method to make the constructor less cumbersome
     * @param overridingBehaviour
     * @param directed
     * @return
     */
    private static Behaviour<NiAmountCell> getBehaviour(Behaviour<NiAmountCell> overridingBehaviour, boolean directed) {
        if (overridingBehaviour != null)
            return overridingBehaviour;
        else 
            return directed ? new DirectedMeasureBehaviour() : TrivialMeasureBehaviour.getInstance();
    }

    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed,
            Predicate<CategAmountCell> or, boolean ignoreFilters, Behaviour<NiAmountCell> overridingBehaviour) {
        this(measureName, transactionType, adjustmentTypeName, directed, false, or, ignoreFilters,
                overridingBehaviour);
    }

    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed,
            boolean anySource, Predicate<CategAmountCell> or, boolean ignoreFilters,
            Behaviour<NiAmountCell> overridingBehaviour) {
        this(measureName, transactionType, adjustmentTypeName, directed, anySource, or, ignoreFilters,
                overridingBehaviour, Collections.emptyMap());
    }
    
    public AmpTrivialMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed,
            boolean anySource, Predicate<CategAmountCell> or, boolean ignoreFilters,
            Behaviour<NiAmountCell> overridingBehaviour, Map<String, Boolean> precursors) {

        this(measureName, 
                cac -> 
                    or.test(cac) || (
                        cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
                        (adjustmentTypeName == null 
                            || cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName)) 
                        && (directed ? isDirected(cac) : anySource || isDonorSourced(cac))
                    ),
                ignoreFilters,
                getBehaviour(overridingBehaviour, directed),
                precursors
            );
    }
    
    /**
     * trivial measure which filters by transactionType only
     * @param measureName
     * @param transactionType
     */
    public AmpTrivialMeasure(String measureName, long transactionType) {
        super(measureName, 
                cac -> cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)),
                TrivialMeasureBehaviour.getInstance(),
                AmpReportsSchema.measureDescriptions.get(measureName),
                false
            );
    }
    
    protected static boolean isDonorSourced(CategAmountCell cac) {
        return /*(!cac.metaInfo.hasMetaInfo(MetaCategory.SOURCE_ROLE.category)) || */cac.metaInfo.containsMeta(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
    }
    
    protected static boolean isDirected(CategAmountCell cac) {
        return cac.metaInfo.hasMetaInfo(MetaCategory.DIRECTED_TRANSACTION_FLOW.category);
    }
}
