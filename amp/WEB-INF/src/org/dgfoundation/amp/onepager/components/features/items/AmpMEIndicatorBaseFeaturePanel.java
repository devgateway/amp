package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEBaseTargetValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AmpMEIndicatorBaseFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {

    private AmpMEBaseTargetValuesFormTableFeaturePanel baseActualValues = null;
    private AmpAjaxLinkField addBaseValue;
    private AmpAjaxLinkField addTargetValue;
    public AmpMEIndicatorBaseFeaturePanel(String id, String fmName, final IModel<IndicatorActivity> conn,
                                      IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values, IModel<AmpActivityLocation> location) throws Exception {
        super(id, fmName, true);
        if (values.getObject() == null) values.setObject(new HashSet<>());


        try {
            baseActualValues = new AmpMEBaseTargetValuesFormTableFeaturePanel("addBaseValue", indicator, conn, location,"Base Target Value", false, 7);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        add(baseActualValues);
        addBaseValue = new AmpAjaxLinkField("addBaseValueButton", "Add Base Value", "Add Base Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();
                value.setIndicatorConnection(conn.getObject());
                value.setActivityLocation(location.getObject());
                value.setValueDate(new Date(System.currentTimeMillis()));
                value.setValueType(AmpIndicatorValue.BASE);
                baseActualValues.getEditorList().addItem(value);


                target.add(baseActualValues);
                updateButtonVisibility(target);
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };

        addTargetValue = new AmpAjaxLinkField("addTargetValueButton", "Add Target Value", "Add Target Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();
                value.setIndicatorConnection(conn.getObject());
                value.setActivityLocation(location.getObject());
                value.setValueDate(new Date(System.currentTimeMillis()));
                value.setValueType(AmpIndicatorValue.TARGET);
                baseActualValues.getEditorList().addItem(value);

                target.add(baseActualValues);
                updateButtonVisibility(target);
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };
//        addTargetValue.setVisible(false);
//        addBaseValue.setVisible(false);

//        updateButtonVisibility();
        add(addTargetValue);
        add(addBaseValue);

    }
    private void updateButtonVisibility(AjaxRequestTarget target) {
        boolean baseValueExists = false;
        boolean targetValueExists = false;

        Set<AmpIndicatorValue> indicatorValues = baseActualValues.getEditorList().getModel().getObject();
        for (AmpIndicatorValue value : baseActualValues.getEditorList().getModel().getObject()) {
            if (value.getValueType() == AmpIndicatorValue.BASE) {
                baseValueExists = true;
            }
            if (value.getValueType() == AmpIndicatorValue.TARGET) {
                targetValueExists = true;
            }
            if (baseValueExists && targetValueExists) {
                break;
            }
        }

        addBaseValue.setVisible(!baseValueExists);
        addTargetValue.setVisible(!targetValueExists);

        // Add the components to the AjaxRequestTarget for UI update
        if (target != null) {
            target.add(addBaseValue);
            target.add(addTargetValue);
        }
    }
}
