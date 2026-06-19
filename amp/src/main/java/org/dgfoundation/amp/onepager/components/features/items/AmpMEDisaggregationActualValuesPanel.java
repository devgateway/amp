package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.models.AbstractMixedSetModel;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Panel rendering and editing ACTUAL values (AmpIndicatorGlobalValue entries with type ACTUAL)
 * for a specific AmpIndicatorDisaggregationValue child category combination.
 */
public class AmpMEDisaggregationActualValuesPanel extends AmpFeaturePanel<AmpIndicatorDisaggregationValue> {

    private final ListEditor<AmpIndicatorGlobalValue> listEditor;

    public AmpMEDisaggregationActualValuesPanel(String id, IModel<AmpIndicatorDisaggregationValue> model,
                                                IModel<AmpActivityLocation> location) {
        super(id, model, "Disaggregation Actual Values", true);
        setOutputMarkupId(true);

        // ListEditor.updateModel() writes submitted rows back into this backing set.
        IModel<Set<AmpIndicatorGlobalValue>> parentModel = new PropertyModel<>(model, "actualValues");
        IModel<Set<AmpIndicatorGlobalValue>> setModel = new AbstractMixedSetModel<AmpIndicatorGlobalValue>(parentModel) {
            @Override
            public boolean condition(AmpIndicatorGlobalValue item) {
                AmpActivityLocation currentLocation = location != null ? location.getObject() : null;
                if (currentLocation == null) {
                    return item.getActivityLocation() == null;
                }
                return item.getActivityLocation() != null
                        && Objects.equals(item.getActivityLocation().getId(), currentLocation.getId());
            }
        };

        listEditor = new ListEditor<AmpIndicatorGlobalValue>("rows", setModel) {
            @Override
            protected void onPopulateItem(ListItem<AmpIndicatorGlobalValue> item) {
                item.setOutputMarkupId(true);
                item.add(new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "originalValue"), "Actual Value") {
                    public IConverter<Double> getInternalConverter(Class<?> type) {
                        return CustomDoubleConverter.INSTANCE;
                    }
                });
                item.add(new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "originalValueDate"), "Actual Date"));
                item.add(new ListEditorRemoveButton("delActualValue", "Delete", "Delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        super.onClick(target);
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEDisaggregationActualValuesPanel.this));
                        target.add(AmpMEDisaggregationActualValuesPanel.this);
                    }
                });
            }
        };
        listEditor.setOutputMarkupId(true);
        add(listEditor);

        AmpAjaxLinkField addActual = new AmpAjaxLinkField("addDisaggActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorDisaggregationValue disaggVal = AmpMEDisaggregationActualValuesPanel.this.getModel().getObject();
                logger.info("Adding new ACTUAL value for disaggregation value: " + disaggVal);
                logger.info("Current actual values: " + disaggVal.getActualValues());
                if (disaggVal.getActualValues() == null) {
                    disaggVal.setActualValues(new HashSet<>());
                }
                AmpIndicatorGlobalValue val = new AmpIndicatorGlobalValue(AmpIndicatorGlobalValue.ACTUAL);
                val.setIndicator(disaggVal.getIndicator());
                if (location != null) {
                    val.setActivityLocation(location.getObject());
                }
                val.setOriginalValueDate(new Date());
                listEditor.addItem(val);
                target.add(AmpMEDisaggregationActualValuesPanel.this);
            }
        };
        addActual.setOutputMarkupId(true);
        add(addActual);
    }
}
