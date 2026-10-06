package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.components.*;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import java.util.Objects;
import java.util.Set;

public abstract class AmpMEValuesFormTableFeaturePanel extends AmpMEFormTableFeaturePanel<AmpIndicator, AmpIndicatorValue> {

    protected IModel<Set<AmpIndicatorValue>> parentModel;
    protected IModel<Set<AmpIndicatorValue>> setModel;
    protected IModel<Set<AmpIndicatorValue>> setBaseTargetModel;

    public AmpMEValuesFormTableFeaturePanel(
            String id, IModel<AmpIndicator> model, IModel<IndicatorActivity> indicatorActivity, IModel<AmpActivityLocation> location, String fmName, boolean hideLeadingNewLine, int titleHeaderColSpan) throws Exception {
        super(id, model, fmName, hideLeadingNewLine);

        getTableId().add(new AttributeModifier("width", "620"));

        setTitleHeaderColSpan(titleHeaderColSpan);
        parentModel = new PropertyModel<>(indicatorActivity, "values");

        setModel = new AbstractMixedSetModel<AmpIndicatorValue>(parentModel) {
            @Override
            public boolean condition(AmpIndicatorValue item) {
                return item.getValueType() == AmpIndicatorValue.ACTUAL
                        && matchesActivityLocation(item.getActivityLocation(), location.getObject());
            }
        };

        setBaseTargetModel = new AbstractMixedSetModel<AmpIndicatorValue>(parentModel) {
            @Override
            public boolean condition(AmpIndicatorValue item) {
                return (item.getValueType() == AmpIndicatorValue.BASE || item.getValueType() == AmpIndicatorValue.TARGET)
                        && matchesActivityLocation(item.getActivityLocation(), location.getObject());
            }
        };
    }

    private static boolean matchesActivityLocation(AmpActivityLocation activityLocation,
                                                   AmpActivityLocation expectedActivityLocation) {
        if (expectedActivityLocation == null) {
            return activityLocation == null;
        }
        if (activityLocation == expectedActivityLocation) {
            return true;
        }
        if (activityLocation == null) {
            return false;
        }
        if (activityLocation.getId() != null || expectedActivityLocation.getId() != null) {
            return Objects.equals(activityLocation.getId(), expectedActivityLocation.getId());
        }
        return Objects.equals(getLocationId(activityLocation), getLocationId(expectedActivityLocation));
    }

    private static Long getLocationId(AmpActivityLocation activityLocation) {
        return activityLocation != null && activityLocation.getLocation() != null
                ? activityLocation.getLocation().getId() : null;
    }

    protected AmpTextFieldPanel<Double> getActualValue(ListItem<AmpIndicatorValue> item){
        return  new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "value"), "Actual Value") {
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
