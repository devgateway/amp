package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorMeasure extends NiTransactionMeasure {

    public AmpIndicatorMeasure(String measureName, long valueType) {
        super(measureName,
                cac -> cac.metaInfo.containsMeta(MetaCategory.INDICATOR_VALUE_TYPE.category, Long.valueOf(valueType)),
                TrivialMeasureBehaviour.getInstance(),
                AmpReportsSchema.measureDescriptions.get(measureName),
                false);
    }
}
