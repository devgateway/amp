package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

import java.util.ArrayList;
import java.util.List;

public class AmpMEFieldsSubsectionFeaturePanel extends AmpFeaturePanel<AmpIndicator> {
    public AmpMEFieldsSubsectionFeaturePanel(String id, String fmName) {
        super(id, fmName);

        List<AmpIndicatorValue> values = new ArrayList<>();

        // render the list
        AmpAjaxLinkField addActualValue = new AmpAjaxLinkField("addActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();

            }
        };

        add(addActualValue);
    }

}
