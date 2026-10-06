package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.MEListEditor;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEIndicatorBaseFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;

public class AmpMEBaseTargetValuesFormTableFeaturePanel extends AmpMEValuesFormTableFeaturePanel{
    private AmpMEIndicatorBaseFeaturePanel parentPanel;
    public AmpMEBaseTargetValuesFormTableFeaturePanel(String id, IModel<AmpIndicator> model, IModel<IndicatorActivity> indicatorActivity, IModel<AmpActivityLocation> location,
                                                      String fmName, boolean hideLeadingNewLine, int titleHeaderColSpan, AmpMEIndicatorBaseFeaturePanel parent) throws Exception {
        super(id, model, indicatorActivity,location, fmName, hideLeadingNewLine, 7);
        this.parentPanel = parent;
        try {
            list = new MEListEditor<AmpIndicatorValue>("listIndicators", setBaseTargetModel) {
                @Override
                protected void onPopulateItem(ListItem<AmpIndicatorValue> item) {
                    super.onPopulateItem(item);
                    AmpTextFieldPanel<Double> valuefield = null;
                    AmpDatePickerFieldPanel date = null;

                    // check if indicator value is base or target
                    if(item.getModel().getObject().getValueType() == AmpIndicatorValue.BASE){
                        valuefield = new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "value"), "Base Value"){
                            public IConverter getInternalConverter(java.lang.Class<?> type) {
                                return CustomDoubleConverter.INSTANCE;
                            }
                        };

                        date = new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "valueDate"), "Base Date");

                        item.add(new ListEditorRemoveButton("delActualValue", "Delete Base Value"){
                            protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                                super.onClick(target);
                                // Update the visibility of add buttons in the parent panel
                                parentPanel.updateButtonVisibility();
                                target.add(parentPanel);
                                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parentPanel));
                            };
                        });
                    } else if (item.getModel().getObject().getValueType() == AmpIndicatorValue.TARGET){
                        valuefield = new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "value"), "Target Value"){
                            public IConverter getInternalConverter(java.lang.Class<?> type) {
                                return CustomDoubleConverter.INSTANCE;
                            }
                        };

                        date = new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "valueDate"), "Target Date");

                        item.add(new ListEditorRemoveButton("delActualValue", "Delete Target Value"){
                            protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                                super.onClick(target);
                                // Update the visibility of add buttons in the parent panel
                                parentPanel.updateButtonVisibility();
                                target.add(parentPanel);
                                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parentPanel));
                            };
                        });
                    } else {
                        valuefield = new AmpTextFieldPanel<Double>("actualValue", new PropertyModel<>(item.getModel(), "value"), "Revised Value"){
                            public IConverter getInternalConverter(java.lang.Class<?> type) {
                                return CustomDoubleConverter.INSTANCE;
                            }
                        };

                        date = new AmpDatePickerFieldPanel("actualDate", new PropertyModel<>(item.getModel(), "valueDate"), "Revised Date");

                        item.add(new ListEditorRemoveButton("delActualValue", "Delete Revised Value"){
                            protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                                super.onClick(target);
                                // Update the visibility of add buttons in the parent panel
                                parentPanel.updateButtonVisibility();
                                target.add(parentPanel);
                                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parentPanel));
                            };
                        });
                    }

                    valuefield.getTextContainer().setType(Double.class);
                    item.add(valuefield);

                    item.add(date);
                }
            };

            add(list);
            addExpandableList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
