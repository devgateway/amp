package org.dgfoundation.amp.onepager.components.features.subsections;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import java.util.Set;

public class AmpMETargetValuesSubsectionFeaturePanel extends AmpSubsectionFeaturePanel {
    public AmpMETargetValuesSubsectionFeaturePanel(String id, String fmName,
                                                   IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> value) {
        super(id, fmName);

        AmpTextFieldPanel<Double> targetValue = new AmpTextFieldPanel<Double>("targetValue",
                new PropertyModel<Double>(indicator, "targetValue.value"), "Target Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };

        targetValue.getTextContainer().setEnabled(false);
        add(targetValue);

        AmpDatePickerFieldPanel targetValueDate = new AmpDatePickerFieldPanel("targetValueDate",
                new PropertyModel<>(indicator, "targetValue.valueDate"), "Target Value Date");
        targetValueDate.getDate().setEnabled(false);
        add(targetValueDate);

    }
}
