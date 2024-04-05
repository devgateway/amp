package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.digijava.module.aim.helper.Constants;

import java.util.Collections;
import java.util.Map;

/**
 * copied from trivial AmpTrivialMeasure
 * @author acartaleanu
 *
 */
public class TrivialTestMeasure extends NiTransactionMeasure {

    
    public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters, Behaviour<?> beh, Map<String, Boolean> precursors) {
        super(measureName, 
            cac -> cac.metaInfo.containsMeta(TestMetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
            cac.metaInfo.containsMeta(TestMetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
            (directed ? (false) : (cac.metaInfo.containsMeta(TestMetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY))),
            beh,
            null,
            ignoreFilters,
            precursors
        );
    }

    public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters, Behaviour<?> beh) {
        this(measureName, transactionType, adjustmentTypeName, directed, ignoreFilters, beh, Collections.emptyMap());
    }

    public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters) {
        this(measureName, transactionType, adjustmentTypeName, directed, ignoreFilters, TrivialMeasureBehaviour.getInstance());
    }

    public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
        this(measureName, transactionType, adjustmentTypeName, directed, false);
    }
}
