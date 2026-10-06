package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.behaviours.TaggedMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

/**
 * a trivial measure which splits itself according to the string Meta values stored in a meta of the cells
 * @author Alexandru Cartaleanu
 *
 */
public class TaggedMeasure extends NiTransactionMeasure {

    /**
     * trivial measure which is split down by a category
     */
    public TaggedMeasure(String measureName, long transactionType, String adjustmentTypeName, MetaCategory metacat, String pseudocolumnName) {
        super(measureName, 
                cac -> 
                    cac.metaInfo.containsMeta(MetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
                    cac.metaInfo.containsMeta(MetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName),
                    new TaggedMeasureBehaviour("Total " + measureName, metacat.category, pseudocolumnName),
                AmpReportsSchema.measureDescriptions.get(measureName), false
            );
    }
}
