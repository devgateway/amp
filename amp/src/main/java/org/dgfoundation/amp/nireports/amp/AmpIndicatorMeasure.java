package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.behaviours.IndicatorMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorMeasure extends NiTransactionMeasure {

    public AmpIndicatorMeasure(String measureName, long valueType) {
        super(measureName,
                cac -> true,
                IndicatorMeasureBehaviour.getInstance(),
                AmpReportsSchema.measureDescriptions.get(measureName),
                false);
    }
}
