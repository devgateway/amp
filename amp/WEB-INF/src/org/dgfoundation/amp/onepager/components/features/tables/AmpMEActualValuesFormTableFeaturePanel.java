package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.MEListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

/**
 * @author tmugo@developmentgateway.org
 * since Apr 27 2023
 * */
public class AmpMEActualValuesFormTableFeaturePanel extends AmpMEValuesFormTableFeaturePanel {
    private static Logger logger = Logger.getLogger(AmpMEActualValuesFormTableFeaturePanel.class);
    public AmpMEActualValuesFormTableFeaturePanel(String id, IModel<AmpIndicator> model, IModel<IndicatorActivity> indicatorActivity, IModel<AmpActivityLocation> location,
                                                  String fmName, boolean hideLeadingNewLine, int titleHeaderColSpan) throws Exception {
        super(id, model, indicatorActivity,location, fmName, hideLeadingNewLine, 7);
        try {
            list = new MEListEditor<AmpIndicatorValue>("listIndicators", setModel) {
                @Override
                protected void onPopulateItem(ListItem<AmpIndicatorValue> item) {
                    super.onPopulateItem(item);
                    AmpTextFieldPanel<Double> valuefield = new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "value"), "Actual Value"){
                        public IConverter getInternalConverter(java.lang.Class<?> type) {
                            return CustomDoubleConverter.INSTANCE;
                        }
                    };
                    valuefield.getTextContainer().setType(Double.class);
                    item.add(valuefield);

                    AmpDatePickerFieldPanel date = new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "valueDate"), "Actual Date");
                    item.add(date);

                    item.add(new ListEditorRemoveButton("delActualValue", "Delete Actual Value"){
                        protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                            super.onClick(target);
                            target.add(AmpMEActualValuesFormTableFeaturePanel.this);
                            target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEActualValuesFormTableFeaturePanel.this));
                        };
                    });
                }
            };

            add(list);
            addExpandableList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
