package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.*;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
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
                return true;
            }
        };
    }

    protected AmpTextFieldPanel<Double> getActualValue(ListItem<AmpIndicatorValue> item){
        return  new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "actualValue"), "Actual Value") {

            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }

        };

    }

    protected ListItem<AmpIndicatorValue> appendActualValueToItem (ListItem<AmpIndicatorValue> item){
        item.add(getActualValue(item));
        return item;
    }

    protected void addExpandableList() {
        if (list instanceof MEListEditor && ((MEListEditor) list).isExpandable()) {
            final ExpandableListNavigator<AmpIndicatorValue> pln = new ExpandableListNavigator<AmpIndicatorValue>(
                    "expandableNavigator", (ExpandableListEditor) list);
            pln.setOutputMarkupId(true);
            add(pln);
        } else {
            add(new EmptyPanel("expandableNavigator"));
        }
    }

}
