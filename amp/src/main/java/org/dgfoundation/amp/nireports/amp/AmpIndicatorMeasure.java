package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.behaviours.IndicatorMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorMeasure extends NiTransactionMeasure {

    public AmpIndicatorMeasure(String measureName, long valueType) {
        this(measureName, valueType, false);
    }

    public AmpIndicatorMeasure(String measureName, long valueType, boolean disaggregatedOnly) {
        this(measureName, valueType, disaggregatedOnly, AmpReportsSchema.measureDescriptions.get(measureName));
    }

    public AmpIndicatorMeasure(String measureName, long valueType, boolean disaggregatedOnly, String description) {
        super(measureName,
                cac -> cac.metaInfo.containsMeta(MetaCategory.INDICATOR_VALUE_TYPE.category, Long.valueOf(valueType))
                        && (!disaggregatedOnly || cac.metaInfo.hasMetaInfo(MetaCategory.CATEGORY_VALUE_ID.category)),
                IndicatorMeasureBehaviour.getInstance(),
                description,
                false);
    }
}
