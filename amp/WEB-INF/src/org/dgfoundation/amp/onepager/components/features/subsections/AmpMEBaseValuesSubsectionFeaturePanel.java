package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

import java.util.Set;

/**
 * @author tmugo@developmentgateway.org
 * @since Apr 21, 2023
 */
public class AmpMEBaseValuesSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpIndicatorGlobalValue> {
    public AmpMEBaseValuesSubsectionFeaturePanel(String id, String fmName,
                                                 IModel<AmpIndicator> indicator,
                                                 final IModel<IndicatorActivity> conn) {
        super(id, fmName);

        AmpTextFieldPanel<Double> baseValueContainer = new AmpTextFieldPanel<Double>("baseValue",
                new PropertyModel<>(indicator, "baseValue.value"), "Base Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        baseValueContainer.getTextContainer().setEnabled(false);
        add(baseValueContainer);

        AmpDatePickerFieldPanel baseValueDateContainer = new AmpDatePickerFieldPanel("baseValueDate",
                new PropertyModel<>(indicator, "baseValue.originalValueDate"), "Base Original Value Date");
        baseValueDateContainer.getDate().setEnabled(true);
        add(baseValueDateContainer);

        AmpTextFieldPanel<Double> baseValueAdjustedContainer = new AmpTextFieldPanel<Double>("revisedBaseValue",
                new PropertyModel<>(indicator, "baseValue.revisedValue"), "Base Revised Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };

        baseValueAdjustedContainer.getTextContainer().setEnabled(false);
        add(baseValueAdjustedContainer);

        AmpDatePickerFieldPanel baseValueAdjustedDateContainer = new AmpDatePickerFieldPanel("revisedBaseValueDate",
                new PropertyModel<>(indicator, "baseValue.revisedValueDate"), "Base Revised Value Date");
        baseValueAdjustedDateContainer.getDate().setEnabled(true);
        add(baseValueAdjustedDateContainer);

        AmpAjaxLinkField saveBaseValues = new AmpAjaxLinkField("saveBaseValues", "Save Base Values", "Save Base Values") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.getButton().add(new AttributeAppender("class", new Model<>("mon_eval_button"), " "));
            }

            

            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpIndicator ampIndicator = indicator.getObject();
                AmpIndicatorGlobalValue baseValuesSet = ampIndicator.getBaseValue();

                baseValuesSet.setOriginalValue(baseValueContainer.getTextContainer().getModelObject());
                baseValuesSet.setOriginalValueDate(baseValueDateContainer.getDate().getModelObject());
                baseValuesSet.setRevisedValue(baseValueAdjustedContainer.getTextContainer().getModelObject());
                baseValuesSet.setRevisedValueDate(baseValueAdjustedDateContainer.getDate().getModelObject());

                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
                target.appendJavaScript(OnePagerUtil.getClickToggle2JS(this.getParent()));
            }

        };

        add(saveBaseValues);

    }
}
