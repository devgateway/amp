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

/**
 * @author tmugo@developmentgateway.org
 * @since Apr 21, 2023
 */

public class AmpMETargetValuesSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpIndicatorGlobalValue> {
    public AmpMETargetValuesSubsectionFeaturePanel(String id, String fmName,
                                                   IModel<AmpIndicator> indicator,
                                                   final IModel<Set<AmpIndicatorValue>> value) {
        super(id, fmName);

        AmpTextFieldPanel<Double> targetValueContainer = new AmpTextFieldPanel<Double>("targetValue",
                new PropertyModel<Double>(indicator, "targetValue.value"), "Target Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };

        targetValueContainer.getTextContainer().setEnabled(true);
        add(targetValueContainer);

        AmpDatePickerFieldPanel targetValueDateContainer = new AmpDatePickerFieldPanel("targetValueDate",
                new PropertyModel<>(indicator, "targetValue.originalValueDate"), "Target Value Date");
        targetValueDateContainer.getDate().setEnabled(true);
        add(targetValueDateContainer);

        AmpTextFieldPanel<Double> targetValueCommentContainer = new AmpTextFieldPanel<Double>("revisedTargetValue",
                new PropertyModel<Double>(indicator, "targetValue.revisedValue"), "Revised Target Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };

        targetValueCommentContainer.getTextContainer().setEnabled(true);
        add(targetValueCommentContainer);

        AmpDatePickerFieldPanel revisedTargetValueDateContainer = new AmpDatePickerFieldPanel("revisedTargetValueDate",
                new PropertyModel<>(indicator, "targetValue.revisedValueDate"), "Revised Target Value Date");
        revisedTargetValueDateContainer.getDate().setEnabled(true);
        add(revisedTargetValueDateContainer);

    }
}
