package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEActualValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

public class AmpMEValuesSubsectionFeature extends AmpSubsectionFeaturePanel<AmpIndicator> {
    public AmpMEValuesSubsectionFeature(String id, String fmName, IModel<AmpIndicator> model, IModel<IndicatorActivity> indicatorActivity) throws Exception {
        super(id, "Actual Values", model);

//        AmpMEActualValuesFormTableFeaturePanel valuesTable = new AmpMEActualValuesFormTableFeaturePanel("table", model, indicatorActivity,location, "Actual Values", false, 7);
//        add(valuesTable);

        AmpAjaxLinkField addActualValue = new AmpAjaxLinkField("addActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();

                value.setIndValId(model.getObject().getIndicatorId());
//                valuesTable.getEditorList().addItem(value);
//                target.add(valuesTable);
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };

        add(addActualValue);
    }
}
