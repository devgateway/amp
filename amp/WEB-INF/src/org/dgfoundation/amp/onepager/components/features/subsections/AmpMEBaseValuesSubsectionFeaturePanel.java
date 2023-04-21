package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import java.util.Set;

public class AmpMEBaseValuesSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpIndicatorGlobalValue> {
    public AmpMEBaseValuesSubsectionFeaturePanel(String id, String fmName,
                                                 IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values) {
        super(id, fmName);

        AmpTextFieldPanel<Double> baseValue = new AmpTextFieldPanel<Double>("baseValue",
                new PropertyModel<Double>(indicator, "baseValue.value"), "Base Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        baseValue.getTextContainer().setEnabled(false);
        add(baseValue);

        AmpDatePickerFieldPanel baseValueDate = new AmpDatePickerFieldPanel("baseValueDate",
                new PropertyModel<>(indicator, "baseValue.valueDate"), "Base Value Date");
        baseValueDate.getDate().setEnabled(false);
        add(baseValueDate);

    }
}
