package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;

import java.util.Set;

public abstract class AmpMEValuesFormTableFeaturePanel extends AmpMEFormTableFeaturePanel<AmpIndicator, AmpIndicatorValue> {
    private static Logger logger = Logger.getLogger(AmpMEValuesFormTableFeaturePanel.class);

    protected IModel<Set<AmpIndicatorValue>> parentModel;
    protected IModel<Set<AmpIndicatorValue>> setModel;

    public AmpMEValuesFormTableFeaturePanel(
            String id, IModel<AmpIndicator> model, String fmName, boolean hideLeadingNewLine, int titleHeaderColSpan) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);

        getTableId().add(new AttributeModifier("width", "620"));

        setTitleHeaderColSpan(titleHeaderColSpan);
        parentModel = new PropertyModel<>(model, "indicatorValues");

        setModel = new AbstractMixedSetModel<AmpIndicatorValue>(parentModel) {
            @Override
            public boolean condition(AmpIndicatorValue item) {
                return false;
            }
        };
    }


    protected AmpTextFieldPanel<Double> getActualValue = new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(null, "actualValue"), "Actual Value") {
        public IConverter getInternalConverter(java.lang.Class<?> type) {
            return CustomDoubleConverter.INSTANCE;
        }
    };

}
