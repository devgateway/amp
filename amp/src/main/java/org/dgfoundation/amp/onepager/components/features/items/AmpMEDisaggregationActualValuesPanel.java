package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;

import java.util.*;

/**
 * Panel rendering and editing ACTUAL values (AmpIndicatorGlobalValue entries with type ACTUAL)
 * for a specific AmpIndicatorDisaggregationValue child category combination.
 */
public class AmpMEDisaggregationActualValuesPanel extends AmpFeaturePanel<AmpIndicatorDisaggregationValue> {

    private final ListEditor<AmpIndicatorGlobalValue> listView;
    private final IModel<AmpActivityLocation> activityLocationModel;

    public AmpMEDisaggregationActualValuesPanel(String id, IModel<AmpIndicatorDisaggregationValue> model) {
        this(id, model, null);
    }

    public AmpMEDisaggregationActualValuesPanel(String id, IModel<AmpIndicatorDisaggregationValue> model,
                                                IModel<AmpActivityLocation> activityLocationModel) {
        super(id, model, "Disaggregation Actual Values", true);
        this.activityLocationModel = activityLocationModel;
        setOutputMarkupId(true);

        IModel<Set<AmpIndicatorGlobalValue>> actualValuesModel = new PropertyModel<>(model, "actualValues");
        IModel<Set<AmpIndicatorGlobalValue>> locationActualValuesModel = new AbstractMixedSetModel<AmpIndicatorGlobalValue>(
                actualValuesModel) {
            @Override
            public boolean condition(AmpIndicatorGlobalValue item) {
                return matchesActivityLocation(item.getActivityLocation());
            }
        };

        listView = new ListEditor<AmpIndicatorGlobalValue>("rows", locationActualValuesModel) {
            @Override
            protected void onPopulateItem(ListItem<AmpIndicatorGlobalValue> item) {
                item.setOutputMarkupId(true);
                item.add(new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "originalValue"), "Actual Value") {
                    public IConverter<Double> getInternalConverter(java.lang.Class<?> type) {
                        return CustomDoubleConverter.INSTANCE;
                    }
                });
                item.add(new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "originalValueDate"), "Actual Date"));
                item.add(new ListEditorRemoveButton("delActualValue", "Delete", "Delete") {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        super.onClick(target);
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEDisaggregationActualValuesPanel.this));
                        target.add(AmpMEDisaggregationActualValuesPanel.this);
                    }
                });
            }
        };
        listView.setOutputMarkupId(true);
        add(listView);

        AmpAjaxLinkField addActual = new AmpAjaxLinkField("addDisaggActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorDisaggregationValue disaggVal = AmpMEDisaggregationActualValuesPanel.this.getModel().getObject();
                if (disaggVal.getActualValues() == null) {
                    disaggVal.setActualValues(new java.util.HashSet<>());
                }
                AmpIndicatorGlobalValue val = new AmpIndicatorGlobalValue(AmpIndicatorGlobalValue.ACTUAL);
                val.setIndicator(disaggVal.getIndicator());
                val.setActivityLocation(getActivityLocation());
                val.setOriginalValueDate(new Date());
                listView.addItem(val);
                target.add(AmpMEDisaggregationActualValuesPanel.this);
            }
        };
        addActual.setOutputMarkupId(true);
        add(addActual);
    }

    private boolean matchesActivityLocation(AmpActivityLocation itemLocation) {
        AmpActivityLocation activityLocation = getActivityLocation();
        if (activityLocation == null) {
            return itemLocation == null;
        }
        if (itemLocation == activityLocation) {
            return true;
        }
        if (itemLocation == null) {
            return false;
        }
        if (itemLocation.getId() != null || activityLocation.getId() != null) {
            return Objects.equals(itemLocation.getId(), activityLocation.getId());
        }
        return Objects.equals(getLocationId(itemLocation), getLocationId(activityLocation));
    }

    private AmpActivityLocation getActivityLocation() {
        return activityLocationModel != null ? activityLocationModel.getObject() : null;
    }

    private Long getLocationId(AmpActivityLocation activityLocation) {
        return activityLocation != null && activityLocation.getLocation() != null
                ? activityLocation.getLocation().getId() : null;
    }
}
