package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.me.singlecountry.AmpMEActualValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpIndicatorDisaggregationValue;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;

import java.util.*;

/**
 * Panel rendering and editing ACTUAL values (AmpIndicatorGlobalValue entries with type ACTUAL)
 * for a specific AmpIndicatorDisaggregationValue child category combination.
 */
public class AmpMEDisaggregationActualValuesPanel extends AmpFeaturePanel<AmpIndicatorDisaggregationValue> {

    private final ListView<AmpIndicatorGlobalValue> listView;

    public AmpMEDisaggregationActualValuesPanel(String id, IModel<AmpIndicatorDisaggregationValue> model) {
        super(id, model, "Disaggregation Actual Values", true);
        setOutputMarkupId(true);

        // Use a List model for ListView (was Set, causing type mismatch)
        IModel<List<AmpIndicatorGlobalValue>> listModel = new LoadableDetachableModel<List<AmpIndicatorGlobalValue>>() {
            @Override
            protected List<AmpIndicatorGlobalValue> load() {
                AmpIndicatorDisaggregationValue disagg = AmpMEDisaggregationActualValuesPanel.this.getModel().getObject();
                if (disagg.getActualValues() == null) {
                    disagg.setActualValues(new java.util.HashSet<>());
                }
                return new java.util.ArrayList<>(disagg.getActualValues());
            }
        };

        listView = new ListView<AmpIndicatorGlobalValue>("rows", listModel) {
            @Override
            protected void populateItem(ListItem<AmpIndicatorGlobalValue> item) {
                AmpIndicatorGlobalValue val = item.getModelObject();
                item.setOutputMarkupId(true);
                item.add(new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "originalValue"), "Actual Value") {
                    public org.apache.wicket.util.convert.IConverter getInternalConverter(java.lang.Class<?> type) {
                        return CustomDoubleConverter.INSTANCE;
                    }
                });
                item.add(new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "originalValueDate"), "Actual Date"));
                item.add(new ListEditorRemoveButton("delActualValue", "Delete", "Delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        AmpMEDisaggregationActualValuesPanel.this.getModel().getObject().getActualValues().remove(val);
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
                val.setOriginalValueDate(new Date());
                disaggVal.getActualValues().add(val);
                target.add(AmpMEDisaggregationActualValuesPanel.this);
            }
        };
        addActual.setOutputMarkupId(true);
        add(addActual);
    }
}
