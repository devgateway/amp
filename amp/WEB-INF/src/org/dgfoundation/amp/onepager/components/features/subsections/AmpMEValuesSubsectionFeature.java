package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEActualValuesFormTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpIndicator;

public class AmpMEValuesSubsectionFeature extends AmpSubsectionFeaturePanel<AmpIndicator> {
    public AmpMEValuesSubsectionFeature(String id, String fmName, IModel<AmpIndicator> model) throws Exception {
        super(id, fmName, model);

        AmpMEActualValuesFormTableFeaturePanel table = new AmpMEActualValuesFormTableFeaturePanel("table", model, fmName, false, 7);
        add(table);
    }
}
