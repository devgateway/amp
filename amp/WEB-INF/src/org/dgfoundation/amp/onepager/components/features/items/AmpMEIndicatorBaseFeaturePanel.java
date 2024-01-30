package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEBaseTargetValuesFormTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

import java.util.HashSet;
import java.util.Set;

public class AmpMEIndicatorBaseFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {
    public AmpMEIndicatorBaseFeaturePanel(String id, String fmName, final IModel<IndicatorActivity> conn,
                                      IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values, IModel<AmpActivityLocation> location) throws Exception {
        super(id, fmName, true);
        if (values.getObject() == null) values.setObject(new HashSet<>());


        AmpMEBaseTargetValuesFormTableFeaturePanel baseActualValues = null;

        try {
            baseActualValues = new AmpMEBaseTargetValuesFormTableFeaturePanel("addBaseValue", indicator, conn, location,"Base Target Value", false, 7);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        add(baseActualValues);

    }

}
